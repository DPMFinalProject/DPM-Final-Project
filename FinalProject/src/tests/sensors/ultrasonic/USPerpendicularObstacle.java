/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	USPerpendicularObstacle.java
 *	Created On:	Feb 24, 2015
 */
package tests.sensors.ultrasonic;


import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.RConsole;
import tests.TestCase;

/**A simple test consisting of a robot moving at a steady speed
 * and recording its distance to an obstacle values as it moves forward.
 * 
 * Test Procedure:
 * Position the robot facing away to a wall and make to robot move at 
 * a constant speed, collecting distance value from the wall which he 
 * was initially facing. 
 * 
 * @author Gregory Brookes
 */
public class USPerpendicularObstacle extends TestCase {

	/* (non-Javadoc)
	 * @see tests.TestCase#runTest()
	 */
	@Override
	public void runTest() {
		NXTRegulatedMotor motorA =Motor.B,motorB=Motor.C;
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
		RConsole.openUSB(10000);

		//drive the robot
		motorA.setSpeed(200);
		motorB.setSpeed(200);
		motorA.rotate(-360*10, true);
		motorB.rotate(-360*10, true);

		//get distances measurements and print them to the console
		while(motorB.isMoving()|| motorB.isMoving()) {
			RConsole.println(us.getDistance()+"");
		}
		
	}

}
