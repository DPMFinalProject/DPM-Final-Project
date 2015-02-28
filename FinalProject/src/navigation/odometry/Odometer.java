/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Odometer.java
 *	Created On:	Feb 26, 2015
 */
package navigation.odometry;

import navigation.odometry.correction.OdometryCorrection;

/**
 * Keeps track of the position and orientation of the robot.
 * @author Oleg
 */
public class Odometer implements Runnable {
	
	// robot position
	private double x, y, theta;
	
	// odometer update period, in ms
	private static final long ODOMETER_PERIOD = 15;
	
	// Correction class that will be used
	private OdometryCorrection correction;

	/**
	 * 	Main odometer loop.
	 * @param a
	 */
	@Override
	public void run() {
		throw new UnsupportedOperationException();
	}

	// Getters and Setters
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}

	public double getTheta() {
		return theta;
	}
	public void setTheta(double theta) {
		this.theta = theta;
	}
}