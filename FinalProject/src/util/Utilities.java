/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Utilities.java
 *	Created On:	Mar 17, 2015
 */
package util;

/**
 * 	implements methods which are useful to many classes.
 * 	can be used with static imports
 * 
 * @author Auguste
 */
public class Utilities {
	
	public static void pause(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {}
	}
	
	public static boolean isNear(double targetValue, double actualValue, double error) {
		return Math.abs(targetValue - actualValue) < error;
	}
}