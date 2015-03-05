/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	OdoTest.java
 *	Created On:	Mar 3, 2015
 */
package tests.navigation;

import lejos.nxt.Button;
import navigation.Driver;
import navigation.odometry.Odometer;
import navigation.odometry.correction.*;
import tests.TestCase;
import util.*;

/**
 * Odometer calibration and testing class
 * @author Oleg
 */
public class OdoWithCorrectionTest extends TestCase {

	/**
	 * @see tests.TestCase#runTest()
	 */
	Driver driver;
	Odometer odo;
	CorrectionLightSensorSS correct;
	OdometryDisplay display;
	GridManager gridMan;
	
	public OdoWithCorrectionTest() {
		
		driver = new Driver();
		odo = new Odometer(driver);
		correct = new CorrectionLightSensorSS(odo);
		display = new OdometryDisplay(odo);
		gridMan = new GridManager();
		
	}
	
	@Override
	public void runTest() {
		
//		(new Thread(odo)).start();
//		(new Thread(correct)).start();
//		(new Thread(display)).start();
		(new Thread(gridMan)).start();
		
		(new Thread() {public void run() {
				while(true) {
					System.out.println(gridMan.isOnLine());
					pause(20
							);
				}
			}
		}).start();
		
		driveSquare();
		
		Button.waitForAnyPress();
	}
	
	public void driveSquare()
	{
		driver.move(60, false);
		driver.turn(Direction.RIGHT,90);
		driver.move(60, false);
		driver.turn(Direction.RIGHT,90);
		driver.move(60, false);
		driver.turn(Direction.RIGHT,90);
		driver.move(60, false);
		driver.turn(Direction.RIGHT,90);
	}
	
	private void pause(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {}
	}
}
