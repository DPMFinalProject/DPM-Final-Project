/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	USLocalizationTest.java
 *	Created On:	Mar 4, 2015
 */
package tests.navigation.localization;

import navigation.Navigation;
import navigation.localization.USLocalization;
import navigation.odometry.Odometer;
import tests.TestCase;

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
		
		
		USLocalization usl= new USLocalization(odo, nav);
		//odo.setX(40);
		//odo.setY(40);
	
		usl.doLocalization();
	
		//System.out.println("Localization Finished");
		System.out.println("X: " + odo.getX());
		System.out.println("Y: " + odo.getY());
		System.out.println("Theta: " + odo.getTheta());
	}

}
