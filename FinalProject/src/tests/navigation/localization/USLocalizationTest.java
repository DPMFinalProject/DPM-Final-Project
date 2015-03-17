/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	oneLsLocalisaion.java
 *	Created On:	Mar 4, 2015
 */
package tests.navigation.localization;

import navigation.Driver;
import navigation.Navigation;
import navigation.localization.LSLocalizationIntercept;
import navigation.localization.LSLocalizationRotation;
import navigation.localization.USLocalization;
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
		Driver driver = new Driver();
		Odometer odo = new Odometer(driver);
		
		(new Thread(odo)).start();
		Navigation nav = new Navigation(odo, driver);
		
		
		USLocalization usl= new USLocalization(odo,driver,nav);
		//odo.setX(40);
		//odo.setY(40);
	
		usl.doLocalization();
	
		//System.out.println("Localization Finished");
		System.out.println("X: " + odo.getX());
		System.out.println("Y: " + odo.getY());
		System.out.println("Theta: " + odo.getTheta());
	}

}
