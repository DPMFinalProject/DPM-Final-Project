/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	OdoWithCorrectionTest.java
 *	Created On:	Mar 3, 2015
 */
package tests.navigation;

import navigation.odometry.Odometer;
import navigation.odometry.correction.CorrectionLightSensorSS;
import tests.TestCase;
import util.Paths;
import util.OdometryDisplay;
import static util.Pause.pause;

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
			
		Paths.square();
	}
}
