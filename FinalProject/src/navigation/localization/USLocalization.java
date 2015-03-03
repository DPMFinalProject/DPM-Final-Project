/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	USLocalization.java
 *	Created On:	Feb 26, 2015
 */
package navigation.localization;

import sensors.FilteredSensor;
import navigation.Driver;
import navigation.Navigation;
import navigation.odometry.Odometer;

/**
 * Performs localization using the ultrasonic sensor.
 * @author Oleg
 */
public class USLocalization extends Localization {
	
	public USLocalization(Odometer odo, Driver driver, Navigation nav) {
		super(odo, driver, nav);
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
