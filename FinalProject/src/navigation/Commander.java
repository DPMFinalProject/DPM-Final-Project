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
import util.Measurements;
import util.ResourceManager;
import util.Utilities;

/**
 *  This class is in charge of decision making and goal evaluation.
 * 	@author Oleg
 */
public class Commander {
	
	private static double[] target1 = {13, 9};
	private static double[] target2 = {};
	
	private static double[] destination = {0.5 * Measurements.TILE, 6 * Measurements.TILE};
	
	private static void execute() {
		// Initialize main classes
		Odometer odo = new Odometer();
		(new Thread(odo)).start();
		Navigation nav = new Navigation(odo);
		
		/*// Perform localization
		USLocalization usl = new USLocalization(odo, nav);
		usl.doLocalization();
		usl = null;
		
		Localization lsl = new LSLocalizationIntercept(odo, nav); // Has to be implemented first.
		lsl.doLocalization();
		lsl = null;
		
		completed();
		
		nav.travelTo(destination[0], destination[1]);
		
		completed();
		*/
		// Possibly relocalize at the destination
		/*usl.doLocalization(destination[0], destination[1], 90);
		
		*/
		
		 //	Use launcher class to shoot balls into target
		
		odo.setX(Measurements.TILE*9);
		odo.setY(Measurements.TILE*9);
		odo.setTheta(0);
		
		//Sound.twoBeeps();		
		Launcher launcher = new Launcher(odo, nav);
		launcher.shootTo(target1[0]  * Measurements.TILE, 
				target1[1] * Measurements.TILE);
		//launcher.shootTo(target1[0], target2[1]);
		
		completed();
		/*
		// Go back to the beginning
		nav.travelTo(0, 0, 0);
		usl.doLocalization();*/
		
	}
	
	private static void completed() {
		Sound.beep();
		Utilities.pause(2000);
	}

	/*
	 * 	Configurations that have to be run before the test can occur
	 * 	These should be standard across all tests so try to avoid changing them 
	 */
	protected static void init() {
		
		LCD.drawString("LEFT FOR NORMAL", 0, 2);
		LCD.drawString("RIGHT FOR CONSOLE", 0, 3);
		if (Button.waitForAnyPress() == Button.ID_RIGHT) {
			LCD.clear();
			
			RConsole.open();
			
			LCD.drawString("Press a button", 0, 3);
			LCD.drawString("to start", 0, 4);
			Button.waitForAnyPress();
			System.setOut(RConsole.getPrintStream());
		}
	}
	
	protected static void done() {		
		RConsole.close();
		System.exit(0);
	}
	
	public static void main(String[] args) {
		try {
			init();

			(new Thread() {
				public void run() {
					execute();
				}
			}).start();
			Button.waitForAnyPress();
		} finally {
			done();
		}
	}
}

