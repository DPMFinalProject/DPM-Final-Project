/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Navigation.java
 *	Created On:	Feb 28, 2015
 */
package navigation;

import lejos.nxt.Sound;
import sensors.managers.ObstacleDetection;
import util.Direction;
import util.Measurements;
import navigation.avoidance.BangBangAvoider;
import navigation.avoidance.DolphinAvoider;
import navigation.avoidance.ObstacleAvoidance;
import navigation.odometry.Odometer;
import static util.Utilities.pause;

/**
 * 	The Navigation class is responsible for odometer-adjusted movement that is in accordance to
 * 	both the reference frame specified by the odometer and the tile-based reference frame specified 
 * 	in the requirements.
 * @author Oleg
 */

public class Navigation {
	private final Odometer odo;
	private ObstacleAvoidance avoider;
	
	private final double ANGLE_ERROR = 10.0;
	private final double POS_ERROR = 1.0;
	private final double BACKWARDS_THRESHOLD = 15.0;
	
	// The following variables describe the range where the robot is considered to be far from a wall.
	// The units are cm-based coordinates
	private final double DETECTION_AREA_MIN = 10;
	private final double DETECTION_AREA_MAX = 6 * Measurements.TILE - DETECTION_AREA_MIN;
	
	public Navigation(Odometer odo) {
		this.odo = odo;
	}
	
	/**
	 * Moves the robot to a specific coordinate, with or without avoidance.
	 * 
	 * @param x	The target x Coordinate in units of tiles.
	 * @param y The target y Coordinate in units of tiles.
	 * @param avoiding	Boolean flag to enable or disable obstacle avoidance
	 */
	public void travelToInTiles(double x, double y, boolean avoiding) {
		travelTo(x * Measurements.TILE, y * Measurements.TILE, avoiding);
	}
	
	/**
	 * Moves the robot to a specific coordinate facing the supplied direction, with or without avoidance.
	 * @param x	The target x Coordinate in units of tiles.
	 * @param y The target y Coordinate in units of tiles.
	 * @param theta	The angle the robot will face after movement, in degrees.
	 * @param avoiding	Boolean flag to enable or disable obstacle avoidance
	 */
	public void travelToInTiles(double x, double y, double theta, boolean avoiding) {
		travelTo(x * Measurements.TILE, y * Measurements.TILE, avoiding);
		Sound.twoBeeps();
		turnTo(theta);
	}
	
	/**
	 * Moves the robot to a specific coordinate facing a desired direction (if provided).
	 * @param coordinates an array of 2 or 3 elements representing the (x, y, theta) coordinates. The theta value is optional
	 */
//	public void travelToInTiles(double[] coordinates, boolean avoiding) {
//		if (coordinates.length == 2) {
//			travelTo(coordinates[0] * Measurements.TILE, coordinates[1] * Measurements.TILE, avoiding);
//		} else {
//			travelTo(coordinates[0] * Measurements.TILE,coordinates[1] * Measurements.TILE,coordinates[2], avoiding);
//		}
//	}
	
	/**
	 * Moves the robot to a specific coordinate, facing the provided direction, with or without avoidance.
	 * @param x	The target x Coordinate in units of tiles.
	 * @param y The target y Coordinate in units of tiles.
	 * @param theta	The angle the robot will face after movement, in degrees.
	 * @param avoiding	Boolean flag to enable or disable obstacle avoidance
	 */
	public void travelTo(double x, double y, double theta, boolean avoiding) {
		travelTo(x, y, avoiding);
		turnTo(theta);
	}
	
	/**
	 * Moves the robot to a specific coordinate facing a desired direction (if provided).
	 * @param coordinates an array of 2 or 3 elements representing the (x, y, theta) coordinates. The theta value is optional
	 */
//	public void travelTo(double[] coordinates, boolean avoiding) {
//		if (coordinates.length == 2) {
//			travelTo(coordinates[0], coordinates[1], avoiding);
//		} else {
//			travelTo(coordinates[0],coordinates[1],coordinates[2], avoiding);
//		}
//	}
	
	
	/**
	 * Moves the robot to a specific coordinate, with or without avoidance.
	 * @param x	The target x coordinate in cm.
	 * @param y The target y coordinate in cm.
	 * @param avoiding	Boolean flag to enable or disable obstacle avoidance
	 */
	public void travelTo(double x, double y, boolean avoiding) {
		double xPos, xErr;
		double yPos, yErr;
		double targetAngle, distance;
		
		while (euclideanDistance(odo.getX(), odo.getY(), x, y) > POS_ERROR) {
			// Get the robot's current position
			xPos = odo.getX();
			yPos = odo.getY();
			
			// Figure out the x and y displacement required to reach target
			xErr = computeError(xPos, x);
			yErr = computeError(yPos, y);
			
			targetAngle = Math.toDegrees(Math.atan2(yErr, xErr));
			targetAngle = adjustRefFrame(targetAngle);	// Odometer uses a slightly different reference frame
			
			distance = Math.sqrt((xErr * xErr) + (yErr * yErr));
			
			// Check to see if the robot went too far, if so it will back up
			if (distance < BACKWARDS_THRESHOLD && targetBehindRobot(targetAngle)) {
				if (targetAngle < 0) {
					turnTo(targetAngle + 180);
				}
				else {
					turnTo(targetAngle - 180);
				}
				
				Driver.move(-distance, avoiding);
			} else { // If the target destination is ahead, proceed with normal movement.
				turnTo(targetAngle);
				Driver.move(distance, avoiding);
			}
			
			if (avoiding) {
				doAvoidance(x, y);
			}
		}
	}
	
