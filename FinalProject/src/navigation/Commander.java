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
import navigation.localization.USLocalizationDiagonal;
import navigation.odometry.Odometer;
import navigation.odometry.correction.CorrectionLightSensorSS;
import util.Direction;
import util.Utilities;

/**
 *  This class is in charge of decision making and goal evaluation.
 * 	@author Oleg
 */
public class Commander {
	
	private static boolean MAPPED = true;
	
	private static double[][] destinations = {
		{2, 3},
		{1, 4},
		{5, 4},
		{5, 6}
		};
	
	private static int NUMBER_OF_SHOTS = 3;
	
	private static double[] target1 = {4, 5};
	//private static double[] target2 = {9, 9};
	
	private static void execute() {
		
//--------------------------------------- INITIALIZE ODOMETER & NAVIGATION ---------------------------------------
		
		final Odometer odo = new Odometer();
		(new Thread(odo)).start();
		
		Navigation nav = new Navigation(odo);
		
//--------------------------------------- PERFORM LOCALIZATION ---------------------------------------
		
		USLocalization usl = new USLocalization(odo, nav);
		usl.doLocalization(0, 0, 0);
		usl = null;

		Localization lsl = new LSLocalizationIntercept(odo, nav);
		lsl.doLocalization(0, -6, 0);	
		Driver.turn(Direction.RIGHT, 90);
		Driver.move(-5);
		lsl.doLocalization(-6, -6, 90);
		lsl = null;
				
		completed();
		System.out.println("DONE: Localization");
		
//--------------------------------------- START ODOMETRY CORRECTION ---------------------------------------
		
		CorrectionLightSensorSS correction = new CorrectionLightSensorSS(odo);
		(new Thread(correction)).start();
		
		(new Thread() {
			public void run() {
				while(true) {
					System.out.println(""+odo.getX()+"\t"+odo.getY()+"\t"+odo.getTheta());
					Utilities.pause(500);
				}
			}
		}
		).start();
		
//--------------------------------------- GO TO SHOOTING AREA ---------------------------------------
				
		for (double[] destination : destinations) {
			nav.travelToInTiles(destination[0], destination[1], !MAPPED);
		}
		
		completed();
		System.out.println("DONE: Travel to Destination");
		
//--------------------------------------- RE-LOCALIZE ---------------------------------------

		//usl.doLocalization(destination[0], destination[1], 90);
		
//--------------------------------------- LAUNCH BALLS ---------------------------------------
		
//		Launcher launcher = new Launcher(odo, nav, 5, 8);//5 and 8 should be variables!!
//		launcher.shootToInTiles(target1[0], target1[1], NUMBER_OF_SHOTS);
//		//launcher.shootToInTiles(target2[0], target2[1], NUMBER_OF_SHOTS);
//		launcher = null;
//		
//		completed();
//		System.out.println("DONE: Launching");
		
//--------------------------------------- GO BACK TO START ---------------------------------------
		
		nav.travelTo(0, 0, false);
		System.out.println("DONE: Travel Back to Origin");
		
//--------------------------------------- RE-LOCALIZE ---------------------------------------
		
//		usl = new USLocalizationDiagonal(odo, nav);
//		usl.doLocalization(0, 0, 0);
//		usl = null;
//
//		lsl = new LSLocalizationIntercept(odo, nav);
//		lsl.doLocalization(0, -6, 0);	
//		Driver.turn(Direction.RIGHT, 90);
//		Driver.move(-5);
//		lsl.doLocalization(-6, -6, 90);
//		lsl = null;
//				
//		completed();
//		
//		nav.travelTo(0, 0, 0, false);
//		
//		System.out.println("DONE: Final Localization");
		
//--------------------------------------- VICTOIRE ---------------------------------------
		
//		new util.songs.Victory().play();
		
	}
	
	private static void completed() {
		Sound.beep();
		Utilities.pause(2000);
	}
	
//	public static void main(String[] args) {
//		
//		LCD.drawString("    PUSH TO", 0, 3);
//		LCD.drawString("     START", 0, 4);
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