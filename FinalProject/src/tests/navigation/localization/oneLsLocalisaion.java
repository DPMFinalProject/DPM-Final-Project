/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	oneLsLocalisaion.java
 *	Created On:	Mar 4, 2015
 */
package tests.navigation.localization;

import navigation.Navigation;
import navigation.localization.LSLocalizationRotation;
import navigation.odometry.Odometer;
import tests.TestCase;

/**
 * 
 * @author Gregory Brookes
 */
public class oneLsLocalisaion extends TestCase {

	/**
	 * @see tests.TestCase#runTest()
	 */
	@Override
	public void runTest() {
		
		Odometer odo = new Odometer();
		
		(new Thread(odo)).start();
		Navigation nav = new Navigation(odo);
		
		
		LSLocalizationRotation lsl=new LSLocalizationRotation(odo, nav);
		
		lsl.doLocalization();
		
		System.out.println("X: " + odo.getX());
		System.out.println("Y: " + odo.getY());
		System.out.println("Theta: " + odo.getTheta());
	}

}
