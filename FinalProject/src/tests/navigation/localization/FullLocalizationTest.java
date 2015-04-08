/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	FullLocalizationTest.java
 *	Created On:	Apr 2, 2015
 */
package tests.navigation.localization;

import lejos.nxt.Sound;
import navigation.Navigation;
import navigation.localization.FullLocalization;
import navigation.localization.Localization;
import navigation.odometry.Odometer;
import navigation.odometry.correction.CorrectionLightSensorSS;
import sensors.managers.ObstacleDetection;
import static util.Utilities.pause;
import tests.TestCase;

/**
 * Tests the total localization using both sensors.
 * @author Oleg
 */
public class FullLocalizationTest extends TestCase {

	@Override
	public void runTest() {
		final Odometer odo = new Odometer();
		Navigation nav = new Navigation(odo);
		Localization localization = new FullLocalization(odo, nav);
		final ObstacleDetection detection = ObstacleDetection.getObstacleDetection(); 
		
		(new Thread(odo)).start();
//		(new Thread() {
//			public void run() {
//				while(true) {
//					if (detection.isFrontObstacle() || detection.isRightObstacle() || detection.isLeftObstacle()) {
//						Sound.beep();
//					}
//					pause(100);
//				}
//			}
//		}).start();
		
		CorrectionLightSensorSS correction = new CorrectionLightSensorSS(odo);
		(new Thread(correction)).start();
		
		localization.doLocalization(0, 0, 0);
		nav.travelToInTiles(1, 6, true);
		nav.travelTo(0, 0, true);
		localization.doLocalization(0, 0, 0);
	}

}
