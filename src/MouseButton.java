package button;

import java.awt.Rectangle;

import base.Game;
import miscDataStructures.Camera;

public abstract class MouseButton extends Button {
	
	public MouseButton(Rectangle bounds, Runnable onClick, Camera cam) {
		super(bounds, onClick, cam);
	}
	
	public void calc() {
		selectorX = Game.get().input.mouseX;
		selectorY = Game.get().input.mouseY;
		press = Game.get().input.lMouse.unclick();
		super.calc();
	}

}
