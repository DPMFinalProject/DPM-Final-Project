/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	OdoTest.java
 *	Created On:	Mar 3, 2015
 */
package tests.navigation;

import navigation.odometry.Odometer;
import tests.TestCase;
import util.Paths;
import static util.Utilities.pause;

/**
 * Odometer calibration and testing class
 * @author Oleg
 */
public class OdoTest extends TestCase {
	
	final Odometer odo;
	
	public OdoTest() {

		odo = new Odometer();
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
		
		Paths.square();
	}
}
