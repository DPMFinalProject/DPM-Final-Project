/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	USLocalizationMin.java
 *	Created On:	Apr 1, 2015
 */
package navigation.localization;

import sensors.filters.AveragingFilter;
import sensors.filters.DifferentialFilter;
import sensors.managers.ObstacleDetection;
import util.Direction;
import util.Measurements;
import util.Utilities;
import navigation.Driver;
import navigation.Navigation;
import navigation.odometry.Odometer;

/**
 * Performs localization using the ultrasonic sensor.
 * @author Auguste
 */

public class USLocalizationMin extends USLocalization {
	
	protected ObstacleDetection obstacleDetection;
	
	DifferentialFilter dFilter = new DifferentialFilter(2);
	AveragingFilter avgFilter = new AveragingFilter(6);
	
	public USLocalizationMin(Odometer odo, Navigation nav) {
		super(odo, nav);
		
		obstacleDetection = ObstacleDetection.getObstacleDetection();
	}
	
	@Override
	public void doLocalization(double x, double y, double theta) {
		faceAwayFromWall(Direction.RIGHT);
		
		Driver.turn(Direction.RIGHT);
		Utilities.pause(1000);
		
		while(!isMinimumDistance()) {
			Utilities.pause(40);
		}
		
		Driver.stop();
		
		odo.setX(obstacleDetection.leftDistance() + SENSOR_OFFSET - Measurements.TILE);
		
		Driver.turn(Direction.RIGHT);
		Utilities.pause(1000);
		
		while(!isMinimumDistance()) {
			Utilities.pause(40);
		}
		
		Driver.stop();
		
		odo.setY(obstacleDetection.leftDistance() + SENSOR_OFFSET - Measurements.TILE);
	}
	
	public boolean isMinimumDistance() {
		return dFilter.filter(avgFilter.filter(obstacleDetection.leftDistance())) > 0;
	}
}
