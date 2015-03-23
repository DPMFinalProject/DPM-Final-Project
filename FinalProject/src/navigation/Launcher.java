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
	
	private Odometer odo;
	private Navigation nav;
	
	private final int LAUNCH_SPEED = 100;
	
	
	private final double[] range = {60,60};
	private final double[] shootingArea = {Measurements.TILE*9,Measurements.TILE*12};
	
	private final static NXTRegulatedMotor shooter = Motor.C;
	
	public Launcher(Odometer odo, Navigation nav) {
		this.odo = odo;
		this.nav = nav;
	}
	
	/**
	 * Position the robot and shoots a projectile to the coordinates (targetX, targetY).
	 * 
	 * @param targetX
	 * @param targetY
	 */
	public void shootTo(double targetX, double targetY) {
		nav.travelTo(findLaunchingCoordinates(targetX,targetY));
		Sound.twoBeeps();
		shoot(3);
	}

	private void shoot(int launchs) {
		Driver.turn(Direction.LEFT, getRangeTheta());
		shooter.setSpeed(LAUNCH_SPEED);
		for(int i=0; i < launchs; i++){
			shooter.rotate(360);
			pause(1000);
		}
	}

	private double[] findLaunchingCoordinates(double targetX, double targetY) {
		double[] coordinates = new double [3];
		findXY(targetX, targetY, coordinates);
		findTheta(targetX, targetY, coordinates);
		return coordinates;
	}

	private void findXY(double targetX, double targetY, double[] coordinates) {
		double x = shootingArea[0]-1,  yUpperCircle, yLowerCircle, temp;
		do{
			yUpperCircle=shootingArea[1];
			yLowerCircle=yUpperCircle;
			x++;
			temp = Math.pow(rangeNormal(), 2) - Math.pow( (targetX-x), 2);
			if(temp>0){
				yUpperCircle = targetY + Math.sqrt(temp);
				yLowerCircle = targetY - Math.sqrt(temp);
				
			}
		}while(isInShootingArea(x) && (isInShootingArea(yUpperCircle) || isInShootingArea(yLowerCircle)) );
		
		coordinates[0] = x;
		if(isInShootingArea(yUpperCircle)){
			coordinates[1] = yUpperCircle;
			System.out.println("Using Upper Circle");
		}else{
			coordinates[1] = yLowerCircle;
			System.out.println("Using Lower Circle");
		}
		
	}
	
	private boolean isInShootingArea(double val) {
		if(val > shootingArea[0] && val < shootingArea[1]){
			return true;
		}
		return false;
	}

	private void findTheta(double targetX, double targetY, double[] coordinates) {
		coordinates[2] = (Math.toDegrees(Math.atan2(targetX-odo.getX(), (targetY-odo.getY())))); /*- getRangeTheta() + 360)%360;*/
	}
	
	private double getRangeTheta(){
		return Math.toDegrees(Math.atan2(range[0], range[1]));
	}

	private double rangeNormal(){
		return Math.sqrt(Math.pow(range[0], 2) + Math.pow(range[1], 2));
	}
}
