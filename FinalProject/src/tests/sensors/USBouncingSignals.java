/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	ExampleTest.java
 *	Created On:	Feb 20, 2015
 */
package tests.sensors;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.RConsole;
import sensors.FilteredUltrasonicSensor;
import sensors.filters.ExampleFilter;
import tests.TestCase;

/**A simple test consisting of a rotating ultrasonic sensor
 * that prints its measurements of an obstacle at different angles 
 * to the console.
 * 
 * Test procedure:
 * Position the ultrasonic sensor on a motor capable of rotating 180 degree. 
 * Then put an obstacle 30 cm in front of it and take measurements as the 
 * ultrasonic sensor rotates. Repeat this experiment with the obstacle placed 
 * at 90, 60, 45, and 30 degrees from the sensor.Repeat this last experiment 
 * with the ultrasonic sensor facing the edge of the obstacle.
 *
 * @author Gregory Brookes
 *	
 */
public class USBouncingSignals extends TestCase {

	@Override
	public void runTest() {
		NXTRegulatedMotor motorA =Motor.A;
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
		RConsole.openUSB(10000);
		
		//drive the motor
		motorA.setSpeed(75);
		motorA.rotate(180, true);
		
		//get distance measurements and print them to the console
		while(motorA.isMoving()){
		RConsole.println(us.getDistance()+"");;
		}
		
		motorA.rotate(-180, true);
	}

}
