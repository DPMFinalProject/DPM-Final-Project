/**
a *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	LSLocalization.java
 *	Created On:	Feb 26, 2015
 */
package navigation.localization;

import navigation.Driver;
import navigation.odometry.Odometer;

/**
 * 	Performs localization using the light sensor
 * @author Oleg
 */
public class LSLocalization extends Localization {
	
	public LSLocalization(Odometer odo, Driver driver) {
		super(odo, driver);
		throw new UnsupportedOperationException();
	}

	/**
	 * @see navigation.localization.Localization#doLocalization()
	 */
	@Override
	public void doLocalization() {
		/*
		 *  Remember to instantiate a desired FilteredSensor with your desired filters
		 *  You also have access to the odometer and driver from the parent class.
		 */
		throw new UnsupportedOperationException();
	}

}
