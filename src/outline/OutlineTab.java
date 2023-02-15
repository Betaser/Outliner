package outline;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import java.awt.Rectangle;

import base.Game;
import button.Button;
import button.ConfirmEditButton;
import button.CreatePointButton;
import button.DeletePointButton;
import button.SelectPointButton;
import miscDataStructures.Mut;
import miscDataStructures.PointSelection;
import outline.ShiftOutline.Shift;

public class OutlineTab {
	
	public static final int LN_HEIGHT = 20;
	public static final int IMG_HEIGHT = 30;
	public static final int TEXTFIELD_X_OFFSET = 100;
	public static int TEXTFIELD_HEIGHT;
	
	public Outline outline;
	public JScrollPane scrollPane;

	List<SelectPointButton> sPBtns;
	ConfirmEditButton cEB;
	DeletePointButton dPB;
	CreatePointButton cPB;
	Button.Group allButtons;
	JTextArea textArea;
	ScrollOutline scrollOutline;
	PointSelection pointSelection;
	List<Line> lines;
	Queue<Runnable> doQueue;
	
	public OutlineTab(Outline outline) {
		TEXTFIELD_HEIGHT = Game.get().getSceneHeight() - 30;
		this.outline = outline;
		scrollOutline = new ScrollOutline();
		scrollOutline.scrollToTop(outline);
		
		sPBtns = new ArrayList<>();
		lines = new ArrayList<>();
		doQueue = new LinkedList<>();
		allButtons = new Button.Group();
		initPointButtons();
		
		initTextArea();
		Line.genLinesFromOutline(lines, outline);
		genSelectPointButtons();
	}
	
	void initPointButtons() {
		allButtons.buttons = allButtons.buttons.stream().filter(btn -> !Arrays.asList(cEB, dPB, cPB).contains(btn)).collect(Collectors.toList());
		cEB = ConfirmEditButton.createConfirmEditButton(() -> System.out.println("no point selected to edit"));
		dPB = DeletePointButton.createDeletePointButton(() -> System.out.println("no point selected to delete"));
		cPB = CreatePointButton.createCreatePointButton(() -> System.out.println("no point selected to create point under"));
		allButtons.buttons.addAll(0, List.of(cEB, dPB, cPB));
	}
	
	public void initTextArea() {
		textArea = new JTextArea();
		textArea.setText("start typing here");
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setSize((int) (Game.get().winWidth * 0.7), (int) (Game.get().winHeight * 0.3));
		scrollPane.setLocation((Game.get().getSceneWidth() - scrollPane.getWidth()) / 2, Game.get().getSceneHeight() - scrollPane.getHeight() - 20);

		Game.get().panelManager.add(scrollPane, Integer.valueOf(1));
	}
	
	public void tick() {
		if (Game.get().input.rMouse.click()) {
			initPointButtons();
			allButtons.buttons.removeAll(List.of(cEB, dPB, cPB));
			scrollPane.setVisible(false);
		} else {
			allButtons.tick();
			shiftOutline();
			scrollOutline.scroll(outline);
		}
		
		//	genLinesFromOutline runs in doQueue
		for (Runnable r : doQueue)
			r.run();
		doQueue.clear();
	}
	
	public void idleTick() {

	}
	
	public void render() {
		RenderOutline.renderText(lines);
		allButtons.render();
		if (pointSelection != null)
			pointSelection.render();
	}
	
	void shiftOutline() {
		if (pointSelection != null) {
			Shift shift = Shift.NONE;
			if (Game.get().input.a.click()) {
				ShiftOutline.shiftLeft(outline, pointSelection.outline);
				shift = Shift.LEFT;
			}
			if (Game.get().input.d.click()) {
				ShiftOutline.shiftRight(outline, pointSelection.outline);
				shift = Shift.RIGHT;
			}
			if (Game.get().input.w.click()) {
				ShiftOutline.shiftUp(outline, pointSelection.outline);
				shift = Shift.UP;
			}
			if (Game.get().input.s.click()) {
				ShiftOutline.shiftDown(outline, pointSelection.outline);
				shift = Shift.DOWN;
			}
			if (shift != Shift.NONE)
				Line.genLinesFromOutline(lines, outline);
			if (shift == Shift.LEFT || shift == Shift.UP || shift == Shift.DOWN) {
				genSelectPointButtons();
				for (int i = 0; i < lines.size(); i++) {
					if (lines.get(i).outline == pointSelection.outline) {
						pointSelection.bounds = sPBtns.get(i).bounds;
						sPBtns.get(i).onPress.run();
					}
				}
			}
		}
	}
	
