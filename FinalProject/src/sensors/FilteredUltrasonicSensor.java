/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	FilteredUltrasonicSensor.java
 *	Created On:	Feb 20, 2015
 */
package sensors;

import sensors.filters.Filter;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

/**
 * Wrapper around the LeJOS ultrasonic class adding filtering capabilities.
 * @author Oleg
 */
public class FilteredUltrasonicSensor extends FilteredSensor {
	UltrasonicSensor sensor;
	
	public FilteredUltrasonicSensor(SensorPort port, Filter... filters) {
		this.filters = filters;
		sensor = new UltrasonicSensor(port);
	}

	@Override
	public double getFilteredData() {
		double distance = sensor.getDistance();
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
