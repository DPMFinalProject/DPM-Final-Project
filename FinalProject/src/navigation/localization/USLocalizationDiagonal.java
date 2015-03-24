/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	USLocaizationDiagonal.java
 *	Created On:	Mar 24, 2015
 */
package navigation.localization;

import util.Direction;
import navigation.Driver;
import navigation.Navigation;
import navigation.odometry.Odometer;

/**
 * 	Less accurate but faster localization that just moves the robot into the first square.
 * @author Oleg
 */
public class USLocalizationDiagonal extends USLocalization {

	public USLocalizationDiagonal(Odometer odo, Navigation nav) {
		super(odo, nav);
	}

	@Override
	public void doLocalization() {
		if(!obstacleDetection.isFrontObstacle())
			faceWall();
		/* 
		 * Do multiple iterations of adjustments of the X and Y positions,
		 * the robot will converge onto (0, 0) provided SENSOR_OFFSET is calibrated
		 * correctly.
		 */
		double yPos = obtainYPosition(false);
		double xPos = obtainXPosition(false);
		double centerAngle = Math.toDegrees(Math.atan2(yPos, xPos)) - (SENSOR_VIEW_ANGLE + 10);
		
		Driver.turn(Direction.LEFT, centerAngle);
		Driver.move(Math.sqrt(yPos*yPos + xPos*xPos) + 10);
		Driver.turn(Direction.RIGHT, centerAngle + 60);
		
	}
	
}
