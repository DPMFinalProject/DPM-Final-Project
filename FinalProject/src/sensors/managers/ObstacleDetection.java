/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	ObstacleDetection.java
 *	Created On:	Mar 3, 2015
 */
package sensors.managers;

import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import sensors.FilteredUltrasonicSensor;
import sensors.filters.OutlierFilter;
import util.Direction;

/**
 * Runs in a separate thread, waiting until it detects an obstacle.
 * This obstacle can either be located left, right or in front of the robot.
 * @author Oleg
 */
public class ObstacleDetection extends SensorManager {
	private FilteredUltrasonicSensor leftSensor, rightSensor;
	private int usSensorOutlier = 255;
	
	private boolean leftObstacle = false, rightObstacle = false, frontObstacle = false;
	
	private ObstacleDetection() {
		leftSensor = new FilteredUltrasonicSensor(SensorPort.S3, new OutlierFilter(3, usSensorOutlier));
		rightSensor = new FilteredUltrasonicSensor(SensorPort.S2, new OutlierFilter(3, usSensorOutlier));
	}
	
	public static ObstacleDetection getObstacleDetection() {
		if (obstDetector == null) {
			obstDetector = new ObstacleDetection();
			obstDetector.start();
		}
		
		return obstDetector;
	}

	@Override
	public void execute() {
		// If sensor does not report an outlier, there is an obstacle nearby
		double leftValue, rightValue;
		
		leftValue = leftSensor.getFilteredData();
		rightValue = rightSensor.getFilteredData();
		
		leftObstacle = !(leftValue == usSensorOutlier);
		rightObstacle = !(rightValue == usSensorOutlier);
		
		frontObstacle = leftObstacle && rightObstacle;
		
		pause(20);		
	}
	
	/**
	 * Returns the direction in which an obstacle is currently located.
	 * @return	FWD, LEFT or RIGHT if an obstacle is detected, null otherwise.
	 */
	public Direction isObstaclePresent() {
		if (frontObstacle) return Direction.FWD;
		if (leftObstacle) return Direction.LEFT;
		if (rightObstacle) return Direction.RIGHT;
		return null;
	}

	public boolean isLeftObstacle() {
		return leftObstacle;
	}

	public boolean isRightObstacle() {
		return rightObstacle;
	}

	public boolean isFrontObstacle() {
		return frontObstacle;
	}	
}
