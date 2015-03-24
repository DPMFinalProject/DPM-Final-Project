/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	OdoCalibration.java
 *	Created On:	Mar 3, 2015
 */
package tests.navigation.odometry;

import navigation.Driver;
import navigation.odometry.Odometer;
import tests.TestCase;
import util.Direction;
import util.Measurements;
import static util.Utilities.pause;

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
		
		rotateTest(1);
	}
	
	@SuppressWarnings("all")
	private void distanceTest(int tiles){
		Driver.move(Measurements.TILE * tiles);
	}
	
	@SuppressWarnings("all")
	private void rotateTest(int turns){
		Driver.turn(Direction.RIGHT, turns*360);
	}
}
