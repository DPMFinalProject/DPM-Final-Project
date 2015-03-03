/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	ObstacleAvoidance.java
 *	Created On:	Mar 1, 2015
 */
package navigation.avoidance;

import navigation.Driver;
import navigation.odometry.Odometer;
import lejos.nxt.NXTRegulatedMotor;
import sensors.FilteredUltrasonicSensor;
import util.Direction;

/**
 * An avoider based on bang-bang control theory
 * when avoid is called, the robot turns 90 degrees, looks towards the wall
 * it follows and and goes around the object until the orientation of the robot
 * is about 90 degrees less than the initial orientation
 * @author Auguste
 */
public class BangBangAvoider extends ObstacleAvoidance {
	private final Driver driver;
	private final Odometer odo;
	private final NXTRegulatedMotor usMotor;
	private final FilteredUltrasonicSensor us;
	private final int BAND_WIDTH, BAND_CENTER;
	public double initialOrientation;

	public BangBangAvoider(Driver driver, NXTRegulatedMotor usMotor, FilteredUltrasonicSensor us, Odometer odo) {
		this(driver, usMotor, us, odo, 8, 20);
	}
	
	public BangBangAvoider(Driver driver, NXTRegulatedMotor usMotor, FilteredUltrasonicSensor us, Odometer odo,int bandWidth, int bandCenter) {
		this.driver = driver;
		this.usMotor = usMotor;
		this.us = us;
		this.odo = odo;
		BAND_WIDTH = bandWidth;
		BAND_CENTER = bandCenter;
	}
	
	@Override
	public void avoid() {
		initialOrientation = odo.getTheta();
		
		driver.stop();
		
		lookTowardsWall();
		driver.turn(90);
		
		while(!hasAvoided()) {
			bangBang();
		}

		lookForward();
	}
	
	public void lookTowardsWall() {
		usMotor.rotate(80);
	}
	
	public void lookForward() {
		usMotor.rotate(-80);
	}
	
	public void bangBang() {
		double error = BAND_CENTER - us.getFilteredData();
		
		if (Math.abs(error)<BAND_WIDTH)	{
			driver.move(Direction.FWD);
		}
		else if (error < 0) {
			driver.drift(Direction.RIGHT);
		}
		else {
			driver.drift(Direction.LEFT);
		}
	}
	
	public boolean hasAvoided() {
		if (isNear(initialOrientation-90, odo.getTheta())) {
			return true;
		}
		return false;
	}

	boolean isNear(double targetAngle, double actualAngle) {
		return Math.abs(targetAngle - actualAngle) < 30;
	}
}
