/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Commander.java
 *	Created On:	Feb 26, 2015
 */
package navigation;

import util.Measurements;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.RConsole;
import navigation.localization.FullLocalization;
import navigation.odometry.Odometer;
import navigation.odometry.correction.CorrectionLightSensorSS;

/**
 *  This class is in charge of decision making and goal evaluation.
 * 	@author Oleg
 */
public class Commander {
	
	private static boolean MAPPED = true;
	
	private static int[][] destinations = {
		{0, 0},
		{8, 8}
		};
	
	private static int NUMBER_OF_SHOTS = 1;
	private static int SHOOTING_RANGE_START = 8;
	private static int SHOOTING_RANGE_END = 11;
	private static int[][] targets = {
		{14, 11}, 
		{11, 12},
		{15, 15}
		};
	
	private static void execute() {
		
//--------------------------------------- INITIALIZE ODOMETER & NAVIGATION ---------------------------------------
		
		final Odometer odo = new Odometer();
		(new Thread(odo)).start();
		
		Navigation nav = new Navigation(odo);
		
////--------------------------------------- START ODOMETRY CORRECTION ---------------------------------------
//		
//		CorrectionLightSensorSS correction = new CorrectionLightSensorSS(odo);
//		(new Thread(correction)).start();
//		
////--------------------------------------- PERFORM LOCALIZATION ---------------------------------------
//		
//		FullLocalization localization = new FullLocalization(odo, nav);
//		localization.doLocalization(0, 0, 0);
//		
////--------------------------------------- GO TO SHOOTING AREA ---------------------------------------
//				
//		for (int i = 1; i < destinations.length; i++) {
//			nav.travelToInTiles(destinations[i][0], destinations[i][1], !MAPPED);
//		}
//		
//		System.out.println("At destination");
//		
////--------------------------------------- RE-LOCALIZE ---------------------------------------
//
//		//usl.doLocalization(destination[0], destination[1], 90);
		
//--------------------------------------- LAUNCH BALLS ---------------------------------------
		
		odo.setX(8 * Measurements.TILE);
		odo.setY(8 * Measurements.TILE);
		
		Launcher launcher = new Launcher(odo, nav, SHOOTING_RANGE_START, SHOOTING_RANGE_END);
		
		for (int[] target : targets) {
			launcher.shootToInTiles(target[0], target[1], NUMBER_OF_SHOTS);
		}
		
		launcher = null;
		
		System.out.println("shot");
		
//--------------------------------------- GO BACK TO START ---------------------------------------
		
		for (int i = destinations.length - 1; i >= 0; i--) {
			nav.travelToInTiles(destinations[i][0], destinations[i][1], !MAPPED);
		}
		
		System.out.println("Done");
		
//--------------------------------------- RE-LOCALIZE ---------------------------------------
		
		//localization.doLocalization(0, 0, 0);
		
//--------------------------------------- VICTOIRE ---------------------------------------
		
		new util.songs.Victory().play();
		
	}
	
//	private static void completed() {
//		Sound.beep();
//		Utilities.pause(2000);
//	}
	
//	public static void main(String[] args) {
//		
//		util.Art.drawInvader();
//		
//		Button.waitForAnyPress();
//		
//		(new Thread() {
//			public void run() {
//				execute();
//			}
//		}).start();
//		
//		Button.waitForAnyPress();
//		System.exit(0);
//	}
	
	private static void init() {
		
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
	
	private static void done() {		
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