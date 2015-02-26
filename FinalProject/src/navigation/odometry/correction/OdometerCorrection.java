/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	OdometerCorrection.java
 *	Created On:	Feb 26, 2015
 */
package navigation.odometry.correction;

/**
 * Uses light sensors to correct the odometer's coordinates.
 * Should not have ties to any other classes except the odometer and the light sensors.
 * @author Oleg
 */
public abstract class OdometerCorrection implements Runnable {
	public abstract void run();
}
