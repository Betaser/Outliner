package miscDataStructures;

public class Timer {
	
	long setTime;
	long length;
	
	public Timer(long length) {
		this.length = length;
		set();
	}
	
	public void set() {
		setTime = System.currentTimeMillis();
	}
	
	public boolean isDone() {
		return System.currentTimeMillis() - setTime > length;
	}
	
	public String toString() {
		return "millis: " + (System.currentTimeMillis() - setTime) + "/" + length;
	}
	
}
