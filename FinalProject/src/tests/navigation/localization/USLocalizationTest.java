/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	USLocalizationTest.java
 *	Created On:	Mar 4, 2015
 */
package tests.navigation.localization;

import navigation.Driver;
import navigation.Navigation;
import navigation.localization.LSLocalizationIntercept;
import navigation.localization.USLocalization;
import navigation.localization.USLocalizationDiagonal;
import navigation.odometry.Odometer;
import tests.TestCase;
import util.Direction;

/**
 * 
 * @author Gregory Brookes
 */
public class USLocalizationTest extends TestCase {

	/**
	 * @see tests.TestCase#runTest()
	 */
	@Override
	public void runTest() {

		Odometer odo = new Odometer();
		
		(new Thread(odo)).start();
		Navigation nav = new Navigation(odo);
		
		
		USLocalization usl= new USLocalizationDiagonal(odo, nav);
		LSLocalizationIntercept lsl = new LSLocalizationIntercept(odo, nav);
		
	
		usl.doLocalization();
		lsl.doLocalization(0, -6, 0);
		Driver.turn(Direction.RIGHT, 90);
		lsl.doLocalization(-6, -6, 90);
		nav.travelTo(0, 0, 0, false);
	}

}
