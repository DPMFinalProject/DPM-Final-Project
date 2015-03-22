/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	USViewingAngles.java
 *	Created On:	Feb 24, 2015
 */
package tests.sensors;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.RConsole;
import tests.TestCase;

/**A simple test consisting of a fixed ultrasonic sensor
 * that prints its measurements to the console.
 * 
 * Test procedure:
 * Position the ultrasonic sensor facing nothing and slowly 
 * move obstacles along the x axis so it can obstruct the view
 * of the ultrasonic sensor. Record the position at which the 
 * sensor starts detecting the obstacle. Do the same both sides 
 * and at different distances.
 *
 * @author Gregory Brookes
 */
public class USViewingAngles extends TestCase {

	@Override
	public void runTest() {
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
		RConsole.openUSB(10000);
		
		//print distances to the console
		while(true) {
			RConsole.println(us.getDistance()+"");
		}
	}

}
