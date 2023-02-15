package miscDataStructures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import base.Game;
import outline.Outline;

public class PointSelection {
	
	public Rectangle bounds;
	public Outline outline;
	
	public PointSelection(Rectangle bounds, Outline outline) {
		this.bounds = bounds;
		this.outline = outline;
	}
	
	public void render() {
		BufferedImage lightRed = Game.get().createLayer(bounds.width, bounds.height);
		Graphics g = lightRed.getGraphics();
		g.setColor(new Color(200, 0, 0, 100));
		g.fillRect(0, 0, bounds.width, bounds.height);
		Game.get().draw(lightRed, bounds.x, bounds.y, bounds.width, bounds.height, 0, Game.get().camera);
	}

}
