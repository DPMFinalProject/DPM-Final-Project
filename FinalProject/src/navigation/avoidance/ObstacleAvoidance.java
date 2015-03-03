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

/**
 * Provides the logic to bypass obstacles.
 * @author Oleg
 */
public abstract class ObstacleAvoidance {
	Driver driver;
	/**
	 *	Executes an obstacle avoidance maneuver.
	 */
	public abstract void avoid();
}