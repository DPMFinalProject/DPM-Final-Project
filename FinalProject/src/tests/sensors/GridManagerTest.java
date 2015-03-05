/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	ExampleTest.java
 *	Created On:	Feb 20, 2015
 */
package tests.sensors;

import navigation.Driver;
import navigation.odometry.Odometer;
import lejos.nxt.SensorPort;
import sensors.FilteredColorSensor;
import sensors.filters.DifferentialFilter;
import sensors.filters.ExampleFilter;
import tests.TestCase;
import util.Direction;
import util.GridManager;

/**
 * @author Oleg
 *	Example Test: Retrieves ultrasonic sensor values with all 255 values removed.
 * 
 */
public class GridManagerTest extends TestCase {

	@Override
	public void runTest() {
		FilteredColorSensor sensor = new FilteredColorSensor(SensorPort.S1, new DifferentialFilter(2));
		Driver driver = new Driver();
		Odometer odo = new Odometer(driver);
		GridManager grid = new GridManager(sensor,3.5,odo);
		double[] pos = new double[3];
		
		
		(new Thread(odo)).start();
		//driver.move(20);
		
		while (true/*driver.isMoving()*/) {
			grid.getPosMidLineCrossing(pos);
			System.out.println("POSITION RETURNED:@=" + pos[0]+"\t"+(pos[1])+"\t"+pos[2]);
			
		}
	}

}
