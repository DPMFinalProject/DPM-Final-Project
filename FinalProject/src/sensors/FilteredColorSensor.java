/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	FilteredLightSensor.java
 *	Created On:	Feb 24, 2015
 */
package sensors;

import sensors.filters.Filter;
import lejos.nxt.ColorSensor;

/**
 * @author Oleg
 *	Wrapper around the LeJOS ultrasonic class adding filtering capabilities.
 */
public class FilteredColorSensor extends FilteredSensor {
	ColorSensor sensor;
	
	public FilteredColorSensor(Filter... filters) {
		this.filters = filters;
	}

	@Override
	public double getFilteredData() {
		double distance = sensor.getLightValue();
		double result;
		
		if (filters == null) {
			return distance;
		} else {
			result = distance;
			for (Filter f : filters) {
				result = f.filter(result);
			}
			return result;
		}
	}
}
