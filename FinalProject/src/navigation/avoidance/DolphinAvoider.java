/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	NewBangBangAvoider.java
 *	Created On:	Apr 8, 2015
 */
package navigation.avoidance;

import lejos.nxt.Sound;
import navigation.Driver;
import navigation.odometry.Odometer;
import sensors.managers.ObstacleDetection;
import util.Direction;
import static util.Utilities.isNear;
import static util.Utilities.pause;

/**
 * Updated BangBang Avoidance to reflect issues with the old one.
 * @author Oleg
 */
public class DolphinAvoider extends ObstacleAvoidance {
	private final ObstacleDetection detector;
	
	private final int DETECTION_RATE = 20;
	
	private final int DRIFT_RADIUS = 25;
	private int DRIFT_COUNT = 0;
	private final int DRIFT_MAX = 100;
	
	private int LIVE_LOCK_COUNT = 0;
	private final int LIVE_LOCK_MAX = 5;
	private Direction lastDirection = null;
	
	private final int TURN_AWAY_ANGLE = 30;
	
	private final double initialOrientation;
	private final double AVOIDED_ANGLE_OFFSET = 20;
	private final double AVOIDED_ANGLE_RANGE = 65;
	
	public DolphinAvoider(Direction wallDirection, Odometer odo) {
		super(wallDirection, odo);
		
		BAND_WIDTH = 6;
		BAND_CENTER = 20;
		
		initialOrientation = odo.getTheta();
		
		detector = ObstacleDetection.getObstacleDetection();
	}

	/**
	 * @see navigation.avoidance.ObstacleAvoidance#avoid()
	 */
	@Override
	public void avoid() {
		faceAwayFromWall(wallDirection);
		Sound.twoBeeps();
		pause(1000);
		while(!hasAvoided()) {
			dolphin(wallDirection);
			pause(DETECTION_RATE);
			if(LIVE_LOCK_COUNT >= LIVE_LOCK_MAX) {
				if (!checkForFront()) {
					Driver.move(20);
				} else {
					Driver.move(-20);
					Driver.turn(wallDirection.opposite(), 90);
				}
				break;
			}
			if (detector.sideDistance(wallDirection.opposite()) < BAND_CENTER - BAND_WIDTH) {
				dolphin(wallDirection.opposite());
				pause(DETECTION_RATE);
			}
		}
		Driver.stop();
	}
	
	private void dolphin(Direction dir) {

		if (detector.sideDistance(dir) < BAND_CENTER) {
			if (dir.opposite() == lastDirection || lastDirection == null) {
				LIVE_LOCK_COUNT++;
				lastDirection = dir;
			} else {
				LIVE_LOCK_COUNT = 0;
			}
			DRIFT_COUNT = 0;
			Driver.turn(dir.opposite(), TURN_AWAY_ANGLE);
		} else {
			LIVE_LOCK_COUNT = 0;
			if (DRIFT_COUNT < DRIFT_MAX) {
				Driver.drift(dir, DRIFT_RADIUS);
			} else {
				Driver.drift(dir, DRIFT_RADIUS/2);
			}
			DRIFT_COUNT++;
		}
		
	}

	private void faceAwayFromWall(Direction direction) {
		Driver.stop();
		Driver.turn(direction.opposite());
		while(detector.sideObstacle(direction)) {
			pause(DETECTION_RATE);
		}
		Driver.stop();
	}
	
	private boolean hasAvoided() {
		double endAngle = initialOrientation + wallDirection.getAngle();
		endAngle = (endAngle < 0) ? (endAngle % 360) + 360 : endAngle % 360;
		
		return isNear(endAngle , odo.getTheta(), AVOIDED_ANGLE_RANGE);
	}
	
	private boolean checkForFront() {
		Driver.turn(Direction.RIGHT, 45);
		boolean frontObstacle = detector.isLeftObstacle();
		Driver.turn(Direction.LEFT, 45);
		
		return frontObstacle;
	}
		
//	private boolean hasAvoided() {
//		double endAngleLeft = initialOrientation - AVOIDED_ANGLE_OFFSET;
//		double endAngleRight = initialOrientation + AVOIDED_ANGLE_OFFSET;
//		
//		endAngleLeft = (endAngleLeft < 0) ? (endAngleLeft % 360) + 360 : endAngleLeft % 360;
//		endAngleRight = (endAngleRight < 0) ? (endAngleRight % 360) + 360 : endAngleRight % 360;
//		
//		boolean hasAvoidedLeft = isNear(endAngleLeft , odo.getTheta(), AVOIDED_ANGLE_RANGE);
//		boolean hasAvoidedRight = isNear(endAngleRight , odo.getTheta(), AVOIDED_ANGLE_RANGE);
//		
//		return hasAvoidedLeft || true;
//	}
}
