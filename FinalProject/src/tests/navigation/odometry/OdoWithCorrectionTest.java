/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	OdoWithCorrectionTest.java
 *	Created On:	Mar 3, 2015
 */
package tests.navigation.odometry;

import navigation.Driver;
import navigation.Navigation;
import navigation.odometry.Odometer;
import navigation.odometry.correction.CorrectionLightSensorSS;
import tests.TestCase;
import util.Direction;
import util.Measurements;
import util.Paths;
import util.OdometryDisplay;
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
			
		//slalom(1,5000);
		
		raysNavigator();
	}
	
	
	private void raysNavigator() {
		nav.travelTo(x, y);
		nav.travelTo(x, y);
		nav.travelTo(x, y);
		nav.travelTo(x, y);
		
	}

	public void slalom(int turns,int pause) {
		Driver.turn(Direction.RIGHT, 90);
		oneSlalomTurn(turns,pause );
	}
	
	private void oneSlalomTurn(int turns, int pause){
		for(int i=0; i<turns; i++){
			Driver.move(Measurements.TILE/2);
				correct.stall();
			Driver.turn(Direction.LEFT, 90);
			pause(pause);
				correct.resume();
			Driver.move(Measurements.TILE/2);
				correct.stall();
			Driver.turn(Direction.LEFT, 90);
			pause(pause);
				correct.resume();
			Driver.move(Measurements.TILE);
				correct.stall();
			Driver.turn(Direction.RIGHT, 90);
			pause(pause);
				correct.resume();
			Driver.move(Measurements.TILE/2);
				correct.stall();
			Driver.turn(Direction.RIGHT, 90);
			pause(pause);
				correct.resume();
			Driver.move(Measurements.TILE/2);
			pause(pause);
		}
	}
}
