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
import navigation.odometry.correction.CorrectionLightSensorSS;
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
		
		driver = new Driver(2.1, 15.6);
		odo = new Odometer(driver);
		correct = new CorrectionLightSensorSS(odo);
		display = new OdometryDisplay(odo);
		
	}
	
	@Override
	public void runTest() {
		
		(new Thread(odo)).start();
		(new Thread(correct)).start();
		(new Thread(display)).start();
		
		odo.setY(0);
		odo.setX(-15);
			
		//driveSquareIsh();
		driveLolRectangle();
	}
	
	private void driveSquareIsh() {
		driver.move(75, false);
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
		System.out.println("final X: "+odo.getX());
		System.out.println("final Y: "+odo.getY());
		System.out.println("final Theta: "+odo.getTheta());
	}
	
	private void driveLolRectangle() {
		driver.move(180, false);
		correct.stall();
		driver.turn(Direction.RIGHT, 90);
		correct.resume();
		driver.move(30, false);
		correct.stall();
		driver.turn(Direction.RIGHT, 90);
		correct.resume();
		driver.move(180, false);
		System.out.println("final X: "+odo.getX());
		System.out.println("final Y: "+odo.getY());
		System.out.println("final Theta: "+odo.getTheta());
	}
}
