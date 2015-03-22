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
import util.Paths;
import util.Measurements;
/**
 * 	Used to verify driver capabilities.
 * 	e.g. drive straight, turn angle, stop smoothly...
 * 
 * @author Michael
 */
public class DriverCalibrationForward extends TestCase {

	/**
	 * @see tests.TestCase#runTest()
	 */
	@Override
	public void runTest() {
		
		// 2 tiles aka 60.96
		Driver.move(2*Measurements.TILE);
		

	}
}