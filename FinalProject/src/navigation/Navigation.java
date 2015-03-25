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
import navigation.avoidance.BangBangAvoider;
import navigation.avoidance.ObstacleAvoidance;
import navigation.odometry.Odometer;
import static util.Utilities.pause;

/**
 * 	The Navigation class is responsible for odometer-adjusted movement.
 * @author Oleg
 */

public class Navigation {
	private Odometer odo;
	
	private final double ANGLE_ERROR = 10.0;
	private final double POS_ERROR= 1.0;
	private final double BACKWARDS_THRESHOLD = 15.0;
	
	public Navigation(Odometer odo) {
		this.odo = odo;
	}
	
	/**
	 * Moves the robot to a specific coordinate facing the supplied direction.
	 * @param x
	 * @param y
	 * @param theta
	 */
	public void travelTo(double x, double y, double theta, boolean avoiding) {
		//System.out.println("Current: " + odo.getX() + "," + odo.getY() + "," + odo.getTheta());
		//System.out.println("Target: " + x + "," + y + "," + theta);
		travelTo(x, y, avoiding);
		turnTo(theta);
	}
	
	/**
	 * Moves the robot to a specific coordinate facing a desired direction (if provided).
	 * @param coordinates an array of 2 or 3 elements representing the (x, y, theta) coordinates. The theta value is optional
	 */
	public void travelTo(double[] coordinates, boolean avoiding) {
		if (coordinates.length == 2) {
			travelTo(coordinates[0], coordinates[1], avoiding);
		} else {
			travelTo(coordinates[0],coordinates[1],coordinates[2], avoiding);
		}
	}
	
	
	/**
	 * Moves the robot to a particular coordinate point without caring for orientation.
	 * @param x
	 * @param y
	 */
	public void travelTo(double x, double y, boolean avoiding) {
		double xPos, xErr;
		double yPos, yErr;
		double targetAngle, distance;
		
		while (euclideanDistance(odo.getX(), odo.getY(), x, y) > POS_ERROR) {
			xPos = odo.getX();
			yPos = odo.getY();
			
			xErr = computeError(xPos, x);
			yErr = computeError(yPos, y);
			
			targetAngle = Math.toDegrees(Math.atan2(yErr, xErr));
			
			distance = Math.sqrt((xErr * xErr) + (yErr * yErr));
			
			//System.out.println("Target: " + targetAngle);
			
			targetAngle = adjustRefFrame(targetAngle);
			
			if ((distance < BACKWARDS_THRESHOLD) && targetBehindRobot(targetAngle)) {
				//move backwards towards target
				if (targetAngle<0) {
					turnTo(targetAngle + 180);
				}
				else {
					turnTo(targetAngle - 180);
				}
				
				Driver.move(-distance, avoiding);
			}
			else {
				turnTo(targetAngle);
				
				Driver.move(distance, avoiding);
			}
			
			if (avoiding) {
				doAvoidance();
			}
		
			Sound.beep();
		}
	}
	
	/**
	 * runs avoidance loop while robot is moving
	 */
	private void doAvoidance() {
		ObstacleAvoidance avoidance;
		ObstacleDetection detection = ObstacleDetection.getObstacleDetection();
		
		while(Driver.isMoving()) {
			detection.setRunning(true);
			if (detection.isLeftObstacle()) {
				avoidance = new BangBangAvoider(Direction.LEFT, odo);
				avoidance.avoid();
			} else if (detection.isRightObstacle()) {
				avoidance = new BangBangAvoider(Direction.RIGHT, odo);
				avoidance.avoid();
			}
			avoidance = null;
			pause(20);
		}
	}
	
	/**
	 * Turns the robot to the desired absolute angle.
	 * @param theta
	 */
	public void turnTo(double theta) {
		double dTheta;
		
		do {
			dTheta = shortestAngle(odo.getTheta(), theta);
			
			if (dTheta > 0) {
				//System.out.println("LEFT");
				Driver.turn(Direction.RIGHT, dTheta);
			} else if (dTheta < 0) {
				//System.out.println("RIGHT");
				Driver.turn(Direction.LEFT, Math.abs(dTheta));
			} else { // Should never happen
				System.out.println("Robot trying to turn by 0 degrees for some reason");
			}
			
			pause(15);
			
		} while (shortestAngle(odo.getTheta(), theta) > ANGLE_ERROR);
	}
	
	// Returns an angle in the range [-180, 180] negative angle means turn left
	private double shortestAngle(double currentAngle, double targetAngle) {
		double rawDeltaAngle = targetAngle - currentAngle;
		
		if(Math.abs(rawDeltaAngle) > 180){
			if(rawDeltaAngle > 0){
				return rawDeltaAngle-360;
			}else if(rawDeltaAngle < 0 ){
				return 360 + rawDeltaAngle;	
			} else {
				return 0;
			}
		} else {
			return rawDeltaAngle;
		}
	}
	
//	private double computeAbsError(double current, double target) {
//		return Math.abs(computeError(current, target));
//	}
	
	private double computeError(double current, double target) {
		return target - current;
	}
	
	private double euclideanDistance(double currentX, double currentY, double targetX, double targetY) {
		return Math.sqrt(Math.pow(currentX - targetX, 2) + Math.pow(currentY - targetY, 2));
	}
	
	private double adjustRefFrame(double angle) {
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
		if (Math.abs(shortestAngle(odo.getTheta(), targetAngle)) > 90) {
			return true;
		}
		return false;
	}
}