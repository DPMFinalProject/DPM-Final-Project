/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Localization.java
 *	Created On:	Feb 26, 2015
 */
package navigation.localization;

import sensors.FilteredSensor;
import navigation.Driver;
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
	 * 	Updates the odometer with the correct absolute x, y and theta values. 
	 */
	public abstract void doLocalization();
}
