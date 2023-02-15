package outline;

import java.util.List;
import java.util.Stack;

import miscDataStructures.Mut;

public class ShiftOutline {
	
	enum Shift {
		UP,
		DOWN,
		LEFT,
		RIGHT,
		NONE
	}
	
//	public static void main(String[] args) {
//		Outline outline = LoadOutline.loadOutlineFromFile(new File("res/outline1.txt"));
//		System.out.println(outline);
//		Outline o = outline.get(5);
//		System.out.println("outline: " + o.contents);
////		outline.remove(o);
////		System.out.println(outline);
////		shiftLeft(outline, o);
////		System.out.println(outline);
////		shiftRight(outline, o);
////		System.out.println(outline);
////		shiftUp(outline, outline.get(6));
////		System.out.println(outline);
////		shiftDown(outline, outline.get(4));
////		System.out.println(outline);
//	}
	
	public static void shiftDown(Outline outline, Outline point) {
		Mut<Outline> parent = new Mut<>(outline);
		Mut<Runnable> shiftIt = new Mut<>(() -> System.out.println("can't shift down"));
		Outline.traverse(
			(o, d) -> {
				final List<Outline> PTS = parent.val.points;
				final int IDX = PTS.indexOf(point);
				if (IDX != -1 && IDX < PTS.size() - 1) {
					shiftIt.val = () -> PTS.add(IDX + 1, PTS.remove(IDX));
				}
				parent.val = o;
			},
			outline,
			0
		);
		shiftIt.val.run();
	}
	
	public static void shiftUp(Outline outline, Outline point) {
		Mut<Outline> parent = new Mut<>(outline);
		Mut<Runnable> shiftIt = new Mut<>(() -> System.out.println("can't shift up"));
		Outline.traverse(
			(o, d) -> {
				final List<Outline> PTS = parent.val.points;
				final int IDX = PTS.indexOf(point);
				if (IDX > 0) {
					shiftIt.val = () -> PTS.add(IDX - 1, PTS.remove(IDX));
				}
				parent.val = o;
			},
			outline,
			0
		);
		shiftIt.val.run();
	}
	
	public static void shiftRight(Outline outline, Outline point) {
		Mut<Outline> parent = new Mut<>(outline);
		Mut<Runnable> shiftIt = new Mut<>(() -> System.out.println("can't shift outline right"));
		Outline.traverse(
			(o, d) -> {
				final List<Outline> PTS = parent.val.points;
				final int IDX = PTS.indexOf(point);
				if (IDX > 0) {
					final Outline PARENT = parent.val;
					shiftIt.val = () -> PTS.get(IDX - 1).points.add(PARENT.remove(point));
				}
				parent.val = o;
			},
			outline,
			0
		);
		shiftIt.val.run();
	}
	
	public static void shiftLeft(Outline outline, Outline point) {
		Stack<Outline> outlines = new Stack<>();
		Mut<Integer> depth = new Mut<>(-1);
		Mut<Runnable> shiftIt = new Mut<>(() -> System.out.println("can't shift outline left"));
		Outline.traverse(
			(o, d) -> {
				for (int i = 0; i <= depth.val - d; i++)
					outlines.pop();
				outlines.push(o);
				depth.val = d;
				if (o == point && outlines.size() > 2) {
					final Outline NEW_PARENT = outlines.get(d - 2);
					final Outline DIRECT_PARENT = outlines.get(d - 1);
					shiftIt.val = () -> {
						NEW_PARENT.points.add(NEW_PARENT.points.indexOf(DIRECT_PARENT) + 1, DIRECT_PARENT.remove(point));
					};
				}
			},
			outline,
			0
		);
		shiftIt.val.run();
	}

}
