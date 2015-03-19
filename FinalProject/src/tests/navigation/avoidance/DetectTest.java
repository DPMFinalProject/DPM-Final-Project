/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	DetectTest.java
 *	Created On:	Mar 4, 2015
 */
package tests.navigation.avoidance;

import lejos.nxt.Sound;
import sensors.managers.ObstacleDetection;
import tests.TestCase;
import static util.Utilities.pause;

/**
 * 	Tests the quality of dual sensor obstacle detection.
 * @author Oleg
 */
public class DetectTest extends TestCase {

	/**
	 * @see tests.TestCase#runTest()
	 */
	@Override
	public void runTest() {
		ObstacleDetection detection = ObstacleDetection.getObstacleDetection();
		
		(new Thread(detection)).start();
		
		detection.setRunning(true);
		
		/*while(true) {
			System.out.println(detection.isLeftObstacle() + "," + detection.isRightObstacle() + "," + detection.isFrontObstacle() +
					detection.leftDistance() + "," + detection.rightDistance() + "," + detection.frontDistance());
		}*/
		
		while(!detection.isFrontObstacle()) {
			pause(30);
		}
		Sound.beep();
	}
}
