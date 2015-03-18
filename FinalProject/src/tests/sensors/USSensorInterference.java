/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	USSensorInterference.java
 *	Created On:	Feb 20, 2015
 */
package tests.sensors;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.RConsole;
import tests.TestCase;

/**A simple test consisting of two ultrasonic sensor
 * that prints its measurements of obstacles to the console.
 * 
 * Test procedure:
 * Using two ultrasonic sensors, both facing 45 degrees outwards 
 * the orientation of the brick, position obstacles in the front 
 * and in diagonal at different distances and in different 
 * obstacles combinations.
 * 
 * @author Gregory Brookes
 *	
 */
public class USSensorInterference extends TestCase {

	@Override
	public void runTest() {
		UltrasonicSensor us1 = new UltrasonicSensor(SensorPort.S1);
		UltrasonicSensor us2 = new UltrasonicSensor(SensorPort.S2);
		RConsole.openUSB(10000);

		//get distances measurements and print them to the console
		for (int i=0; i<61; i++) {
			RConsole.println(us1.getDistance()+"\t"+ us2.getDistance());
		}

	}

}
