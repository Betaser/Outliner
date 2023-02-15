package button;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import base.Game;

public class DeleteOutlineButton extends MouseButton {
	
	public static final int WIDTH = 80, HEIGHT = 30;
	
	private DeleteOutlineButton(Rectangle bounds, Runnable deleteButton) {
		super(bounds, deleteButton, Game.get().DEF_CAMERA);
	}

	public static DeleteOutlineButton createDeleteOutlineButton(Runnable deleteButton) {
		final int X = 0;
		final int Y = Game.get().getSceneHeight() - HEIGHT;
		return new DeleteOutlineButton(new Rectangle(X, Y, WIDTH, HEIGHT), deleteButton);
	}
	
	public void renderHighlighted() {
		BufferedImage red = Game.get().createLayer(bounds.width, bounds.height);
		Graphics g = red.getGraphics();
		g.setColor(Color.RED);
		g.fillRect(0, 0, bounds.width, bounds.height);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.PLAIN, 16));
		g.drawString("Delete", 0, 20);
		renderNormalized(red, 0, 0, 0);
	}
	
	public void renderNormal() {
		BufferedImage red = Game.get().createLayer(bounds.width, bounds.height);
		Graphics g = red.getGraphics();
		g.setColor(Color.RED.darker());
		g.fillRect(0, 0, bounds.width, bounds.height);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.PLAIN, 16));
		g.drawString("Delete", 0, 20);
		renderNormalized(red, 0, 0, 0);
	}
	
}
