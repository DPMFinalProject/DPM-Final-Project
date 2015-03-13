/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	OdoTest.java
 *	Created On:	Mar 3, 2015
 */
package tests.navigation;

import navigation.Driver;
import navigation.odometry.Odometer;
import tests.TestCase;
import util.Direction;

/**
 * Odometer calibration and testing class
 * @author Oleg
 */
public class OdoTest extends TestCase {
	
	final Driver driver;
	final Odometer odo;
	
	public OdoTest() {
		driver = new Driver();
		odo = new Odometer(driver);
	}
	
	/**
	 * @see tests.TestCase#runTest()
	 */
	@Override
	public void runTest() {
		
		(new Thread(odo)).start();
		
		(new Thread() {
			public void run() {
				while(true) {
					System.out.println(odo.getX()+ "\t"+ odo.getY()+"\t"+odo.getTheta());
					pause(500);
				}
			}
		}).start();
		
		driveSquare();
	}
	
	private void driveSquare()
	{
		driver.move(2*30.48, false);
		driver.turn(Direction.RIGHT, 90);
		System.out.println("Turn");
		driver.move(10*30.48, false);
		driver.turn(Direction.RIGHT, 90);
		System.out.println("Turn");
		driver.move(2*30.48, false);
		driver.turn(Direction.RIGHT, 90);
		System.out.println("Turn");
		driver.move(10*30.48, false);
		driver.turn(Direction.RIGHT, 90);
		System.out.println("Turn");
	}
	
	private void pause(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {}
	}
}
