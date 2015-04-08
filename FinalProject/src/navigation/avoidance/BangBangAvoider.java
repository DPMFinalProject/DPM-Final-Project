/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	BangBangAvoider.java
 *	Created On:	Mar 1, 2015
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
 * An avoider based on bang-bang control theory
 * when avoid is called, the robot turns 90 degrees away from the detected obstacle
 * then follows and and goes around the obstacle until the robot returns to its original
 * orientation -90
 * @author Auguste
 */
public class BangBangAvoider extends ObstacleAvoidance {
	
	private double initialOrientation;
	private ObstacleDetection detector;
	
	private int LIVE_LOCK_COUNT = 0;
	private final int LIVE_LOCK_MAX = 5;
	private Direction lastDirection = null;

	private final int DRIFT_RADIUS = 25;	// drifting radius in cm
	private int DRIFT_COUNT = 0;
	private final int DRIFT_MAX = 100;
	
	private final int TURN_AWAY_ANGLE = 30;	// turning angle for going away from wall.

	private boolean turningAway = false;
	
	private final int BANGBANG_PERIOD = 20;	// minimal delay between two consecutive bang-bang avoidance calls

	private final int AVOIDED_ANGLE_RANGE = 65;
	
	public BangBangAvoider(Direction wallDirection, Odometer odo) {
		super(wallDirection, odo);
		BAND_WIDTH = 6;
		BAND_CENTER = 22;
		
		detector = ObstacleDetection.getObstacleDetection();
	}
	
	/**
	 * Take care of avoiding any obstacle detected by the US Sensors.
	 * It will move the robot based on where the obstacles are detected,
	 * even during avoidance.
	 */
	@Override
	public void avoid() {
		Driver.stop();
		
		initialOrientation = odo.getTheta();
		
		if(wallDirection == Direction.FWD) {
			wallDirection = Direction.RIGHT;
		}
		
		faceAwayFromWall(wallDirection);
		
		while(!hasAvoided()) {
			bangBang(wallDirection);
			pause(BANGBANG_PERIOD);
			
			if (detector.wallDistance(wallDirection.opposite()) < (BAND_CENTER - BAND_WIDTH)) {
				bangBang(wallDirection.opposite());
				pause(BANGBANG_PERIOD);
			}
			
			if(LIVE_LOCK_COUNT >= LIVE_LOCK_MAX) {
				if (!checkForFront()) {
					Driver.move(20);
				} else {
					Driver.move(-20);
					Driver.turn(wallDirection.opposite(), 90);
				}
				break;
			}
		}
		
		Driver.setDrifting(false);
		Driver.stop();
	}
	
	private void bangBang(Direction direction) {
		double error = BAND_CENTER - detector.wallDistance(direction);
		
		if (Math.abs(error) < BAND_WIDTH)	{
			LIVE_LOCK_COUNT = 0;
			DRIFT_COUNT = 0;
			turningAway = false;
			
			Driver.setDrifting(false);
			Driver.move(Direction.FWD);
		}
		else if (error < 0) {
			LIVE_LOCK_COUNT = 0;
			
			if (turningAway) {
				turningAway = false;
				Driver.turn(direction.opposite(), TURN_AWAY_ANGLE);
			}

			Driver.setDrifting(true);
			
			if (DRIFT_COUNT < DRIFT_MAX) {
				Driver.drift(direction, DRIFT_RADIUS);
			} else {
				Driver.drift(direction, DRIFT_RADIUS/2);
			}
			
			DRIFT_COUNT++;
		}
		else {
			DRIFT_COUNT = 0;
			turningAway = true;
			
			Driver.turn(direction.opposite(), TURN_AWAY_ANGLE);
			
			if(lastDirection == direction.opposite() || lastDirection == null) {
				lastDirection = direction;
				LIVE_LOCK_COUNT++;
			}
			else {
				LIVE_LOCK_COUNT = 0;
			}
		}
	}
	
	private boolean hasAvoided() {
		double endAngle = initialOrientation + wallDirection.getAngle();
		endAngle = (endAngle < 0) ? (endAngle % 360) + 360 : endAngle % 360;
		
		return isNear(endAngle , odo.getTheta(), AVOIDED_ANGLE_RANGE);
	}
	
	private void faceAwayFromWall(Direction direction) {
		Driver.turn(direction.opposite());
		while(detector.sideObstacle(direction)) {
			pause(BANGBANG_PERIOD);
		}
		Driver.stop();
	}
	
	private boolean checkForFront() {
		Driver.turn(Direction.RIGHT, 45);
		
		boolean frontObstacle = detector.isLeftObstacle();
		
		Driver.turn(Direction.LEFT, 45);
		
		return frontObstacle;
	}
	
//	public void setDirection(Direction direction) {
//		this.wallDirection = direction;
//	}
}