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
import lejos.nxt.Sound;
import navigation.localization.LSLocalizationIntercept;
import navigation.localization.Localization;
import navigation.localization.USLocalization;
import navigation.odometry.Odometer;
import navigation.odometry.correction.CorrectionLightSensorSS;
import util.Direction;
import util.Measurements;
import util.Utilities;

/**
 *  This class is in charge of decision making and goal evaluation.
 * 	@author Oleg
 */
public class Commander {
	
	private static double[] target1 = {5, 9};
	private static double[] target2 = {9, 9};
	
//	private static double[][] destinations = {
//		{0, 0},
//		{-0.5, 2.5},
//		{-0.5, 5.5},
//		{1.5, 5.5},
//		{1.5, 6.5},
//		{4.5, 6.5},
//		{6, 6}
//		};
	
	private static double[][] destinations = {{2.5,-0.5}, {6, 6}};
	
	private static void execute() {
		
//--------------------------------------- INITIALIZE ODOMETER & NAVIGATION ---------------------------------------
		
		Odometer odo = new Odometer();
		(new Thread(odo)).start();
		
		Navigation nav = new Navigation(odo);
		
//--------------------------------------- PERFORM LOCALIZATION ---------------------------------------
		
		USLocalization usl = new USLocalization(odo, nav);
		usl.doLocalization();
		usl = null;
	
		Driver.move(-2);
		Localization lsl = new LSLocalizationIntercept(odo, nav);
		lsl.doLocalization();
//		Driver.turn(Direction.LEFT, 90);
//		Driver.move(-10);
//		lsl.doLocalization();
		lsl = null;
		
		odo.setX(0);
		odo.setY(-6);
		odo.setTheta(0);
		
		completed();
		System.out.println("DONE: First Localization");
		
//--------------------------------------- START ODOMETRY CORRECTION ---------------------------------------
		
		CorrectionLightSensorSS correction = new CorrectionLightSensorSS(odo);
		(new Thread(correction)).start();

//--------------------------------------- GO TO SHOOTING AREA ---------------------------------------
		
		//unmapped area
		for (double[] destination : destinations) {
			nav.travelToInTiles(destination[0], destination[1], true);
		}
		
		//through mapped area
//		for (double[] destination : destinations) {
//			nav.travelToInTiles(destination[0] - 2.5/Measurements.TILE, 
//					destination[1] + 2.5/Measurements.TILE, false); // [0] = x, [1] = y
//		}
		
		completed();
		System.out.println("DONE: Travel to Destination");
		
//--------------------------------------- RE-LOCALIZE ---------------------------------------

		//usl.doLocalization(destination[0], destination[1], 90);
		
//--------------------------------------- LAUNCH BALLS ---------------------------------------
		
		Launcher launcher = new Launcher(odo, nav, 5, 8);
		launcher.shootToInTiles(target1[0], target1[1], 3);
		//launcher.shootToInTiles(target2[0], target2[1], 3);
		launcher = null;
		
		completed();
		System.out.println("DONE: Launching");
		
//--------------------------------------- GO BACK TO START ---------------------------------------
		
		nav.travelTo(0, 0, 0, true);
		System.out.println("DONE: Travel Back to Origin");
		
//--------------------------------------- RE-LOCALIZE ---------------------------------------
		
		usl = new USLocalization(odo, nav);
		usl.doLocalization();
		usl = null;
		
		Driver.move(-2);
		lsl = new LSLocalizationIntercept(odo, nav);
		lsl.doLocalization();
		Driver.turn(Direction.LEFT, 90);
		Driver.move(-10);
		lsl.doLocalization();
		lsl = null;
		
		odo.setX(6);
		odo.setY(-6);
		odo.setTheta(270);
		
		nav.travelTo(0, 0, 0, false);
		
		System.out.println("DONE: Final Localization");
	}
	
	private static void completed() {
		Sound.beep();
		Utilities.pause(2000);
	}
	
	public static void main(String[] args) {
		Button.waitForAnyPress();
		
		(new Thread() {
			public void run() {
				execute();
			}
		}).start();
		
		Button.waitForAnyPress();
		System.exit(0);
	}
}

