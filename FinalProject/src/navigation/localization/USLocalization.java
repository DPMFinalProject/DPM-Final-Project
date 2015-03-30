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
	
	protected ObstacleDetection obstacleDetection;
	
	protected final double SENSOR_VIEW_ANGLE = 30;
	protected final double SENSOR_OFFSET = 10;//9.3;
	protected final double FRONT_OBSTACLE_THRESHOLD = 46;
	
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
			obtainYPosition(true);
			obtainXPosition(true);
		}
		
		/*
		 * 	It will have an approximate final orientation of 0 degrees,
		 * 	which should be accurate enough for the LS localization to adjust.
		 */
		Driver.turn(Direction.RIGHT, 60);
		
		Driver.move(-10);
		odo.setX(x);
		odo.setY(y);
		odo.setTheta(theta);
	}
	
	protected double obtainXPosition(boolean move) {
		double xPosition;
		
		faceAwayFromWall(Direction.RIGHT);
		Driver.turn(Direction.RIGHT, SENSOR_VIEW_ANGLE);
		xPosition = obstacleDetection.leftDistance() + SENSOR_OFFSET - Measurements.TILE;
		Driver.turn(Direction.LEFT, SENSOR_VIEW_ANGLE + 10);
		
//		System.out.println("XPos: " + xPosition);
		
		if (move)
			Driver.move(xPosition);
		
		return xPosition;
	}
	
	protected double obtainYPosition(boolean move) {
		double yPosition;
		
		faceAwayFromWall(Direction.LEFT);
		Driver.turn(Direction.LEFT, SENSOR_VIEW_ANGLE);
		yPosition = obstacleDetection.rightDistance() + SENSOR_OFFSET - Measurements.TILE;
		Driver.turn(Direction.RIGHT, SENSOR_VIEW_ANGLE + 10);
		
//		System.out.println("YPos: " + yPosition);
		
		if (move)
			Driver.move(yPosition);
		
		return yPosition;
	}
	
	// turn until facing away from wall;
	private void faceAwayFromWall(Direction sensorDirection){
		double wallDistance;
		DifferentialFilter dFilter = new DifferentialFilter(2);
		
		Driver.turn(sensorDirection);
		pause(500);
		do{
			// Use edge triggering by applying the differential filter.
			if (sensorDirection == Direction.RIGHT) { 
				wallDistance = obstacleDetection.rightDistance();
			} else {
				wallDistance = obstacleDetection.leftDistance();
			}
			
			wallDistance = dFilter.filter(wallDistance);
			pause(20);
		} while(wallDistance < 40 || wallDistance > 245);
		Driver.stop();
	}
	
	protected void faceWall() {
		// Turn until facing a wall

		Driver.turn(Direction.RIGHT);
		while(!obstacleDetection.isFrontObstacle(FRONT_OBSTACLE_THRESHOLD)) {
			pause(20);
		}
		Driver.stop();
		Driver.turn(Direction.RIGHT, 90);
	}
}
