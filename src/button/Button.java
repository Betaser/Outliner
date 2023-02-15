package button;

import java.util.ArrayList;
import java.util.List;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import base.Game;
import miscDataStructures.Camera;

public abstract class Button {
	
	public int selectorX, selectorY;
	public final Rectangle bounds;
	public boolean highlighted;
	public boolean press;
	public Runnable onPress;
	public Camera cam;
	public boolean justSpawned;
	
	public Button(Rectangle bounds, Runnable onPress, Camera cam) {
		this.bounds = bounds;
		this.onPress = onPress;
		this.cam = cam;
		justSpawned = true;
	}
	
	public void render() {
		renderNormal();
		if (highlighted)
			renderHighlighted();
	}
	
	public abstract void renderHighlighted();
	
	public abstract void renderNormal();
	
	protected void renderNormalized(BufferedImage image, double xOffset, double yOffset, double depth) {
		Game.get().draw(image, bounds.x + xOffset, bounds.y + yOffset, bounds.width, bounds.height, depth, cam);
	}
	
	public void calc() {
		Rectangle onScreenBounds = new Rectangle(bounds);
		onScreenBounds.x += cam.xPos;
		onScreenBounds.y -= cam.yPos;
		highlighted = onScreenBounds.contains(selectorX, selectorY);
	}
	
	public void tryRun() {
		if (highlighted && press && !justSpawned) {
			onPress.run();
			press = false;
		}
		justSpawned = false;
	}
	
	public void tick() {
		calc();
		tryRun();
	}
	
	public static class Group {
		
		public List<Button> buttons;
		
		public Group(Button... buttons) {
			this.buttons = new ArrayList<>(List.of(buttons));
		}
		
		public void tick() {
			List<Rectangle> btnBounds = new ArrayList<>();
			for (Button button : buttons) {
				button.calc();
				Rectangle onScreenBounds = new Rectangle(button.bounds);
				onScreenBounds.x += button.cam.xPos;
				onScreenBounds.y -= button.cam.yPos;
				if (button.highlighted) {
					for (Rectangle bounds : btnBounds) {
						if (bounds.intersects(onScreenBounds)) {
							button.highlighted = false;
						}
					}
				}
				button.tryRun();
				btnBounds.add(onScreenBounds);
			}
		}
		
		public void render() {
			buttons.forEach(Button::render);
		}
		
	}
	
}
