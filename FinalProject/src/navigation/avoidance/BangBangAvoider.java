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
	
	public BangBangAvoider(Direction wallDirection, Odometer odo) {
		super(wallDirection, odo);
		BAND_WIDTH = 8;
		BAND_CENTER = 18;
		detector = ObstacleDetection.getObstacleDetection();
	}
	
	@Override
	public void avoid() {
		
		initialOrientation = odo.getTheta();
		
		Driver.stop();
		
		if(wallDirection == Direction.FWD) {
			wallDirection = Direction.LEFT;
		}
		
		Driver.turn(wallDirection.opposite(), 90);
		
		while(!hasAvoided()) {
			bangBang();
			
			if(detector.wallDistance(wallDirection.opposite()) < 30) {
				Driver.drift(wallDirection);
			}
			
			pause(20);
		}
		
		Driver.stop();
	}
	
	private void bangBang() {
		double error = BAND_CENTER - detector.wallDistance(wallDirection);
		
		if (Math.abs(error)<BAND_WIDTH)	{
			Driver.move(Direction.FWD);
		}
		else if (error < 0) {
			Driver.drift(wallDirection);
		}
		else {
			Driver.drift(wallDirection.opposite());
		}
	}
	
	private boolean hasAvoided() {
		
		double endAngle = initialOrientation + wallDirection.getAngle();
		endAngle = (endAngle < 0) ? (endAngle % 360) + 360 : endAngle % 360;
		
		if (isNear(endAngle , odo.getTheta(), 30)) {
			return true;
		}
		return false;
	}
}