/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Navigation.java
 *	Created On:	Feb 28, 2015
 */
package navigation;

import navigation.localization.Localization;
import navigation.odometry.Odometer;

/**
 * 	The Navigation class is responsible for odometer-adjusted movement.
 * @author Oleg
 */
public class Navigation {
	private Driver driver;
	private Localization localization;
	private Odometer odo;
	
	private final double ANGLE_ERROR = 2.0;
	private final double POS_ERROR= 2.0;
	/**
	 * Moves the robot to a specific coordinate facing the supplied direction.
	 * @param x
	 * @param y
	 * @param theta
	 */
	public void travelTo(double x, double y, double theta) {
		travelTo(x, y);
		turnTo(theta);
	}
	
	/**
	 * Moves the robot to a particular coordinate point without caring for orientation.
	 * @param x
	 * @param y
	 */
	public void travelTo(double x, double y) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Turns the robot to the desired angle.
	 * @param theta
	 */
	public void turnTo(double theta) {
		throw new UnsupportedOperationException();
	}
	
	// Returns an angle in the range [-180, 180] negative angle means turn right
	/*private double shortestAngle(double currentAngle, double targetAngle) {
		double rawDistance = targetAngle - currentAngle;
		
	}*/
}
