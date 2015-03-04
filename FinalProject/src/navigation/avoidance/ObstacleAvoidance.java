/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	ObstacleAvoidance.java
 *	Created On:	Feb 26, 2015
 */
package navigation.avoidance;

import navigation.Driver;
import navigation.odometry.Odometer;

/**
 * Provides the logic to bypass obstacles.
 * @author Oleg
 */
public abstract class ObstacleAvoidance {
	protected final Driver driver;
	protected final Odometer odo;
	
	public ObstacleAvoidance(Driver driver, Odometer odo) {
		this.driver = driver;
		this.odo = odo;
	}
	/**
	 *	Executes an obstacle avoidance maneuver.
	 */
	public abstract void avoid();
}
