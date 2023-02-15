package button;

import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import base.Game;
import outline.OutlineTab;

public class OutlineTabButton extends MouseButton {
	
	public OutlineTab leadsTo;
	
	public OutlineTabButton(Rectangle bounds, Runnable setOutlineTab, OutlineTab leadsTo) {
		super(
			bounds, 
			() -> {
				setOutlineTab.run();
				for (Component c : Game.get().panelManager.getComponents()) {
					if (c instanceof JScrollPane) {
						if (c != leadsTo.scrollPane)
							c.setVisible(false);
						else
							c.setVisible(true);
					}
				}
			}, 
			Game.get().DEF_CAMERA
		);
		this.leadsTo = leadsTo;
	}
	
	public void renderHighlighted() {
		BufferedImage yellow = Game.get().createLayer(bounds.width, bounds.height);
		Graphics g = yellow.getGraphics();
		g.setColor(new Color(200, 200, 0, 100));
		g.fillRect(0, 0, bounds.width, bounds.height);
		renderNormalized(yellow, 0, 0, 0);
	}
	
	public void renderNormal() {}

}
