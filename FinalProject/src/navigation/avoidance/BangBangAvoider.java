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
import util.Measurements;
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
	
	private int chargeCount;
	
	public BangBangAvoider(Direction wallDirection, Odometer odo) {
		super(wallDirection, odo);
		BAND_WIDTH = 8;
		BAND_CENTER = 18;
		detector = ObstacleDetection.getObstacleDetection();
	}
	
	@Override
	public void avoid() {
		
		/*if (!checkForFront()) {
			Driver.move(Measurements.TILE / 2);
			return;
		}*/
		
		initialOrientation = odo.getTheta();
		
		Driver.stop();
		
		if(wallDirection == Direction.FWD) {
			wallDirection = Direction.RIGHT;
		}
		
		Driver.turn(wallDirection.opposite(), 90);
		
		chargeCount = 0;
		while(!hasAvoided()) {
			bangBang(wallDirection);
			pause(500);
			
			if (detector.wallDistance(wallDirection.opposite()) < BAND_WIDTH) {
				bangBang(wallDirection.opposite());
				pause(500);
			}
			
//			if(detector.wallDistance(wallDirection) > 100) {
//				Driver.move(23);
//				Driver.turn(wallDirection, 90);
//				chargeCount++;
//			}
		
		}
		
		Driver.stop();
	}
	
	private void bangBang(Direction direction) {
		double error = BAND_CENTER - detector.wallDistance(direction);
		
		if (Math.abs(error)<BAND_WIDTH)	{
			Driver.move(Direction.FWD);
		}
		else if (error < 0) {
			Driver.drift(direction);
			//Driver.move(Direction.FWD);
			//pause(1000);
			//Driver.turn(direction, 10);
		}
		else {
			//Driver.drift(direction.opposite());
			Driver.turn(direction.opposite(), 10);
			//Driver.move(Direction.FWD);
		}
	}
	
	private boolean hasAvoided() {
		double endAngle = initialOrientation + wallDirection.getAngle();
		endAngle = (endAngle < 0) ? (endAngle % 360) + 360 : endAngle % 360;
		
		if (isNear(endAngle , odo.getTheta(), 50)) {
			return true;
		}
		return false;
	}
	
	private boolean checkForFront() {
		Driver.turn(Direction.RIGHT, 45);
		
		boolean frontObstacle = detector.isLeftObstacle();
		
		Driver.turn(Direction.LEFT, 45);
		
		return frontObstacle;
	}
}