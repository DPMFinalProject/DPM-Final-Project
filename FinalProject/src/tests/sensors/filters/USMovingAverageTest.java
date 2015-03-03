/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	USMovingAverage.java
 *	Created On:	Feb 27, 2015
 */
package tests.sensors.filters;

import lejos.nxt.SensorPort;
import sensors.FilteredColorSensor;
import sensors.FilteredSensor;
import sensors.FilteredUltrasonicSensor;
import sensors.filters.AveragingFilter;
import sensors.filters.DifferentialFilter;
import sensors.filters.OutlierFilter;
import tests.TestCase;

/**
 * This test applies the moving average filter to output of the ultrasonic sensor
 * to see if the class logic works correctly.
 * @author Oleg
 */
public class USMovingAverageTest extends TestCase {

	/**
	 * @see tests.TestCase#runTest()
	 */
	@Override
	public void runTest() {
		FilteredSensor sensor = new FilteredColorSensor(SensorPort.S1, new DifferentialFilter(4));
		
		while (true) {
			String signal = "" + sensor.getFilteredData();
			System.out.println(signal);
		}
	}

}
