/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	OdometryCorrection.java
 *	Created On:	Feb 26, 2015
 */
package navigation.odometry.correction;

import navigation.odometry.Odometer;

/**
 * Uses light sensors to correct the odometer's coordinates.
 * Should not have ties to any other classes except the odometer and the light sensors.
 * @author Gregory Brookes
 */
public abstract class OdometryCorrection implements Runnable {
	protected Odometer odo;
	
	public OdometryCorrection(Odometer odo) {
		this.odo = odo;
	}
	
	public abstract void run();	
}
