/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	USLocalization.java
 *	Created On:	Feb 26, 2015
 */
package navigation.localization;

import sensors.filters.DifferentialFilter;
import sensors.managers.ObstacleDetection;
import util.Direction;
import util.Measurements;
import navigation.Driver;
import navigation.Navigation;
import navigation.odometry.Odometer;
import static util.Utilities.pause;

/**
 * Performs localization using the ultrasonic sensor.
 * @author Gregory Brookes, Oleg Zhilin
 */

public class USLocalization extends Localization {
	
	private ObstacleDetection obstacleDetection;
	
	private final double SENSOR_VIEW_ANGLE = 30;
	private final double SENSOR_OFFSET = 10;//9.3; 
	
	public USLocalization(Odometer odo, Navigation nav) {
		super(odo, nav);
		
		obstacleDetection = ObstacleDetection.getObstacleDetection();
	}
	
	public void doLocalization(double x, double y, double theta) {
		if(!obstacleDetection.isFrontObstacle())
			faceWall();
		/* 
		 * Do multiple iterations of adjustments of the X and Y positions,
		 * the robot will converge onto (0, 0) provided SENSOR_OFFSET is calibrated
		 * correctly.
		 */
		for (int i = 0; i < 2; i++) {
			adjustYPosition(true);
			adjustXPosition(true);
		}
		
		/*
		 * 	It will have an approximate final orientation of 0 degrees,
		 * 	which should be accurate enough for the LS localization to adjust.
		 */
		Driver.turn(Direction.RIGHT, 60);
		
		Driver.move(-7);
		odo.setX(0);
		odo.setY(0);
		odo.setTheta(0);
	}
	/**
	 * @see navigation.localization.Localization#doLocalization()
	 */
	@Override
	public void doLocalization() {
		doLocalization(0, 0, 0);
	}
	
	private void adjustXPosition(boolean move) {
		double xPosition;
		
		obstacleDetection.setRunning(true);
		faceAwayFromWall(Direction.RIGHT);
		Driver.turn(Direction.RIGHT, SENSOR_VIEW_ANGLE);
		xPosition = obstacleDetection.leftDistance() + SENSOR_OFFSET - Measurements.TILE;
		Driver.turn(Direction.LEFT, SENSOR_VIEW_ANGLE + 10);
		
		System.out.println("XPos: " + xPosition);
		
		if (move)
			Driver.move(xPosition);
	}
	
	private void adjustYPosition(boolean move) {
		double yPosition;
		
		faceAwayFromWall(Direction.LEFT);
		Driver.turn(Direction.LEFT, SENSOR_VIEW_ANGLE);
		yPosition = obstacleDetection.rightDistance() + SENSOR_OFFSET - Measurements.TILE;
		Driver.turn(Direction.RIGHT, SENSOR_VIEW_ANGLE + 10);
		
		System.out.println("YPos: " + yPosition);
		
		if (move)
			Driver.move(yPosition);
	}
	
	// turn until facing away from wall;
	private void faceAwayFromWall(Direction sensorDirection){
		double wallDistance;
		DifferentialFilter dFilter = new DifferentialFilter(2);
		
		Driver.turn(sensorDirection);
		pause(1000);
		do{
			// Use edge triggering by applying the differential filter.
			if (sensorDirection == Direction.RIGHT) { 
				wallDistance = obstacleDetection.rightDistance();
			} else {
				wallDistance = obstacleDetection.leftDistance();
			}
			
			wallDistance = dFilter.filter(wallDistance);
			pause(20);
		} while(wallDistance < 50 || wallDistance > 245);
		Driver.stop();
	}
	
	private void faceWall() {
		// Turn until facing a wall
		obstacleDetection.setRunning(true);
		Driver.turn(Direction.RIGHT);
		while(!obstacleDetection.isFrontObstacle()) {
			pause(20);
		}
		Driver.stop();
		Driver.turn(Direction.RIGHT, 90);
	}
}
