/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Commander.java
 *	Created On:	Feb 26, 2015
 */
package navigation;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.nxt.comm.RConsole;
import navigation.localization.LSLocalizationIntercept;
import navigation.localization.Localization;
import navigation.localization.USLocalization;
import navigation.odometry.Odometer;
import navigation.odometry.correction.CorrectionLightSensorSS;
import util.Measurements;
import util.Utilities;

/**
 *  This class is in charge of decision making and goal evaluation.
 * 	@author Oleg
 */
public class Commander {
	
	private static double[] target1 = {5, 9};
	private static double[] target2 = {};
	
//	private static double[][] destinations = {
//		{0, 0},
//		{-0.5, 2.5},
//		{-0.5, 5.5},
//		{1.5, 5.5},
//		{1.5, 6.5},
//		{4.5, 6.5},
//		{6, 6}
//		};
	
	private static double[][] destinations = {{0,0}, {1,5}, {6, 6}};
	
	private static void execute() {
		// Initialize main classes
		Odometer odo = new Odometer();
		(new Thread(odo)).start();
		
		(new Thread(){
			public void run() {
				
			}
		}).start();
		
		Navigation nav = new Navigation(odo);
		
		// Perform localization
		USLocalization usl = new USLocalization(odo, nav);
		usl.doLocalization();
		usl = null;
		
		Localization lsl = new LSLocalizationIntercept(odo, nav);
		lsl.doLocalization();
		lsl = null;
		
		odo.setX(0);
		odo.setY(0);
		odo.setTheta(0);
		
		//completed();
		
		CorrectionLightSensorSS correction = new CorrectionLightSensorSS(odo);
		(new Thread(correction)).start();
		
		for (int i = 1; i < destinations.length; i++)
			nav.travelToInTiles(destinations[i][0], destinations[i][1], true); // [0] = x, [1] = y
		
		completed();
		
		// Possibly relocalize at the destination
		//usl.doLocalization(destination[0], destination[1], 90);
		
		 //	Use launcher class to shoot balls into target
		
		Launcher launcher = new Launcher(odo, nav, 5, 8);
		launcher.shootToInTiles(target1[0], target1[1], 3);
		launcher = null;
		//launcher.shootTo(target1[0], target2[1]);
		
		completed();
		
		// Go back to the beginning
		nav.travelTo(0, 0, 0, true);
		
		// Travel to all the points in reverse order
		for (int i = destinations.length - 1; i > 0; i--)
			nav.travelToInTiles(destinations[i][0], destinations[i][1], true); // [0] = x, [1] = y
		
		usl = new USLocalization(odo, nav);
		usl.doLocalization();
		usl = null;
		
		lsl = new LSLocalizationIntercept(odo, nav);
		lsl.doLocalization();
		lsl = null;
	}
	
	private static void completed() {
		Sound.beep();
		Utilities.pause(2000);
	}
	
	public static void main(String[] args) {
		execute();
	}
}

