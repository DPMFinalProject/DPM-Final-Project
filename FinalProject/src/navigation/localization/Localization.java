/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Localization.java
 *	Created On:	Feb 26, 2015
 */
package navigation.localization;

import navigation.Navigation;
import navigation.odometry.Odometer;

/**
 * Identifies the absolute position of the robot.
 * @author Oleg
 */
public abstract class Localization {
	protected final Odometer odo;
	protected final Navigation nav;
	
	public Localization(Odometer odo, Navigation nav) {
		this.odo = odo;
		this.nav = nav;
	}
	
	/**
	 * 	Localizes the robot and updates the vales of the odometer correctly. 
	 */
	public abstract void doLocalization();
	
	/**
	 * 	Localizes the robot and updates the vales of the odometer correctly.
	 * 	@param x The x position at which the robot should end up after localizing
	 * 	@param y The y position at which the robot should end up after localizing
	 *  @param theta The orientation of the robot after localizing
	 */
	public abstract void doLocalization(double x, double y, double theta);
}
