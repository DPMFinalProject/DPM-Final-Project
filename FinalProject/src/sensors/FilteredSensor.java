/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Sensor.java
 *	Created On:	Feb 19, 2015
 */
package sensors;

import sensors.filters.Filter;

/**
 * @author Oleg
 *
 */
public abstract class FilteredSensor {
	protected Filter filter;
	
	public abstract double getFilteredData();
}
