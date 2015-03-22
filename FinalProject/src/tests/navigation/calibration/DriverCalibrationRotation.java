/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Rotation Calibration
 *	Created On:	Mar 22, 2015
 */
package tests.navigation.calibration;

import navigation.Driver;
import tests.TestCase;
import util.Direction;
import util.Paths;
import util.Measurements;
/**
 * 	To calibrate the width between the wheels 
 * 
 * @author Michael
 */
public class DriverCalibrationRotation extends TestCase {

	/**
	 * @see tests.TestCase#runTest()
	 */
	@Override
	public void runTest() {
		
		// 2 tiles aka 60.96
		Driver.turn(Direction.RIGHT, 360);
		

	}
}