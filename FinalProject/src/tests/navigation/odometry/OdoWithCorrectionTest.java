/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	OdoWithCorrectionTest.java
 *	Created On:	Mar 3, 2015
 */
package tests.navigation.odometry;

import navigation.Navigation;
import navigation.odometry.Odometer;
import navigation.odometry.correction.CorrectionLightSensorSS;
import tests.TestCase;
import util.Measurements;
import util.OdometryDisplay;
import util.Paths;
import static util.Utilities.pause;

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
	Navigation nav;
	
	public OdoWithCorrectionTest() {
		
		odo = new Odometer();
		correct = new CorrectionLightSensorSS(odo);
		display = new OdometryDisplay(odo);
		nav = new Navigation(odo);
		
	}
	
	@Override
	public void runTest() {
		
//		(new Thread(odo)).start();
//		(new Thread(correct)).start();
//		(new Thread(display)).start();
//		
//		(new Thread() {
//			public void run() {
//				while(true) {
//					System.out.println(odo.getX()+"\t"+odo.getY()+"\t"+odo.getTheta());
//					pause(500);
//				}
//			}
//		}).start();

		Paths.square(Measurements.TILE*2);
		
		new util.songs.Tetris().play();
	}
	
	
	private void raysSimplePath() {
		nav.travelTo(Measurements.TILE, 2*Measurements.TILE, false);
		System.out.println(odo.getX()+"\t"+odo.getY()+"\t"+odo.getTheta());
		nav.travelTo(-Measurements.TILE, 2*Measurements.TILE, false);
		System.out.println(odo.getX()+"\t"+odo.getY()+"\t"+odo.getTheta());
		nav.travelTo(-3*Measurements.TILE, 0, false);
		System.out.println(odo.getX()+"\t"+odo.getY()+"\t"+odo.getTheta());
		nav.travelTo(0, 0, 0, false);
		System.out.println(odo.getX()+"\t"+odo.getY()+"\t"+odo.getTheta());
	}
	
	private void raysComplexPath() {
																					printcoordinates();
		nav.travelTo(Measurements.TILE*2, 1*Measurements.TILE, false);				printcoordinates();
		nav.travelTo(Measurements.TILE*0, 1*Measurements.TILE, false);				printcoordinates();
		nav.travelTo(Measurements.TILE*2, 0*Measurements.TILE, false);				printcoordinates();
		nav.travelTo(Measurements.TILE*1, 2*Measurements.TILE, false);				printcoordinates();
		nav.travelTo(Measurements.TILE*0, 0*Measurements.TILE, false);				printcoordinates();
		nav.travelTo(Measurements.TILE*0, 1*Measurements.TILE, false);				printcoordinates();
		nav.travelTo(Measurements.TILE*1, 2*Measurements.TILE, false);				printcoordinates();
		nav.travelTo(Measurements.TILE*2, 1*Measurements.TILE, false);				printcoordinates();
		nav.travelTo(Measurements.TILE*2, 0*Measurements.TILE, false);				printcoordinates();
		nav.travelTo(Measurements.TILE*0, 0*Measurements.TILE, false);				printcoordinates();
		nav.travelTo(Measurements.TILE*2, 0*Measurements.TILE, false);				printcoordinates();
		nav.travelTo(Measurements.TILE*4, 2*Measurements.TILE, false);				printcoordinates();
		nav.travelTo(Measurements.TILE*6, 0*Measurements.TILE, false);				printcoordinates();
		nav.travelTo(Measurements.TILE*4, 0*Measurements.TILE, false);				printcoordinates();
		nav.travelTo(Measurements.TILE*5, 1*Measurements.TILE, false);				printcoordinates();
		nav.travelTo(Measurements.TILE*3, 1*Measurements.TILE, false);				printcoordinates();
		nav.travelTo(Measurements.TILE*4, 0*Measurements.TILE, false);				printcoordinates();
		nav.travelTo(Measurements.TILE*2, 0*Measurements.TILE, false);				printcoordinates();
	}
	
	private void printcoordinates(){
		System.out.println(odo.getX()+"\t"+odo.getY()+"\t"+odo.getTheta());
	}
}
