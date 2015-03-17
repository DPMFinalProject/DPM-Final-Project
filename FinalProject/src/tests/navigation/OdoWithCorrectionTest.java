/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	OdoWithCorrectionTest.java
 *	Created On:	Mar 3, 2015
 */
package tests.navigation;

import navigation.Driver;
import navigation.odometry.Odometer;
import navigation.odometry.correction.CorrectionLightSensorSS;
import tests.TestCase;
import util.Direction;
import util.OdometryDisplay;

/**
 * Odometry Correction test
 * @author Auguste
 */
public class OdoWithCorrectionTest extends TestCase {

	/**
	 * @see tests.TestCase#runTest()
	 */
	Odometer odo;
	CorrectionLightSensorSS correct;
	OdometryDisplay display;
	
	public OdoWithCorrectionTest() {
		
		odo = new Odometer();
		correct = new CorrectionLightSensorSS(odo);
		display = new OdometryDisplay(odo);
		
	}
	
	@Override
	public void runTest() {
		
		(new Thread(odo)).start();
		(new Thread(correct)).start();
		(new Thread(display)).start();
		
		(new Thread() {
			public void run() {
				while(true) {
					System.out.println(odo.getX()+"\t"+odo.getY()+"\t"+odo.getTheta());
					pause(500);
				}
			}
		}).start();
			
		//driveSquareIsh();
		driveCircle();
	}
	
	private void driveSquareIsh() {
		Driver.move(2*30.48, false);
		correct.stall();
		Driver.turn(Direction.RIGHT, 90);
		correct.resume();
		Driver.move(4*30.48, false);
		correct.stall();
		Driver.turn(Direction.RIGHT, 90);
		correct.resume();
		Driver.move(2*30.48, false);
		correct.stall();
		Driver.turn(Direction.RIGHT, 90);
		correct.resume();
		Driver.move(4*30.48, false);
		correct.stall();
		Driver.turn(Direction.RIGHT, 90);
		System.out.println("final X: "+odo.getX());
		System.out.println("final Y: "+odo.getY());
		System.out.println("final Theta: "+odo.getTheta());
	}
	
	private void driveCircle() {
		Driver.move(30.48, false);
		correct.stall();
		Driver.turn(Direction.RIGHT, 90);
		correct.resume();
		Driver.driveCircle(30.48);
	}
	
	private void pause(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {}
	}
}
