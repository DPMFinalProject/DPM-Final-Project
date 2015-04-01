/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	FilteredSensor.java
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
	
	public FilteredSensor(Filter... filters) {
		this.filters = filters;
	}
	
	/**
	 * Returns data specific for the sensor used.
	 * @return	Filtered data, the nature of which is dependent on the sensor.
	 */
	public abstract double getFilteredData();
	
	/**
	 * Apply all the provided filters to the desired signal.
	 * @param signal
	 * @return result
	 */
	protected double applyFilters(double signal) {
		double result = signal;
		
		// Apply all filters in the order they were provided.
		if (filters != null) {
			for (Filter f : filters) {
				result = f.filter(result);
			}
		}
		
		return result;
	}
}
