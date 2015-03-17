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
import util.Measurements;
import navigation.odometry.Odometer;

/**
 * 	The Navigation class is responsible for the launching of the projectile.
 * @author Gregory Brookes
 */

public class Launcher {
	private Driver driver;
	private Odometer odo;
	private Navigation nav;
	private final double[] target = new double[2];
	private final double[] range = {Measurements.TILE*9,Measurements.TILE*12};
	
	
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
		
//		#############################################################
//					TODO: make it shoot 3x 
//		#############################################################

	}

	private double[] findCoordinatesToTravelTo(double x, double y) {
		double[] coordinates = new double [3];
		findXY(x,y,coordinates);
		findTheta(x,y,coordinates);
		return coordinates;
	}

	private void findTheta(double x, double y, double[] coordinates) {
//		#############################################################
//				TODO: Find Heading 
//		#############################################################
		
	}

	private void findXY(double x, double y, double[] coordinates) {
		while(! (range[0]<x && x<range[1] && range[0]<y && y<range[1]) ){
			x++;
			y = target[1] - Math.sqrt( Math.pow(rangeNormal(), 2) - Math.pow( (target[2]-x), 2));
		}
		
		coordinates[0] = x;
		coordinates[1] = y;
		
	}
	
	private double rangeNormal(){
		return Math.sqrt( Math.pow(range[0], 2)+Math.pow(range[1], 2));
	}
	
}
