/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	LSFilterTest.java
 *	Created On:	Feb 28, 2015
 */
package tests.sensors.filters;

import navigation.Driver;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import sensors.FilteredColorSensor;
import sensors.FilteredSensor;
import sensors.filters.*;
import tests.TestCase;
import util.Direction;
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
		FilteredSensor cs= new FilteredColorSensor(SensorPort.S1, new DifferentialFilter(2));
				
		Driver driver = new Driver();
		//driver.move(Direction.FWD);
		driver.turn(Direction.LEFT, 360, true);
		
	
		while (driver.isMoving()) { 
			System.out.println(cs.getFilteredData());
			try {	Thread.sleep(100);	} catch (InterruptedException e) {}
		}
	}

}
