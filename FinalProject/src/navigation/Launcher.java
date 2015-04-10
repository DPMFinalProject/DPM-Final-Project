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
	private final double[] range = {0,151.9};
	
	private final Odometer odo;
	private final Navigation nav;
	private final int LAUNCH_SPEED = 300;
	private double flexibleOffset;
	private double flexibleRange;
	private final double minShootingArea;
	private final double maxShootingArea;
	private boolean isRangeTooBig;
	
	private final static NXTRegulatedMotor shooter = Motor.C;
	
	public Launcher(Odometer odo, Navigation nav, double minShootingArea, double maxShootingArea){
		this.odo = odo;
		this.nav = nav;
		this.minShootingArea = minShootingArea * Measurements.TILE;
		this.maxShootingArea = maxShootingArea * Measurements.TILE;
	}
	
	public Launcher(Odometer odo, Navigation nav) {
		this.odo = odo;
		this.nav = nav;
		minShootingArea = 8 * Measurements.TILE;
		maxShootingArea = 11 * Measurements.TILE;
	}
	
	/**
	 * Position the robot and shoots some projectiles to the coordinates (targetX, targetY).
	 * 
	 * @param targetX The x coordinate of the target, in tiles.
	 * @param targetY The y coordinate of the target, in tiles.
	 * @param projectiles The number of balls to shoot at the target.
	 */
	public void shootToInTiles(double targetX, double targetY, int projectiles) {
		shootTo(targetX * Measurements.TILE, targetY * Measurements.TILE, projectiles);
	}
	
	private void shootTo(double targetX, double targetY, int projectiles) {
		flexibleRange = range[1];
		flexibleOffset = range[0];
		isRangeTooBig=false;
		nav.travelTo(findLaunchingCoordinates(targetX, targetY), false);
		shoot(projectiles);
	}

	private void shoot(int launches) {
		Driver.turn(Direction.LEFT, getRangeTheta());		//position himself to compensate for the launch offset
		shooter.setSpeed(LAUNCH_SPEED);
		
		for(int i=0; i < launches; i++){
			shooter.rotate(360);
			pause(1000);
		}
	}

	private double[] findLaunchingCoordinates(double targetX, double targetY) {
		double[] coordinates = new double [3];
												//will enable the robot to try to shoot at the target even if this one is out of range
		while(! findXY(targetX, targetY, coordinates));	{}			//will search for x,y until it finds a suitable value (in our out of range)
		findTheta(targetX, targetY, coordinates);
		//System.out.println(""+coordinates[0]+" - "+coordinates[1]);
		return coordinates;
	}
/*
 * 	findXY() will draw a circle of radius range around the target. Then, it'll guess 
 * 	a value of x and look if there is a value of y that can satisfy the circle.
 * 
 *	If not, it'll increment x until it does. If x reaches the maximum value of the 
 * 	shooting area, it means the robot cannot shoot far enough to hit the target.
 * 
 *  If this happens, we will artificially increase the range and restart the procedure. 
 *  In doing so, we are hoping for an anomaly that will make the launcher shoot longer.
 */
	private boolean findXY(double targetX, double targetY, double[] coordinates) {
		double x = minShootingArea - 4,  yUpperCircle, yLowerCircle, temp;   //set x so the code doesnt consider any position before the minimum shootign area
		
		do{
			yLowerCircle = yUpperCircle = maxShootingArea;  				//set to max of shooting area so it wont interfere with the exit condition
			x += 10;															//if there is no y value at this x, increment x until it find one
			temp = Math.pow(rangeNormal(), 2) - Math.pow((targetX - x), 2); //calculate the position of the correcponding y value (whithout the square root to prevent imaginaty numbers)
			if (temp > 0) {													//is no imaginary number, calculate both part of the circle 
				yUpperCircle = targetY + Math.sqrt(temp);
				yLowerCircle = targetY - Math.sqrt(temp);
			}
			
			//System.out.println("range: "+rangeNormal());
			
			if(x >= maxShootingArea && !isRangeTooBig) {										// If x reaches max shooting area without find a y value, the rage is too small, so increment it.
				flexibleOffset += 10 * Math.sin(Math.toRadians(getRangeTheta()));
				flexibleRange += 10 * Math.cos(Math.toRadians(getRangeTheta()));
				if(rangeNormal()> 225){
					isRangeTooBig = true;
					flexibleRange = range[1];
					flexibleOffset = range[0];
				}
				return false;												//break the method, and retry until it works
			}else if(x >= maxShootingArea && isRangeTooBig){
				flexibleOffset -=  Math.sin(Math.toRadians(getRangeTheta()));
				flexibleRange  -=  Math.cos(Math.toRadians(getRangeTheta()));
				return false;
			}
			//System.out.println("      x:"+x+" - "+yUpperCircle+" - "+yLowerCircle);
			
		} while(! (isInShootingArea(x) && (isInShootingArea(yUpperCircle) || isInShootingArea(yLowerCircle))));
		
		coordinates[0] = x; //update coordinates
		if (isInShootingArea(yUpperCircle)) {
			coordinates[1] = yUpperCircle;
		}
		else {
			coordinates[1] = yLowerCircle;
		}
		return true;
	}

	private boolean isInShootingArea(double val) {
		return val > minShootingArea && val < maxShootingArea-20;
//		if(val > minShootingArea && val < maxShootingArea-20){
//			return true;
//		}
//		return false;
	}

	private void findTheta(double targetX, double targetY, double[] coordinates) {
//		if(targetX-coordinates[0]<1){
			coordinates[2] = nav.adjustRefFrame(Math.toDegrees(Math.atan2(targetY-coordinates[1], targetX-coordinates[0])));
//		}else{
//			coordinates[2] = nav.adjustRefFrame(Math.toDegrees(Math.atan2(targetX-coordinates[0], targetY-coordinates[1]))); 
//		}
		//System.out.println((int)coordinates[2] + "   "+ (int) coordinates[0]+ "     "+ (int) coordinates[1]+"     "+ (int) targetX+"     "+ (int) targetY);
	}
	
	//return the deviation angle of the projectile trajectory
	private double getRangeTheta() {
		return Math.toDegrees(Math.atan2(range[0], range[1]));
	}

	//return the eucledian distance the projectile travel.
	private double rangeNormal() {
		return Math.sqrt(Math.pow(flexibleOffset, 2) + Math.pow(flexibleRange, 2));
	}
}
