/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	ResourceManager.java
 *	Created On:	Feb 26, 2015
 */
package util;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.RConsole;

/**
 *	Takes care of setting up RConsole and closing RConsole
 * @author Oleg
 */
public class ResourceManager {
	/**
	 * 	Standard run configurations 
	 */
	public static void init() {
		RConsole.open();
		
		LCD.drawString("Press a button", 0, 3);
		LCD.drawString("to start", 0, 4);
		Button.waitForAnyPress();
		System.setOut(RConsole.getPrintStream());
	}
	
	public static void done() {		
		Button.waitForAnyPress();
		RConsole.close();
		System.exit(0);
	}
}
