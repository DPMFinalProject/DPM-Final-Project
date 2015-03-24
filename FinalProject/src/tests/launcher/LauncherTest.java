/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	LauncherTest.java
 *	Created On:	Mar 24, 2015
 */
package tests.launcher;

import navigation.Launcher;
import navigation.Navigation;
import navigation.odometry.Odometer;
import tests.TestCase;
import util.Measurements;

/**
 * 
 * @author Oleg
 */
public class LauncherTest extends TestCase {

	@Override
	public void runTest() {
		Odometer odo = new Odometer();
		(new Thread(odo)).start();
		Navigation nav = new Navigation(odo);
		
		Launcher launcher = new Launcher(odo, nav);
		launcher.shootTo(3 * Measurements.TILE, 3 * Measurements.TILE);
	}

}
