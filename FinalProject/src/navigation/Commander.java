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
import navigation.localization.FullLocalization;
import navigation.localization.LSLocalizationIntercept;
import navigation.localization.Localization;
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
	
	private static boolean MAPPED = false;
	
	private static double[][] destinations = {
		{1, 5},
		{6, 5},
		};
	
	private static int NUMBER_OF_SHOTS = 2;
	
	private static double[] target1 = {9, 6};
	private static double[] target2 = {8, 1};
	
	private static void execute() {
		
//--------------------------------------- INITIALIZE ODOMETER & NAVIGATION ---------------------------------------
		
		final Odometer odo = new Odometer();
		(new Thread(odo)).start();
		
		Navigation nav = new Navigation(odo);
		
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
		
//--------------------------------------- PERFORM LOCALIZATION ---------------------------------------
		
		// add localization
		FullLocalization localization = new FullLocalization(odo, nav);
		localization.doLocalization(0, 0, 0);
		
//--------------------------------------- GO TO SHOOTING AREA ---------------------------------------
				
		for (double[] destination : destinations) {
			nav.travelToInTiles(destination[0], destination[1], !MAPPED);
		}
		
		completed();
		System.out.println("DONE: Travel to Destination");
		
//--------------------------------------- RE-LOCALIZE ---------------------------------------

		//usl.doLocalization(destination[0], destination[1], 90);
		
//--------------------------------------- LAUNCH BALLS ---------------------------------------
		
		Launcher launcher = new Launcher(odo, nav, 4, 7);//5 and 8 should be variables!!
		launcher.shootToInTiles(target1[0], target1[1], NUMBER_OF_SHOTS);
		launcher.shootToInTiles(target2[0], target2[1], NUMBER_OF_SHOTS);
		launcher = null;
		
		
//--------------------------------------- GO BACK TO START ---------------------------------------
		
		for (int i = destinations.length - 1; i > 0; i--) {
			nav.travelToInTiles(destinations[i][0], destinations[i][1], !MAPPED);
		}
		
//--------------------------------------- RE-LOCALIZE ---------------------------------------
		
		localization.doLocalization(0, 0, 0);
		
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