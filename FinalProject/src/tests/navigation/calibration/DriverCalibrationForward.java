/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Wheel size calibration
 *	Created On:	Mar 22, 2015
 */
package tests.navigation.calibration;

import navigation.Driver;
import tests.TestCase;
import util.Direction;
import util.Measurements;
import static util.Utilities.pause;
/**
 * To calibrate the wheel radius 
 * 
 * @author Michael
 */
public class DriverCalibrationForward extends TestCase {

	@Override
	public void runTest() {
		
		// 2 tiles aka 60.96
		Driver.move(2*Measurements.TILE);
		pause(2000);
		Driver.turn(Direction.RIGHT, 360);
	
	}
}