/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	BangBangAvoidanceTest.java
 *	Created On:	Mar 18, 2015
 */
package tests.navigation.avoidance;

import tests.TestCase;
import navigation.Navigation;
import navigation.odometry.Odometer;

/**
 * 	Tests avoidance capability of the robot.
 * @author Auguste
 */
public class BangBangAvoidanceTest extends TestCase {
	
	private Odometer odo;
	
	public BangBangAvoidanceTest() {
		odo = new Odometer();
		new Thread(odo).start();
		//avoider = new BangBangAvoider(Direction.RIGHT, odo);
	}
	
	/**
	 * @see tests.TestCase#runTest()
	 */
	@Override
	public void runTest() {
		
		Navigation nav = new Navigation(odo);
		nav.travelToInTiles(0, 3, true);
		
	}
}