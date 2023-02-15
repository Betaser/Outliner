package outline;

import java.util.List;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import base.Game;

public class RenderOutline {

	public static void renderText(List<Line> lines) {
		for (Line l : lines) {
			int y = 0;
			boolean first = true;
			for (String line : (l.formatting + l.outline.contents).split("\n")) {
				if (first) {
					drawLineOfText(line, l.xPos, l.yPos + y * OutlineTab.LN_HEIGHT, OutlineTab.IMG_HEIGHT);
				} else {
					final String LINE = "                    " + line;
					drawLineOfText(LINE, l.xPos, l.yPos + y * OutlineTab.LN_HEIGHT, OutlineTab.IMG_HEIGHT);
				}
				first = false;
				y--;
			}
		}
	}
	
	private static void drawLineOfText(String text, int x, int y, int IMG_HEIGHT) {
		if (text.length() == 0)
			return;
		BufferedImage blankImage = Game.get().createLayer(text.length() * 100, IMG_HEIGHT);
		Graphics g = blankImage.getGraphics();
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString(text, 0, 20);
		Game.get().draw(blankImage, x, y, blankImage.getWidth(), blankImage.getHeight(), 0, Game.get().camera);
	}
	
}
