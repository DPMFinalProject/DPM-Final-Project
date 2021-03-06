/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	FilteredColorSensor.java
 *	Created On:	Feb 24, 2015
 */
package sensors;

import sensors.filters.Filter;
import lejos.nxt.ColorSensor;
import lejos.nxt.SensorPort;

/**
 * Wrapper around the LeJOS ColorSensor class adding filtering capabilities.
 * @author Oleg
 */
public class FilteredColorSensor extends FilteredSensor {
	ColorSensor sensor;
	
	public FilteredColorSensor(SensorPort port, Filter... filters) {
		super(filters);
		sensor = new ColorSensor(port);
		
			sensor.setFloodlight(true);
	}

	@Override
	public double getFilteredData() {
		double lightValue = sensor.getLightValue();
		//System.out.println("\t"+lightValue);
		
		return applyFilters(lightValue);
	}
}
