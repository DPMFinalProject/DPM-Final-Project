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
	
	/**
	 * abstraction of Thread.sleep()
	 * 
	 * @param ms time in milliseconds to sleep
	 */
	public static void pause(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {}
	}
	
	/**
	 * checks if acualValue is within an error of +- error from targetValue
	 * 
	 * @param targetValue
	 * @param actualValue
	 * @param error
	 * @return
	 */
	public static boolean isNear(double targetValue, double actualValue, double error) {
		return Math.abs(targetValue - actualValue) < error;
	}
}