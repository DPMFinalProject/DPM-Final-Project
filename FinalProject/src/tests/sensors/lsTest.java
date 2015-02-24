/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	NewTest.java
 *	Created On:	Feb 24, 2015
 */
package tests.sensors;

import lejos.nxt.ColorSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.RConsole;
import tests.TestCase;

/**
 * @author Mc Greggys
 *
 */
public class lsTest extends TestCase {

	/* (non-Javadoc)
	 * @see tests.TestCase#runTest()
	 */
	@Override
	public void runTest() {
		NXTRegulatedMotor motorA =Motor.A, motorB=Motor.B;
		ColorSensor ls = new ColorSensor(SensorPort.S1);
		ls.setFloodlight(false);
		
		motorA.setSpeed(100);
		motorB.setSpeed(100);
		motorA.rotate(360*2, true);
		motorB.rotate(360*2, true);
		
		while(motorB.isMoving() || motorA.isMoving()){
		System.out.println(ls.getLightValue());
		try {	Thread.sleep(100);	} catch (InterruptedException e) {}
		}
	}

}
