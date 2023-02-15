package button;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import base.Game;

public class ConfirmEditButton extends MouseButton {
	
	private ConfirmEditButton(Rectangle bounds, Runnable confirmEdit) {
		super(bounds, confirmEdit, Game.get().DEF_CAMERA);
	}
	
	public static ConfirmEditButton createConfirmEditButton(Runnable confirmEdit) {
		final int WIDTH = 100;
		final int HEIGHT = 30;
		final int X = Game.get().getSceneWidth() - WIDTH - 30;
		final int Y = 50;
		return new ConfirmEditButton(new Rectangle(X, Y, WIDTH, HEIGHT), confirmEdit);
	}
	
	public void renderHighlighted() {
		BufferedImage green = Game.get().createLayer(bounds.width, bounds.height);
		Graphics g = green.getGraphics();
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, bounds.width, bounds.height);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 16));
		g.drawString("Confirm Edits", 0, 20);
		renderNormalized(green, 0, 0, 0);
	}
	
	public void renderNormal() {
		BufferedImage blue = Game.get().createLayer(bounds.width, bounds.height);
		Graphics g = blue.getGraphics();
		g.setColor(new Color(0, 0, 200, 255));
		g.fillRect(0, 0, bounds.width, bounds.height);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 16));
		g.drawString("Confirm Edits", 0, 20);
		renderNormalized(blue, 0, 0, 0);
	}

}
