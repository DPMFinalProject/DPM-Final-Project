/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	DriverTests.java
 *	Created On:	Mar 3, 2015
 */
package tests.navigation;

import navigation.Driver;
import navigation.odometry.Odometer;
import tests.TestCase;
import util.Direction;

/**
 * 	Test Class used to calibrate the Driver class
 * @author Oleg
 */
public class DriverTests extends TestCase {

	/**
	 * @see tests.TestCase#runTest()
	 */
	@Override
	public void runTest() {
		Driver driver = new Driver();
		Odometer odo = new Odometer(driver);
		
		(new Thread(odo)).start();
		
		//driver.move(30);
		driver.turn(Direction.RIGHT, 360);
		
		System.out.println("X: " + odo.getX());
		System.out.println("Y: " + odo.getY());
		System.out.println("Theta: " + odo.getTheta());
	}

}