	void genSelectPointButtons() {
		allButtons.buttons.removeAll(sPBtns);
		sPBtns.clear();
		Mut<Integer> y = new Mut<>(0);
		final int N_TOTAL_LINES = Outline.reduce(
			(r, o, d) -> r + (int) o.contents.chars().filter(c -> c == '\n').count() + 1,
			0,
			outline,
			0
		);
		
		Outline.traverse(
			(o, d) -> {
				final int N_LINES = 1 + (int) o.contents.chars().filter(c -> c == '\n').count();
				Rectangle bounds = new Rectangle(
					0,
					((N_TOTAL_LINES - y.val) * LN_HEIGHT - IMG_HEIGHT) - (LN_HEIGHT * N_LINES) + LN_HEIGHT + 5,
					Game.get().getSceneWidth(),
					LN_HEIGHT * N_LINES
				);
				Rectangle btnBounds = new Rectangle(0, bounds.y, 60, bounds.height);
				SelectPointButton sPBtn = new SelectPointButton(btnBounds, () -> {
					pointSelection = new PointSelection(bounds, o);
					textArea.setText(o.contents);
					initPointButtons();
					scrollPane.setVisible(true);
					cEB.onPress = () -> cEBonPress(o);
					dPB.onPress = () -> dPBonPress(o);
					cPB.onPress = () -> cPBonPress(o);
					Game.get().focusGamePanel();
				});
				sPBtns.add(sPBtn);
				allButtons.buttons.add(sPBtn);
				y.val += N_LINES;
			},
			outline, 
			0
		);
	}
	
	void cEBonPress(Outline o) {
		o.contents = textArea.getText().replace("\t", "");
		Line.genLinesFromOutline(lines, outline);
		doQueue.add(() -> genSelectPointButtons());
		final int NEW_N_LINES = 1 + (int) o.contents.chars().filter(c -> c == '\n').count();
		pointSelection.bounds.height = LN_HEIGHT * NEW_N_LINES;
	}
	
	void dPBonPress(Outline o) {
		if (o == outline)
			return;
		outline.remove(o);
		Line.genLinesFromOutline(lines, outline);
		doQueue.add(() -> genSelectPointButtons());
		doQueue.add(() -> initPointButtons());
		pointSelection = null;
		scrollOutline.scrollPos -= LN_HEIGHT * (1 + (int) o.toString().chars().filter(c -> c == '\n').count());
	}
	
	void cPBonPress(Outline o) {
		Mut<Outline> parent = new Mut<>(outline);
		Mut<Outline> created = new Mut<>(new Outline(""));
		Mut<Runnable> createIt = new Mut<>(() -> outline.points.add(created.val));
		Outline.traverse(
			(o2, d2) -> {
				final List<Outline> PTS = parent.val.points;
				final int IDX = PTS.indexOf(o);
				if (IDX != -1) {
					createIt.val = () -> PTS.add(IDX + 1, created.val);
				}
				parent.val = o2;
			},
			outline,
			0
		);
		createIt.val.run();
		Line.genLinesFromOutline(lines, outline);
		pointSelection.outline = created.val;
		doQueue.add(() -> {
			genSelectPointButtons();
			for (int i = 0; i < lines.size(); i++) {
				if (lines.get(i).outline == created.val) {
					pointSelection.bounds = sPBtns.get(i).bounds;
					sPBtns.get(i).onPress.run();
				}
			}
		});
	}

}