package button;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import base.Game;

public class CreatePointButton extends MouseButton {

	private CreatePointButton(Rectangle bounds, Runnable createPoint) {
		super(bounds, createPoint, Game.get().DEF_CAMERA);
	}
	
	public static CreatePointButton createCreatePointButton(Runnable createPoint) {
		final int WIDTH = 100;
		final int HEIGHT = 30;
		final int X = 30;
		final int Y = 100;
		return new CreatePointButton(new Rectangle(X, Y, WIDTH, HEIGHT), createPoint);
	}
	
	public void renderNormal() {
		BufferedImage green = Game.get().createLayer(bounds.width, bounds.height);
		Graphics g = green.getGraphics();
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, bounds.width, bounds.height);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 16));
		g.drawString("Create Point", 0, 20);
		renderNormalized(green, 0, 0, 0);
	}
	
	public void renderHighlighted() {
		BufferedImage yellow = Game.get().createLayer(bounds.width, bounds.height);
		Graphics g = yellow.getGraphics();
		g.setColor(Color.YELLOW);
		g.fillRect(0, 0, bounds.width, bounds.height);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 16));
		g.drawString("Create Point", 0, 20);
		renderNormalized(yellow, 0, 0, 0);
	}
	
}
