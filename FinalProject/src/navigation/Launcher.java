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
import navigation.odometry.Odometer;

/**
 * 	The Navigation class is responsible for the launching of the projectile.
 * @author Gregory Brookes
 */

public class Launcher {
	private Driver driver;
	private Odometer odo;
	private Navigation nav;
	
	
	public Launcher(Odometer odo, Driver driver, Navigation nav) {
		this.odo = odo;
		this.driver = driver;
		this.nav=nav;
	}
	
	/**
	 * Position the robot and shoots a projectile to the coordinates (x,y).
	 * 
	 * @param x
	 * @param y
	 */
	public void ShootTo(double x, double y) {
		nav.travelTo(findCoordinatesToTravelTo(x,y));

	}

	private double[] findCoordinatesToTravelTo(double x, double y) {
		double[] coordinates = new double [3];
	
		return coordinates;
	}
	
	
}
