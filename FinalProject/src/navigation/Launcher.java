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
import lejos.nxt.Sound;
import util.Direction;
import util.Measurements;
import navigation.odometry.Odometer;
import static util.Utilities.pause;

/**
 * 	The Navigation class is responsible for the launching of the projectile.
 * @author Gregory Brookes
 */

public class Launcher {
	//###############################################
	//   YOU WANT TO CHANGE THOSE VALUES:
	//range: (offset, range)
	private final double[] range = {0,160};
	
	
	private Odometer odo;
	private Navigation nav;
	private final int LAUNCH_SPEED = 300;
	private double[] flexibleRange;
	private double minShootingArea = Measurements.TILE*9;
	private double maxShootingArea = Measurements.TILE*12;
	
	private final static NXTRegulatedMotor shooter = Motor.C;
	
	public Launcher(Odometer odo, Navigation nav, double minShootingArea, double maxShootingArea){
		this.odo = odo;
		this.nav = nav;
		this.minShootingArea = minShootingArea *Measurements.TILE;
		this.maxShootingArea = maxShootingArea*Measurements.TILE;
	}
	
	public Launcher(Odometer odo, Navigation nav) {
		this.odo = odo;
		this.nav = nav;
	}
	
	/**
	 * Position the robot and shoots some projectiles to the coordinates (targetX, targetY).
	 * 
	 * @param targetX
	 * @param targetY
	 * @param projectiles
	 */
	public void shootToInTiles(double targetX, double targetY, int projectiles) {
		shootTo(targetX * Measurements.TILE, targetY * Measurements.TILE, projectiles);
	}
	
	private void shootTo(double targetX, double targetY, int projectiles) {
		double[] launchingCoordinates = findLaunchingCoordinates(targetX,targetY);
		nav.travelTo(launchingCoordinates[0], launchingCoordinates[1], false);	
		Sound.twoBeeps();
		shoot(projectiles);
	}

	private void shoot(int launchs) {
		Driver.turn(Direction.LEFT, getRangeTheta());		//position himself to compensate for the launch offset
		shooter.setSpeed(LAUNCH_SPEED);
		for(int i=0; i < launchs; i++){
			shooter.rotate(360);
			pause(1000);
		}
	}

	private double[] findLaunchingCoordinates(double targetX, double targetY) {
		double[] coordinates = new double [3];
		flexibleRange=range;										//will enable the robot to try to shoot at the target even if this one is out of range
		while(! findXY(targetX, targetY, coordinates)) ;			//will search for x,y until it finds a suitable value (in our out of range)
		findTheta(targetX, targetY, coordinates);				
		return coordinates;
	}
/*
 * findXY() will draw a circle of radius range around the target. Then, it'll guess a value of x and look if there is a value of y that can satisfy the circle.
 * If not, it'll increment x until it does. If x reaches the maximum value of the shooting area, it means the robot cannot shoot far enough to hit the target.
 *  If this happens, we will artificially increase the range and restart the procedure. In doing so, we are hoping for an anomaly that will make the launcher shoot longer.
 */
	private boolean findXY(double targetX, double targetY, double[] coordinates) {
		double x = minShootingArea-4,  yUpperCircle, yLowerCircle, temp;   //set x so the code doesnt consider any position before the minimum shootign area
		
		do{
			yLowerCircle = yUpperCircle = maxShootingArea;  				//set to max of shooting area so it wont interfere with the exit condition
			x+=5;															//if there is no y value at this x, increment x until it find one
			temp = Math.pow(rangeNormal(), 2) - Math.pow((targetX - x), 2); //calculate the position of the correcponding y value (whithout the square root to prevent imaginaty numbers)
			if (temp > 0) {													//is no imaginary number, calculate both part of the circle 
				yUpperCircle = targetY + Math.sqrt(temp);
				yLowerCircle = targetY - Math.sqrt(temp);
			}
			
			if(x >= maxShootingArea){										// If x reaches max shooting area without find a y value, the rage is too small, so increment it.
				flexibleRange[0] += 5*Math.cos(Math.toRadians(getRangeTheta()));
				flexibleRange[1] += 5*Math.sin(Math.toRadians(getRangeTheta()));
				return false;												//break the method, and retry until it works
			}
			
		} while(! (isInShootingArea(x) && (isInShootingArea(yUpperCircle) || isInShootingArea(yLowerCircle))));
		
		
		coordinates[0] = x;												//update coordinates
		if (isInShootingArea(yUpperCircle)) {
			coordinates[1] = yUpperCircle;
		}
		else {
			coordinates[1] = yLowerCircle;
		}
		return true;
	}

	private boolean isInShootingArea(double val) {
		if(val > minShootingArea && val < maxShootingArea-20){
			return true;
		}
		return false;
	}

	private void findTheta(double targetX, double targetY, double[] coordinates) {
		coordinates[2] = (Math.toDegrees(Math.atan2(targetX-odo.getX(), (targetY-odo.getY())))); /*- getRangeTheta() + 360)%360;*/
	}
	
	//return the deviation angle of the projectile trajectory
	private double getRangeTheta() {
		return Math.toDegrees(Math.atan2(range[0], range[1]));
	}

	//return the eucledian distance the projectile travel.
	private double rangeNormal() {
		return Math.sqrt(Math.pow(flexibleRange[0], 2) + Math.pow(flexibleRange[1], 2));
	}
}
