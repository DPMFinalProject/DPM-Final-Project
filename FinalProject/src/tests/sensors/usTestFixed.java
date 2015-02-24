/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	NewTest.java
 *	Created On:	Feb 24, 2015
 */
package tests.sensors;


import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.RConsole;
import tests.TestCase;

/**
 * @author Mc Greggys
 *
 */
public class usTestFixed extends TestCase {

	/* (non-Javadoc)
	 * @see tests.TestCase#runTest()
	 */
	@Override
	public void runTest() {
		NXTRegulatedMotor motorA =Motor.A, motorB=Motor.B;
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
		
		
		for(int i=0; i<61;i++){
		System.out.println(us.getDistance());
		try {	Thread.sleep(100);	} catch (InterruptedException e) {}
		}
		Sound.beep();
	}

}
