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

	private final int DRIFT_RADIUS = 30;	// drifting radius in cm
	private final int TURN_AWAY_ANGLE = 15;	// turning angle for going away from wall.
	private final int INITIAL_TURN_AWAY_ANGLE = 70;
	private boolean turningAway = false;
	
	private final int BANGBANG_PERIOD = 500;	// minimal delay between two consecutive bang-bang avoidance calls

	private final int AVOIDED_ANGLE_RANGE = 50;
	
	public BangBangAvoider(Odometer odo, Direction wallDirection) {
		super(Direction.LEFT, odo);
		BAND_WIDTH = 6;
		BAND_CENTER = 22;
		this.wallDirection = wallDirection;
		
		detector = ObstacleDetection.getObstacleDetection();
	}
	
	/**
	 * Take care of avoiding any obstacle detected by the US Sensors.
	 * It will move the robot based on where the obstacles are detected,
	 * even during avoidance.
	 */
	@Override
	public void avoid() {
		Sound.beep();
		Driver.stop();
		
		initialOrientation = odo.getTheta();
		
		if(wallDirection == Direction.FWD) {
			wallDirection = Direction.RIGHT;
		}
		
		Driver.turn(wallDirection.opposite(), INITIAL_TURN_AWAY_ANGLE);
		
		while(!hasAvoided()) {
			bangBang(wallDirection);
			pause(BANGBANG_PERIOD);
			
			if (detector.wallDistance(wallDirection.opposite()) < (BAND_CENTER - BAND_WIDTH)) {
				bangBang(wallDirection.opposite());
				pause(BANGBANG_PERIOD);
			}
			
			if(LIVE_LOCK_COUNT >= LIVE_LOCK_MAX) {
				Driver.move(-BAND_CENTER);
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
			turningAway = false;
			
			Driver.setDrifting(false);
			Driver.move(Direction.FWD);
		}
		else if (error < 0) {
			LIVE_LOCK_COUNT = 0;
			
			if(turningAway) {
				turningAway = false;
				Driver.turn(direction.opposite(), TURN_AWAY_ANGLE);
			}

			Driver.setDrifting(true);
			Driver.drift(direction, DRIFT_RADIUS);
		}
		else {
			turningAway = true;
			
			Driver.turn(direction.opposite(), TURN_AWAY_ANGLE);
			
			if(lastDirection == direction.opposite() || lastDirection == null) {
				lastDirection = direction;
				LIVE_LOCK_COUNT++;
			}
		}
	}
	
	private boolean hasAvoided() {
		double endAngle = initialOrientation + wallDirection.getAngle();
		endAngle = (endAngle < 0) ? (endAngle % 360) + 360 : endAngle % 360;
		
		
		return isNear(endAngle , odo.getTheta(), AVOIDED_ANGLE_RANGE);
	}
	
//	private boolean checkForFront() {
//		Driver.turn(Direction.RIGHT, 45);
//		
//		boolean frontObstacle = detector.isLeftObstacle();
//		
//		Driver.turn(Direction.LEFT, 45);
//		
//		return frontObstacle;
//	}
	
	public void setDirection(Direction direction) {
		this.wallDirection = direction;
	}
}
