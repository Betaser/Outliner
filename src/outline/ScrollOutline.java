package outline;

import base.Game;

public class ScrollOutline {
	
	public float scrollPos;
	final float SCROLL_SPEED = 20;
	
	public void scrollToTop(Outline outline) {
		scrollPos = (outline.toString().chars().filter(c -> c == '\n').count() + 1) * OutlineTab.LN_HEIGHT - OutlineTab.TEXTFIELD_HEIGHT;
		capScroll(outline);
	}
	
	public void scroll(Outline outline) {
		scrollPos += -Game.get().input.scroll * SCROLL_SPEED;
		capScroll(outline);
	}
	
	private void capScroll(Outline outline) {
		final float MAX_SCROLL = (outline.toString().chars().filter(c -> c == '\n').count() + 1) * OutlineTab.LN_HEIGHT - OutlineTab.TEXTFIELD_HEIGHT;
		if (MAX_SCROLL > 0) {
			scrollPos = Math.min(scrollPos, MAX_SCROLL);
			scrollPos = Math.max(scrollPos, 0);
		} else {
			scrollPos = Math.min(scrollPos, 0);
			scrollPos = Math.max(scrollPos, MAX_SCROLL);
		}
		Game.get().camera.yPos = ((int) scrollPos / OutlineTab.LN_HEIGHT) * OutlineTab.LN_HEIGHT;
	}

}
