/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Navigation.java
 *	Created On:	Feb 28, 2015
 */
package navigation;

import util.Direction;
import navigation.localization.Localization;
import navigation.odometry.Odometer;
import static util.Pause.pause;

/**
 * 	The Navigation class is responsible for odometer-adjusted movement.
 * @author Oleg
 */

public class Navigation {
	private Odometer odo;
	
	private final double ANGLE_ERROR = 10.0;
	private final double POS_ERROR= 2.0;
	
	public Navigation(Odometer odo) {
		this.odo = odo;
	}
	
	/**
	 * Moves the robot to a specific coordinate facing the supplied direction.
	 * @param x
	 * @param y
	 * @param theta
	 */
	public void travelTo(double x, double y, double theta) {
		//System.out.println("Current: " + odo.getX() + "," + odo.getY() + "," + odo.getTheta());
		//System.out.println("Target: " + x + "," + y + "," + theta);
		travelTo(x, y);
		turnTo(theta);
	}
	/**
	 * Moves the robot to a specific coordinate facing the supplied direction.
	 * @param coordinates
	 */
	
	public void travelTo(double[] coordinates) {
		travelTo(coordinates[0],coordinates[1],coordinates[2]);
	}
	
	
	/**
	 * Moves the robot to a particular coordinate point without caring for orientation.
	 * @param x
	 * @param y
	 */
	public void travelTo(double x, double y) {
		double xPos, xErr;
		double yPos, yErr;
		double targetAngle, distance;
		
		while (computeAbsError(odo.getX(), x) > POS_ERROR || computeAbsError(odo.getY(), y) > POS_ERROR) {
			xPos = odo.getX();
			yPos = odo.getY();
			
			xErr = computeError(xPos, x);
			yErr = computeError(yPos, y);
			
			targetAngle = Math.toDegrees(Math.atan2(yErr, xErr));
			
			System.out.println("Target: " + targetAngle);
			
			targetAngle = adjustRefFrame(targetAngle);
			
			turnTo(targetAngle);
			
			distance = Math.sqrt((xErr * xErr) + (yErr * yErr));
			Driver.move(distance);
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
				System.out.println("LEFT");
				Driver.turn(Direction.LEFT, dTheta);
			} else if (dTheta < 0) {
				System.out.println("RIGHT");
				Driver.turn(Direction.RIGHT, Math.abs(dTheta));
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
	
	private double computeAbsError(double current, double target) {
		return Math.abs(computeError(current, target));
	}
	private double computeError(double current, double target) {
		return target - current;
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
			} else {
				return 90 - angle;
			}
	}
}
