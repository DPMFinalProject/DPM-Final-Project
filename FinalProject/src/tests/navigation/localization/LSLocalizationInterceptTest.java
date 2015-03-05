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
import navigation.localization.LSLocalization;
import navigation.localization.LSLocalizationIntercept;
import navigation.localization.LSLocalizationRotation;
import navigation.odometry.Odometer;
import tests.TestCase;
import util.Direction;

/**
 * 
 * @author Gregory Brookes
 */
public class LSLocalizationInterceptTest extends TestCase {

	/**
	 * @see tests.TestCase#runTest()
	 */
	@Override
	public void runTest() {
		Driver driver = new Driver();
		Odometer odo = new Odometer(driver);
		
		(new Thread(odo)).start();
		Navigation nav = new Navigation(odo, driver);
		
		
		LSLocalizationIntercept lsl=new LSLocalizationIntercept(odo,driver,nav);
		odo.setX(20);
		odo.setY(20);
		
		lsl.doLocalization();
		
	/*	System.out.println("X: " + odo.getX());
		System.out.println("Y: " + odo.getY());
		System.out.println("Theta: " + odo.getTheta());*/
	
	
		System.out.println("Localization Finished");
	}

}