	/**
	 * Loops during navigation and triggers the avoidance routine if an obstacle is detected.
	 * The destination coordinates are passed in order to disable detection when the robot is near its destination.s 
	 * @param x	The target x coordinate in cm. 
	 * @param y The target y coordinate in cm.
	 */
	private void doAvoidance(double x, double y) {
		ObstacleDetection detection = ObstacleDetection.getObstacleDetection();
		while(Driver.isMoving()) {
			// Obstacle detection is used if the robot is far from its destination and not near a wall.
			//if (euclideanDistance(odo.getX(), odo.getY(), x, y) > 2 * Measurements.TILE && !nearWall()) {
				if (detection.isLeftObstacle()) {
					avoider = new BangBangAvoider(Direction.LEFT, odo);
					avoider.avoid();
					avoider = null;
				} else if (detection.isRightObstacle()) {
					avoider = new BangBangAvoider(Direction.RIGHT, odo);
					avoider.avoid();
					avoider = null;
				}
			//}
			pause(100);
		}
	}
	
	/**
	 * Turns the robot to the desired angle in the reference frame of the odometer.
	 * @param theta
	 */
	public void turnTo(double theta) {
		double dTheta;
		
		do {
			dTheta = shortestAngle(odo.getTheta(), theta);
			
			if (dTheta > 0) {
				Driver.turn(Direction.RIGHT, dTheta);
			} else if (dTheta < 0) {
				Driver.turn(Direction.LEFT, Math.abs(dTheta));
			}
			
		} while (shortestAngle(odo.getTheta(), theta) > ANGLE_ERROR);
	}
	
	// Returns an angle in the range [-180, 180] negative angle means turn left
	private double shortestAngle(double currentAngle, double targetAngle) {
		double rawDeltaAngle = computeError(currentAngle, targetAngle);
		
		if (Math.abs(rawDeltaAngle) > 180) {
			if (rawDeltaAngle > 0) {
				return rawDeltaAngle - 360;
			} else if (rawDeltaAngle < 0) {
				return rawDeltaAngle + 360;	
			} else {
				return 0;
			}
		} else {
			return rawDeltaAngle;
		}
	}
	
	private double computeError(double current, double target) {
		return target - current;
	}
	
	private double euclideanDistance(double currentX, double currentY, double targetX, double targetY) {
		double dx = currentX - targetX;
		double dy = currentY - targetY;
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	
	/**
	 * Adjust an angle from a polar coordinate system to the reference frame of the odometer.
	 * @param angle
	 * @return
	 */
	public double adjustRefFrame(double angle) {
		//Convert the destination angle to the same reference frame as the odometer angle
		if (angle > 0){
			if (angle < 90) {
				return 90 - angle;
			}
			else {
				return 450 - angle;
			}
		} 
		else {
			return 90 - angle;
		}
	}
	
	private boolean targetBehindRobot(double targetAngle) {
		return Math.abs(shortestAngle(odo.getTheta(), targetAngle)) > 90;
	}
	
	private boolean nearWall() {
		double xPos = odo.getX();
		double yPos = odo.getY();
		
		return xPos < DETECTION_AREA_MIN || yPos < DETECTION_AREA_MIN || 
				xPos > DETECTION_AREA_MAX || yPos > DETECTION_AREA_MAX;
	}

		
	public void travelTo(double[] coordinates, boolean avoiding) {
		if (coordinates.length == 2) {
			travelTo(coordinates[0], coordinates[1], avoiding);
		} else {
			travelTo(coordinates[0],coordinates[1],coordinates[2], avoiding);
		}
	}
}
