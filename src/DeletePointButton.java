package button;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import base.Game;

public class DeletePointButton extends MouseButton {
	
	private DeletePointButton(Rectangle bounds, Runnable deletePoint) {
		super(bounds, deletePoint, Game.get().DEF_CAMERA);
	}
	
	public static DeletePointButton createDeletePointButton(Runnable deletePoint) {
		final int WIDTH = 100;
		final int HEIGHT = 30;
		final int X = 30;
		final int Y = 50;
		return new DeletePointButton(new Rectangle(X, Y, WIDTH, HEIGHT), deletePoint);
	}
	
	public void renderNormal() {
		BufferedImage red = Game.get().createLayer(bounds.width, bounds.height);
		Graphics g = red.getGraphics();
		g.setColor(Color.RED);
		g.fillRect(0, 0, bounds.width, bounds.height);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 16));
		g.drawString("Delete Point", 0, 20);
		renderNormalized(red, 0, 0, 0);
	}
	
	public void renderHighlighted() {
		BufferedImage orange = Game.get().createLayer(bounds.width, bounds.height);
		Graphics g = orange.getGraphics();
		g.setColor(Color.ORANGE);
		g.fillRect(0, 0, bounds.width, bounds.height);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 16));
		g.drawString("Delete Point", 0, 20);
		renderNormalized(orange, 0, 0, 0);
	}

}
