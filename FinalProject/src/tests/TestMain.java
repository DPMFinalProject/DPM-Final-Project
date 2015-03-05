/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Test.java
 *	Created On:	Feb 18, 2015
 *	
 */
package tests;

import tests.navigation.*;
import tests.navigation.localization.*;
import tests.sensors.*;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.RConsole;

/**
 *	Test execution main class, ensures that no resource leaks occur
 *	Class is abstract to prevent instantiation.
 * @author Oleg
 */
public class TestMain {

	private static void execute(String[] args) {
		TestCase test = new OdoWithCorrectionTest();

		test.runTest();
	}

	/*
	 * 	Configurations that have to be run before the test can occur
	 * 	These should be standard across all tests so try to avoid changing them 
	 */
	protected static void init() {
		
		LCD.drawString("LEFT FOR NORMAL", 0, 2);
		LCD.drawString("RIGHT FOR CONSOLE", 0, 3);
		if (Button.waitForAnyPress() == Button.ID_RIGHT) {
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
		final String[] passedArgs = args;
		try {
			init();
			
			(new Thread() {
				public void run() {
					execute(passedArgs);
				}
			}).start();
			Button.waitForAnyPress();
		} finally {
			done();
		}
	}
}
