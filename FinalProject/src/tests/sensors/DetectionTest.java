/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	DetectionTest.java
 *	Created On:	Apr 3, 2015
 */
package tests.sensors;

import sensors.managers.ObstacleDetection;
import static util.Utilities.pause;
import tests.TestCase;

/**
 * 	Prints out what the Obstacle Detection sees 
 * @author Oleg
 */
public class DetectionTest extends TestCase {

	@Override
	public void runTest() {
		ObstacleDetection detection = ObstacleDetection.getObstacleDetection();
		
		while(true) {
			System.out.println(detection.isFrontObstacle() + " " + 
					detection.leftDistance() + "," + detection.rightDistance());
			pause(50);
		}
	}

}
