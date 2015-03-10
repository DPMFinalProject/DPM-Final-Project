/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	OdoWithCorrectionTest.java
 *	Created On:	Mar 3, 2015
 */
package tests.navigation;

import lejos.nxt.Button;
import navigation.Driver;
import navigation.odometry.Odometer;
import navigation.odometry.correction.*;
import tests.TestCase;
import util.Direction;
import util.OdometryDisplay;

/**
 * Odometry Correction test
 * @author Auguste
 */
public class OdoWithCorrectionTest extends TestCase {

	/**
	 * @see tests.TestCase#runTest()
	 */
	Driver driver;
	Odometer odo;
	CorrectionLightSensorSS correct;
	OdometryDisplay display;
	
	public OdoWithCorrectionTest() {
		
		driver = new Driver();
		odo = new Odometer(driver);
		correct = new CorrectionLightSensorSS(odo);
		display = new OdometryDisplay(odo);
		
	}
	
	@Override
	public void runTest() {
		
		(new Thread(odo)).start();
		(new Thread(correct)).start();
		(new Thread(display)).start();
		
		driveSquare();
		
		Button.waitForAnyPress();
	}
	
	private void driveSquare()
	{
		driver.move(60, false);
		correct.stall();
		driver.turn(Direction.RIGHT, 90);
		correct.resume();
		driver.move(60, false);
		correct.stall();
		driver.turn(Direction.RIGHT, 90);
		correct.resume();
		driver.move(60, false);
		correct.stall();
		driver.turn(Direction.RIGHT, 90);
		correct.resume();
		driver.move(60, false);
		correct.stall();
		driver.turn(Direction.RIGHT, 90);
		correct.resume();
		driver.move(15, false);
		System.out.println("final orientation: "+odo.getTheta());
	}
}
