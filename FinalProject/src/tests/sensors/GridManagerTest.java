/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	OdoTest.java
 *	Created On:	Mar 7, 2015
 */
package tests.sensors;

import navigation.Driver;
import sensors.managers.GridManager;
import tests.TestCase;
import util.*;

/**
 * Line detection test
 * @author Auguste
 */
public class GridManagerTest extends TestCase {

	/**
	 * @see tests.TestCase#runTest()
	 */
	Driver driver;
	GridManager gridMana;
	
	public GridManagerTest() {
		
		driver = new Driver();
		gridMana = GridManager.getGridManager();
	}
	
	@Override
	public void runTest() {
		
		(new Thread(gridMana)).start();
		(new Thread() {
			public void run() {
				while(true) {
					System.out.println(gridMana.lineDetected());
					pause(50	);
				}
			}
		}
		).start();
		
		driveSquare();
	}
	
	public void driveSquare()
	{
		driver.move(60, false);
		driver.turn(Direction.RIGHT, 90);
		driver.move(60, false);
		driver.turn(Direction.RIGHT, 90);
		driver.move(60, false);
		driver.turn(Direction.RIGHT, 90);
		driver.move(60, false);
		driver.turn(Direction.RIGHT, 90);
	}
	
	private void pause(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {}
	}
}
