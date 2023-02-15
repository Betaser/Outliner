package state;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import base.Game;
import button.CreateOutlineButton;
import button.DeleteOutlineButton;
import button.OutlineTabButton;
import outline.Outline;
import outline.OutlineTab;
import outline.load.LoadOutline;

public class EditingOutlineState extends State {
	
	OutlineTab outlineTab;
	CreateOutlineButton cOB;
	DeleteOutlineButton dOB;
	List<OutlineTabButton> oTBtns;
	
	public EditingOutlineState() {
		oTBtns = new ArrayList<>();
	}
	
	public void init() {
		initOutlineTabButtons();
		cOB = CreateOutlineButton.createCreateOutlineButton(() -> {
			OutlineTab newOT = new OutlineTab(new Outline("New outline"));
			oTBtns.add(new OutlineTabButton(new Rectangle(), () -> {
				outlineTab = newOT;
			}, newOT));
			outlineTab = newOT;
			positionOutlineTabButtons();
		});
		dOB = DeleteOutlineButton.createDeleteOutlineButton(() -> {
			if (oTBtns.size() <= 1)
				return;
			int IDX = -1;
			for (int i = 0; i < oTBtns.size(); i++) {
				if (oTBtns.get(i).leadsTo == outlineTab) {
					IDX = i;
					break;
				}
			}
			oTBtns.remove(IDX);
			outlineTab = oTBtns.get(Math.max(0, Math.min(oTBtns.size() - 1, IDX - 1))).leadsTo;
			positionOutlineTabButtons();
		});
	}

	public void tick() {
		if (Game.get().input.rMouse.click())
			for (OutlineTabButton btn : oTBtns)
				btn.leadsTo.scrollPane.setFocusable(false);
		for (OutlineTabButton btn : oTBtns) {
			btn.tick();
			if (btn.leadsTo == outlineTab) 
				continue;
			btn.leadsTo.idleTick();
		}
		outlineTab.tick();
		cOB.tick();
		dOB.tick();
	}
	
	public void render() {
		outlineTab.render();
		renderOutlineTabs();
		cOB.render();
		dOB.render();
	}
	
	void initOutlineTabButtons() {
		initOutlineTabs();
		positionOutlineTabButtons();
	}
	
	void initOutlineTabs() {
		OutlineTab exOT = new OutlineTab(LoadOutline.loadOutlineFromFile(new File("res/outline1.txt")));
		OutlineTabButton exOTB = new OutlineTabButton(new Rectangle(), () -> outlineTab = exOT, exOT);
		oTBtns.add(exOTB);
		
		OutlineTab newOT = new OutlineTab(new Outline("new outline"));
		OutlineTabButton newOTB = new OutlineTabButton(new Rectangle(), () -> outlineTab = newOT, newOT);
		oTBtns.add(newOTB);
		
		outlineTab = exOT;
		exOTB.onPress.run();
//		newOTB.onPress.run();
	}
	
	void positionOutlineTabButtons() {
		final int TAB_WIDTH = (Game.get().getSceneWidth() - CreateOutlineButton.WIDTH - DeleteOutlineButton.WIDTH) / oTBtns.size();
		for (int i = 0; i < oTBtns.size(); i++) {
			final OutlineTabButton btn = oTBtns.get(i);
			final int WIDTH = TAB_WIDTH;
			final int HEIGHT = 30;
			final int X = i * TAB_WIDTH + DeleteOutlineButton.WIDTH;
			final int Y = Game.get().getSceneHeight() - HEIGHT;
			btn.bounds.x = X;
			btn.bounds.y = Y;
			btn.bounds.width = WIDTH;
			btn.bounds.height = HEIGHT;
		}
	}
	
	void renderOutlineTabs() {
		for (OutlineTabButton btn : oTBtns) {
			final OutlineTab tab = btn.leadsTo;
			BufferedImage img = Game.get().createLayer(btn.bounds.width, btn.bounds.height);
			Graphics g = img.getGraphics();
			if (tab == outlineTab) {
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(0, 0, img.getWidth(), img.getHeight());
			} else {
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, img.getWidth(), img.getHeight());
			}
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, img.getWidth() - 1, img.getHeight() - 1);
			g.setFont(new Font("Arial", Font.PLAIN, 20));
			g.drawString(tab.outline.contents, 0, 20);
			Game.get().draw(img, btn.bounds.x, btn.bounds.y, btn.bounds.width, btn.bounds.height, 0, Game.get().DEF_CAMERA);
			btn.render();
		}
	}
	
}