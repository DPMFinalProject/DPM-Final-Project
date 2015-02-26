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
 * Abstract class providing the definition of a LeJOS API sensor wrapper.
 * @author Oleg
 */
public abstract class FilteredSensor {
	protected Filter[] filters;
	
	public abstract double getFilteredData();
}
