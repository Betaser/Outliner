package miscDataStructures;

public class Camera {
	
	public double zoom;
	public int xPos, yPos;
	
	public Camera(double zoom, int xPos, int yPos) {
		this.zoom = zoom;
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public String toString() {
		return "zoom" + zoom + "x (" + xPos + ", " + yPos + ")";
	}

}
