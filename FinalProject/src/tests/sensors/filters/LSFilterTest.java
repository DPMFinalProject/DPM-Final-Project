/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	LSFilterTest.java
 *	Created On:	Feb 28, 2015
 */
package tests.sensors.filters;

import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import sensors.FilteredColorSensor;
import sensors.FilteredSensor;
import sensors.filters.*;
import tests.TestCase;
import util.MovingWindow;

/**
 *	This class is used to test various filtering behaviors with the light sensor
 * @author Oleg
 */
public class LSFilterTest extends TestCase {

	/** 
	 * @see tests.TestCase#runTest()
	 */
	@Override
	public void runTest() {
		FilteredSensor sensor = new FilteredColorSensor(SensorPort.S1, new DifferentialFilter(4));
		FilteredSensor averageSensor = new FilteredColorSensor(SensorPort.S1, new AveragingFilter(4));
		
		MovingWindow window = new MovingWindow(50);
		
		while (true) {
			double value = sensor.getFilteredData();
			window.add(value);
			String signal = "" + sensor.getFilteredData() + "," + window.stdDev(); 
			System.out.println(signal);
			if (Math.abs(value - averageSensor.getFilteredData()) > window.stdDev()*3) {
				Sound.beep();
			}
		}
	}

}
