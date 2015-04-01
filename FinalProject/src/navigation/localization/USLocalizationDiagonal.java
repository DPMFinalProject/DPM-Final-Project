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
import static util.Utilities.pause;

/**
 * 	Less accurate but faster localization that just moves the robot into the first square.
 * 	Not yet completed
 * @author Oleg
 */
public class USLocalizationDiagonal extends USLocalization {

	public USLocalizationDiagonal(Odometer odo, Navigation nav) {
		super(odo, nav);
	}

	/**
	 * 
	 * @see navigation.localization.Localization.java
	 */
	@Override
	public void doLocalization(double x, double y, double theta) {
		double xPos, yPos;
		
		if(!obstacleDetection.isFrontObstacle())
			faceWall();
		
		yPos = obtainYPosition(false);
		xPos = obtainXPosition(false);
		
		odo.setX(xPos);
		odo.setY(yPos);
		odo.setTheta(270);
		
		nav.travelTo(-10, -10, 0, false);
	}
}
