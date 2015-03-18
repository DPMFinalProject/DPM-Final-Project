/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	LSFLMoving.java
 *	Created On:	Feb 24, 2015
 */
package tests.sensors;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import sensors.FilteredColorSensor;
import sensors.FilteredSensor;
import sensors.filters.DifferentialFilter;
import tests.TestCase;

/**A simple test consisting of a robot moving at a steady speed
 * and recording the light values as it moves forward. 
 * In this version, the light sensor flood light is ON.
 * 
 * Test Procedure:
 * In a “normal” light environment, make to robot move at a constant speed,
 * crossing different colors and collect the light value outputted by the sensor.
 * Repeat this experiment 3x at different height.
 * 
 * Repeat the experiment in a low light environment by covering the robot 
 * with a cloth. Repeat this experiment 6x, with and without flood light.
 * 
 * Repeat the experiment in a bright environment by shining the sensor area 
 * with a flashlight. Repeat this experiment 6x at different height from the ground.
 *
 * @author Gregory Brookes
 */
public class LSFLMoving extends TestCase {

	/* (non-Javadoc)
	 * @see tests.TestCase#runTest()
	 */
	@Override
	public void runTest() {
		NXTRegulatedMotor motorA =Motor.A, motorB=Motor.B;
		FilteredSensor ls = new FilteredColorSensor(SensorPort.S1, new DifferentialFilter(2));
	
		
		//drive the robot
		motorA.setSpeed(100);
		motorB.setSpeed(100);
		motorA.rotate(360*2, true);
		motorB.rotate(360*2, true);
		
		//get light measurements and print them to the console
		while(motorB.isMoving() || motorA.isMoving()) {
			System.out.println(ls.getFilteredData());
			try {	Thread.sleep(50);	} catch (InterruptedException e) {}
		}
	}

}
