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
		
		
//		USLocalization usl= new USLocalizationMin(odo, nav);
		LSLocalizationIntercept lsl = new LSLocalizationIntercept(odo, nav);
		
	
//		usl.doLocalization();

		//lsl.doLocalization();
		
		nav.travelTo(0, 0, false);
		
		//System.out.println("Localization Finished");
		System.out.println("X: " + odo.getX());
		System.out.println("Y: " + odo.getY());
		System.out.println("Theta: " + odo.getTheta());
	}

}
