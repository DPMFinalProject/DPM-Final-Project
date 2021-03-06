/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	SensorManager.java
 *	Created On:	Mar 10, 2015
 */
package sensors.managers;
import static util.Utilities.pause;

/**
 * Manages the sensor to control thread access to the lejos sensor classes
 * @author Oleg
 */
public abstract class SensorManager extends Thread {
	protected static GridManager gridManager;
	protected static ObstacleDetection obstDetector;
	private boolean running = true;
	
	public final void run() {
		while(true) {
			if (running) {
				execute();
			}
			else {
				pause(100);
			}
		}
	}
	
	public abstract void execute();

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public boolean getRunning() {
		return running;
	}
}
