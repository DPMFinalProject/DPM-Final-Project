/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	SensorManager.java
 *	Created On:	Mar 10, 2015
 */
package sensors.managers;

/**
 * Manages the sensor to control thread access to the lejos sensor classes
 * @author Oleg
 */
public abstract class SensorManager extends Thread {
	protected static GridManager gridManager;
	private static boolean running = true;
	
	public final void run() {
		while(true) {
			if (running) {
				execute();
			}
		}
	}
	
	public abstract void execute();
	//public abstract SensorManager getSensorManager();

	public void setRunning(boolean run) {
		running = run;
	}
	
	public boolean getRunning() {
		return running;
	}
	
	protected void pause(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
