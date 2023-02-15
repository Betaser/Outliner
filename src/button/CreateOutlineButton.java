package button;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import base.Game;

public class CreateOutlineButton extends MouseButton {
	
	public static final int WIDTH = 80, HEIGHT = 30;
	
	private CreateOutlineButton(Rectangle bounds, Runnable createButton) {
		super(bounds, createButton, Game.get().DEF_CAMERA);
	}
	
	public static CreateOutlineButton createCreateOutlineButton(Runnable createButton) {
		final int X = Game.get().getSceneWidth() - WIDTH;
		final int Y = Game.get().getSceneHeight() - HEIGHT;
		return new CreateOutlineButton(new Rectangle(X, Y, WIDTH, HEIGHT), createButton);
	}
	
	public void renderHighlighted() {
		BufferedImage cyan = Game.get().createLayer(bounds.width, bounds.height);
		Graphics g = cyan.getGraphics();
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, bounds.width, bounds.height);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.PLAIN, 16));
		g.drawString("Create", 0, 20);
		renderNormalized(cyan, 0, 0, 0);
	}
	
	public void renderNormal() {
		BufferedImage blue = Game.get().createLayer(bounds.width, bounds.height);
		Graphics g = blue.getGraphics();
		g.setColor(Color.CYAN.darker());
		g.fillRect(0, 0, bounds.width, bounds.height);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.PLAIN, 16));
		g.drawString("Create", 0, 20);
		renderNormalized(blue, 0, 0, 0);
	}

}
