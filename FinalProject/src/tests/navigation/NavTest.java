/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	NavTest.java
 *	Created On:	Mar 3, 2015
 */
package tests.navigation;

import lejos.nxt.Sound;
import navigation.Driver;
import navigation.Navigation;
import navigation.odometry.Odometer;
import tests.TestCase;

/**
 * Checks to make sure that the navigation behaves correctly
 * @author Oleg
 */
public class NavTest extends TestCase {

	/**
	 * @see tests.TestCase#runTest()
	 */
	@Override
	public void runTest() {
		Driver driver = new Driver();
		Odometer odo = new Odometer(driver);
		
		(new Thread(odo)).start(); 
		Navigation nav = new Navigation(odo, driver);
		
		nav.travelTo(30, 30, 135);
		nav.travelTo(60, 30, 180);
		nav.travelTo(0, 0, 0);
	}

}
