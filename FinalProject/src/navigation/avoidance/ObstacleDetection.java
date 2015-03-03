/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	ObstacleDetection.java
 *	Created On:	Mar 3, 2015
 */
package navigation.avoidance;

import lejos.nxt.SensorPort;
import sensors.FilteredUltrasonicSensor;
import sensors.filters.OutlierFilter;

/**
 * Runs in a separate thread, waiting until it detects an obstacle.
 * This obstacle can either be located left, right or in front of the robot.
 * @author Oleg
 */
public class ObstacleDetection implements Runnable {
	private boolean running = false;
	
	private FilteredUltrasonicSensor leftSensor, rightSensor;
	private int usSensorOutlier = 255;
	
	private boolean leftObstacle = false, rightObstacle = false, frontObstacle = false;
	
	public ObstacleDetection() {
		leftSensor = new FilteredUltrasonicSensor(SensorPort.S1, new OutlierFilter(3, usSensorOutlier));
		rightSensor = new FilteredUltrasonicSensor(SensorPort.S2, new OutlierFilter(3, usSensorOutlier));
	}

	@Override
	public void run() {
		running = true;
		
		while (running) {
			// If sensor does not report an obstacle, there is an obstacle nearby
			leftObstacle = !(leftSensor.getFilteredData() == usSensorOutlier);
			rightObstacle = !(rightSensor.getFilteredData() == usSensorOutlier);
			
			frontObstacle = leftObstacle && rightObstacle;
			
			pause(20);
		}
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	private void pause(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
