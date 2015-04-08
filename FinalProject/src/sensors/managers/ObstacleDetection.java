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
import sensors.FilteredUltrasonicSensor;
import sensors.filters.OutlierFilter;
import util.Direction;
import static util.Utilities.pause;

/**
 * Runs in a separate thread, waiting until it detects an obstacle.
 * This obstacle can either be located left, right or in front of the robot.
 * @author Oleg
 */
public class ObstacleDetection extends SensorManager {
	private FilteredUltrasonicSensor leftSensor, rightSensor;
	private final int US_SENSOR_OUTLIER = 255;
	private final int OBSTACLE_THRESHOLD = 45;	// Has to be raised when speed is increased
	
	private final int DETECTION_PERIOD = 20;
	private final int PERP_ERROR = 20;
	
	// Used after first creating the filtered sensors to make sure the moving window becomes full.
	private final static int INITIAL_DELAY = 100;
	
	private boolean leftObstacle = false, rightObstacle = false, frontObstacle = false;
	private double leftDistance = 100, rightDistance = 100;
	
	private Object lock = new Object();
	
	private ObstacleDetection() {
		leftSensor = new FilteredUltrasonicSensor(SensorPort.S3, new OutlierFilter(10, US_SENSOR_OUTLIER));
		rightSensor = new FilteredUltrasonicSensor(SensorPort.S2, new OutlierFilter(10, US_SENSOR_OUTLIER));
	}
	
	public static synchronized ObstacleDetection getObstacleDetection() {
		if (obstDetector == null) {
			obstDetector = new ObstacleDetection();
			obstDetector.start();
			pause(INITIAL_DELAY);
		}
		
		obstDetector.setRunning(true);
		
		return obstDetector;
	}

	@Override
	public void execute() {
		// Distance values under OBSTACLE_THRESHOLD are considered to be obstacles
		synchronized(lock) {
			leftDistance = leftSensor.getFilteredData();
			rightDistance = rightSensor.getFilteredData();
		}
		
		leftObstacle = leftDistance < OBSTACLE_THRESHOLD;
		rightObstacle = rightDistance < OBSTACLE_THRESHOLD;
		
		frontObstacle = leftObstacle && rightObstacle;
		
		pause(DETECTION_PERIOD);		
	}
	
	/**
	 * return true if the sensors detect an obstacle in front of the robot. false otherwise.
	 * @param obstThresh
	 * @return
	 */
	public boolean isFrontObstacle(double obstThresh) {
		synchronized(lock) {
			leftDistance = leftSensor.getFilteredData();
			rightDistance = rightSensor.getFilteredData();
		}
		
		leftObstacle = leftDistance < obstThresh;
		rightObstacle = rightDistance < obstThresh;
		
		return leftObstacle && rightObstacle;
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
	
	public double leftDistance() {
		return leftDistance;
	}
	
	public boolean isRightObstacle() {
		return rightObstacle;
	}
	
	public double rightDistance() {
		return rightDistance;
	}
	
	public boolean isFrontObstacle() {
		return frontObstacle;
	}
	
	public double frontDistance() {
		return (leftDistance + rightDistance)/2;
	}
	
	public double sideDistance(Direction direction) {
		if (direction == Direction.RIGHT) {
			return rightDistance;
		} else {
			return leftDistance;
		}
	}
	
	public boolean perpendicularToWall() {
		return Math.abs(rightDistance() - leftDistance()) < PERP_ERROR; 
	}
	
	/**
	 * return the distance measured by the sensors in the desired direction.
	 * @param direction
	 * @return
	 */
	public double wallDistance(Direction direction) {
		switch(direction) {
		case RIGHT:
			return rightDistance();
		case LEFT:
			return leftDistance();
		case FWD:
			return frontDistance();
		default:
//			System.out.println("ERROR: no distance for Direction.BACK");
			return 0.0;
		}
	}
}
