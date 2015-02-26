/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Localization.java
 *	Created On:	Feb 26, 2015
 */
package navigation.localization;

/**
 * Identifies the absolute position of the robot.
 * @author Oleg
 */
public abstract class Localization {
	
	/**
	 * 	Updates the odometer with the correct absolute x, y and theta values. 
	 */
	public abstract void doLocalization();
}
