/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	USLocaizationDiagonal.java
 *	Created On:	Mar 24, 2015
 */
package navigation.localization;

import static util.Utilities.pause;
import lejos.nxt.Sound;
import sensors.managers.ObstacleDetection;
import util.Direction;
import util.Measurements;
import navigation.Driver;
import navigation.Navigation;
import navigation.odometry.Odometer;

/**
 * 	Less accurate but faster localization that just moves the robot into the first square.
 * 	Not yet completed
 * @author Oleg
 */
public class USLocalizationDiagonal extends Localization {

	private final ObstacleDetection obstacleDetection;
	
	private final double SENSOR_ORIENTATION = 45;
	private final double SENSOR_OFFSET = 10.5;
	private final double FRONT_OBSTACLE_THRESHOLD = 60;
	
	
	public USLocalizationDiagonal(Odometer odo, Navigation nav) {
		super(odo, nav);
		obstacleDetection = ObstacleDetection.getObstacleDetection();
	}

	/**
	 * 
	 * @see navigation.localization.Localization.java
	 */
	@Override
	public void doLocalization(double x, double y, double theta) {
		double xPos, yPos;
		
		if(!obstacleDetection.isFrontObstacle())
			faceWall();
		
		yPos = obtainDistanceToWall(Direction.LEFT, false);
		xPos = obtainDistanceToWall(Direction.RIGHT, false);
		
		odo.setX(xPos);
		odo.setY(yPos);
		odo.setTheta(theta - 90);
		
		nav.travelTo(x,  y, theta, false);
	}
	

	/**
	 * Measures the distance from a wall by pointing one of the ultrasonic sensors directly at it
	 * @param dir
	 * @param move
	 * @return
	 */
	private double obtainDistanceToWall(Direction dir, boolean move) {
		double coord;
		
		faceAwayFromWall(dir);
		Driver.turn(dir, SENSOR_ORIENTATION);
		coord = obstacleDetection.sideDistance(dir.opposite()) + SENSOR_OFFSET - Measurements.TILE;
		Driver.turn(dir.opposite(), SENSOR_ORIENTATION);
		
		return coord;
	}
	
	/**
	 * Moves the robot so it faces away from the wall
	 * @param sensorDirection
	 */
	private void faceAwayFromWall(Direction sensorDirection){
		Driver.turn(sensorDirection);
		pause(500);
		do{
			// Keep turning until one sensor doesn't see the wall anymore
			pause(20);
		} while(obstacleDetection.isFrontObstacle(FRONT_OBSTACLE_THRESHOLD));
		Driver.stop();
		perpendicularToWall(sensorDirection.opposite());
	}
	
	/**
	 * Moves the robot so it faces toward the wall.
	 * @see doLocalization()
	 */
	private void faceWall() {
		// Turn until facing a wall
		Direction turnDir = Direction.RIGHT;
		
		if (obstacleDetection.isLeftObstacle()) {
			turnDir = Direction.LEFT;
		}
		
		Driver.turn(turnDir);
		while(!obstacleDetection.isFrontObstacle(FRONT_OBSTACLE_THRESHOLD)) {
			pause(20);
		}
		Driver.stop();
		Driver.turn(turnDir, 40);
	}
	
	/**
	 *  Turns to make sure that the robot is facing the wall perpendicularly
	 */
	private void perpendicularToWall(Direction dir) {
		Driver.turn(dir);
		while(!obstacleDetection.perpendicularToWall()) {
			pause(20);
		}
		System.out.println("Stopped");
		Driver.stop();
	}
}
