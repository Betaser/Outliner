package button;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import base.Game;

public class SelectPointButton extends MouseButton {
	
	public SelectPointButton(Rectangle bounds, Runnable selectPoint) {
		super(bounds, selectPoint, Game.get().camera);
	}
	
	public void renderHighlighted() {
		BufferedImage pink = Game.get().createLayer(bounds.width, bounds.height);
		Graphics g = pink.getGraphics();
		g.setColor(Color.PINK);
		g.fillRect(0, 0, bounds.width, bounds.height);
		renderNormalized(pink, 0, 0, 0);
	}
	
	public void renderNormal() {}

}
