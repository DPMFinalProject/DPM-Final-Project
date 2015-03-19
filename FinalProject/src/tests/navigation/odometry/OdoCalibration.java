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
		
		distanceTest(6);
	}
	
	private void distanceTest(int tiles){
		Driver.move(30.48*tiles);
	}
}
