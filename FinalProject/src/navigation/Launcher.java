/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Launcher.java
 *	Created On:	Feb 28, 2015
 */
package navigation;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import util.Measurements;
import navigation.odometry.Odometer;
import static util.Utilities.pause;

/**
 * 	The Navigation class is responsible for the launching of the projectile.
 * @author Gregory Brookes
 */

public class Launcher {
	private Odometer odo;
	private Navigation nav;
	private final double[] target = new double[2];
	private final double[] range = new double[2];

	private final double[] shootingArea = {Measurements.TILE*9,Measurements.TILE*12};
	private final static NXTRegulatedMotor Shooter = Motor.C;
	
	public Launcher(Odometer odo, Navigation nav) {
		this.odo = odo;
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
		shoot(3);

	}

	private void shoot(int launchs) {
		Shooter.setSpeed(100);
		for(int i=0; i<launchs; i++){
			Shooter.rotate(360);
			pause(1000);
		}
		
	}

	private double[] findCoordinatesToTravelTo(double x, double y) {
		double[] coordinates = new double [3];
		findXY(x,y,coordinates);
		findTheta(x,y,coordinates);
		return coordinates;
	}

	private void findTheta(double x, double y, double[] coordinates) {
		coordinates[2] = (Math.atan((x-odo.getX())/(y-odo.getY()))-getRangeTheta()+360)%360;
	}

	private void findXY(double x, double y, double[] coordinates) {
		while(! (shootingArea[0]<x && x<shootingArea[1] && shootingArea[0]<y && y<shootingArea[1]) ){
			x++;
			y = target[1] - Math.sqrt( Math.pow(rangeNormal(), 2) - Math.pow( (target[2]-x), 2));
		}
		
		coordinates[0] = x;
		coordinates[1] = y;
		
	}
	private double getRangeTheta(){
		return Math.atan2(range[0],range[1]);
	}

	private double rangeNormal(){
		return Math.sqrt( Math.pow(range[0], 2)+Math.pow(range[1], 2));
	}
}
