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
 * @author Oleg
 *	Wrapper around the LeJOS ultrasonic class adding filtering capabilities.
 */
public class FilteredUltrasonicSensor extends FilteredSensor {
	UltrasonicSensor sensor;
	
	public FilteredUltrasonicSensor(SensorPort port, Filter filter) {
		this.filter = filter;
		sensor = new UltrasonicSensor(port);
	}

	@Override
	public double getFilteredData() {
		double distance = sensor.getDistance();
		
		if (filter == null) {
			return distance;
		} else {
			return filter.filter(distance);
		}
	}
}
