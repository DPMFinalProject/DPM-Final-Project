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

/**
 * Odometer calibration and testing class
 * @author Oleg
 */
public class OdoTest extends TestCase {

	/**
	 * @see tests.TestCase#runTest()
	 */
	@Override
	public void runTest() {
		Odometer odo = new Odometer(new Driver(), null);
		
		(new Thread(odo)).start();
		
		while(true) {
			System.out.println(odo.getTheta());
		}
	}

}
