/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	BangBangAvoider.java
 *	Created On:	Mar 1, 2015
 */
package navigation.avoidance;

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
	private int LIVE_LOCK_MAX = 5;

	private final int TURN_RADIUS = 30;	// drifting radius in cm
	private final int TURN_AWAY_ANGLE = 10;	// turning angle for going away from wall.
	
	private int BANGBANG_PERIOD = 500;	// minimal delay between two consecutive bang-bang avoidance calls

	private int AVOIDED_ANGLE_RANGE = 50;
	
	public BangBangAvoider(Odometer odo) {
		super(Direction.LEFT, odo);
		BAND_WIDTH = 6;
		BAND_CENTER = 22;
		detector = ObstacleDetection.getObstacleDetection();
	}
	
	@Override
	public void avoid() {
		
		initialOrientation = odo.getTheta();
		
		Driver.stop();
		
		if(wallDirection == Direction.FWD) {
			wallDirection = Direction.RIGHT;
		}
		
		Driver.turn(wallDirection.opposite(), 70);	// need a var name
		
		while(!hasAvoided()) {
			bangBang(wallDirection);
			pause(BANGBANG_PERIOD);
			
			if (detector.wallDistance(wallDirection.opposite()) < BAND_WIDTH) {
				bangBang(wallDirection.opposite());
				pause(BANGBANG_PERIOD);
			}
			
			if(LIVE_LOCK_COUNT >= LIVE_LOCK_MAX) {
				Driver.move(-20);
				break;
			}
		}
		
		Driver.stop();
	}
	
	private void bangBang(Direction direction) {
		double error = BAND_CENTER - detector.wallDistance(direction);
		
		if (Math.abs(error) < BAND_WIDTH)	{
			LIVE_LOCK_COUNT = 0;
			Driver.setDrifting(false);
			Driver.move(Direction.FWD);
		}
		else if (error < 0) {
			LIVE_LOCK_COUNT = 0;
			Driver.setDrifting(true);
			Driver.drift(direction, TURN_RADIUS);
		}
		else {
			Driver.turn(direction.opposite(), TURN_AWAY_ANGLE);
			LIVE_LOCK_COUNT++;
		}
		
		Driver.setDrifting(false);
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