package util;

public class Pause {
	
	public static void pause(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {}
	}
}