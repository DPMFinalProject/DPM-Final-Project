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
import util.Measurements;
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
	}
	
	/**
	 * @see tests.TestCase#runTest()
	 */
	@Override
	public void runTest() {
		
		Navigation nav = new Navigation(odo);
		odo.setX(5 * Measurements.TILE);//this is so that the robot is in the correction range i.e. not near wall
		odo.setY(2 * Measurements.TILE);
		nav.travelToInTiles(5, 5, true);
		
	}
}