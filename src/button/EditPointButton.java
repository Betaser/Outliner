package button;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import base.Game;

public class EditPointButton extends MouseButton {
	
	public EditPointButton(Rectangle bounds, Runnable onClick) {
		super(bounds, onClick, Game.get().camera);
	}
	
	public void renderHighlighted() {
		BufferedImage yellow = Game.get().createLayer(bounds.width, bounds.height);
		Graphics g = yellow.getGraphics();
		g.setColor(new Color(200, 200, 0, 100));
		g.fillRect(0, 0, bounds.width, bounds.height);
		renderNormalized(yellow, 0, 0, 0);
	}

	public void renderNormal() {
//		BufferedImage green = Game.get().createLayer(bounds.width, bounds.height);
//		Graphics g = green.getGraphics();
//		g.setColor(new Color(0, 200, 0, 100));
//		g.fillRect(0, 0, bounds.width, bounds.height);
//		renderNormalized(green, 0, 0, 0);
	}

}
