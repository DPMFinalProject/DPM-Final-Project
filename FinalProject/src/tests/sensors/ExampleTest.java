/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	ExampleTest.java
 *	Created On:	Feb 20, 2015
 */
package tests.sensors;

import lejos.nxt.SensorPort;
import sensors.FilteredUltrasonicSensor;
import sensors.filters.ExampleFilter;
import tests.TestCase;

/**
 * @author Oleg
 *	Example Test: Retrieves ultrasonic sensor values with all 255 values removed.
 * 
 */
public class ExampleTest extends TestCase {

	@Override
	public void runTest() {
		FilteredUltrasonicSensor sensor = new FilteredUltrasonicSensor(SensorPort.S1, new ExampleFilter());
		while (true) {
			System.out.println(sensor.getFilteredData());
		}
	}

}
