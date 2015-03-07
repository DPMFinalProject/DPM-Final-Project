/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	BangBangAvoider.java
 *	Created On:	Mar 1, 2015
 */
package navigation.avoidance;

import navigation.Driver;
import navigation.odometry.Odometer;
import lejos.nxt.SensorPort;
import sensors.FilteredUltrasonicSensor;
import sensors.filters.AveragingFilter;
import util.Direction;

/**
 * An avoider based on bang-bang control theory
 * when avoid is called, the robot turns 90 degrees, looks towards the wall
 * it follows and and goes around the object until the orientation of the robot
 * is about 90 degrees less than the initial orientation
 * @author Auguste
 */
public class BangBangAvoider extends ObstacleAvoidance {
	private final FilteredUltrasonicSensor us = new FilteredUltrasonicSensor(SensorPort.S1, new AveragingFilter(5));
	private final int BAND_WIDTH = 8;
	private final int BAND_CENTER = 20;
	public double initialOrientation;
	
	public BangBangAvoider(Driver driver, Odometer odo)
	{
		super(driver, odo);
	}

//	public BangBangAvoider(Driver driver, NXTRegulatedMotor usMotor, FilteredUltrasonicSensor us, Odometer odo) {
//		this(driver, usMotor, us, odo, 8, 20);
//	}
	
//	private BangBangAvoider(Driver driver, NXTRegulatedMotor usMotor, FilteredUltrasonicSensor us, Odometer odo, int bandWidth, int bandCenter) {
//		super(driver, odo);
//		this.usMotor = usMotor;
//		this.us = us;
//		BAND_WIDTH = bandWidth;
//		BAND_CENTER = bandCenter;
//	}
	
	@Override
	public void avoid() {
		initialOrientation = odo.getTheta();
		
		driver.stop();
		
		driver.turn(Direction.RIGHT, 90);
		
		while(!hasAvoided()) {
			bangBang();
		}
	}
	
	private void bangBang() {
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
	
	private boolean hasAvoided() {
		if (isNear(initialOrientation-90, odo.getTheta())) {
			return true;
		}
		return false;
	}

	private boolean isNear(double targetAngle, double actualAngle) {
		return Math.abs(targetAngle - actualAngle) < 30;
	}
}
