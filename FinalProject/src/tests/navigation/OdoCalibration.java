/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	OdoCalibration.java
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
public class OdoCalibration extends TestCase {
	
	/**
	 * @see tests.TestCase#runTest()
	 */
	@Override
	public void runTest() {
		final Odometer odo = new Odometer();
		
		(new Thread(odo)).start();
		
		(new Thread() {
			public void run() {
				while(true) {
					System.out.println("x: "+odo.getX());
					System.out.println("y: "+odo.getY());
					System.out.println("T: "+odo.getTheta());
					pause(1000);
				}
			}
		}).start();
		
		distanceTest(6);
	}
	
	private void driveSquare()
	{
		Driver.move(60, false);
		Driver.turn(Direction.RIGHT, 90);
		Driver.move(60, false);
		Driver.turn(Direction.RIGHT, 90);
		Driver.move(60, false);
		Driver.turn(Direction.RIGHT, 90);
		Driver.move(60, false);
		Driver.turn(Direction.RIGHT, 90);
	}
	
	private void distanceTest(int tiles){
		Driver.move(30.48*tiles,false);
	}
	
	private void pause(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {}
	}
}
