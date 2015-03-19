/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	ObstacleAvoidance.java
 *	Created On:	Feb 26, 2015
 */
package navigation.avoidance;

import util.Direction;
import navigation.odometry.Odometer;

/**
 * Provides the logic to bypass obstacles.
 * @author Oleg
 */
public abstract class ObstacleAvoidance {
	protected Odometer odo;
	
	protected int BAND_WIDTH, BAND_CENTER;
	protected Direction wallDirection;
	
	public ObstacleAvoidance(Direction wallDirection, Odometer odo) {
		this.wallDirection = wallDirection;
		this.odo = odo;
	}
	/**
	 *	Executes an obstacle avoidance maneuver.
	 */
	public abstract void avoid();
	
	public void setWallDirection(Direction direction) {
		wallDirection = direction;
	}
	
	public Direction towardsWall() {
		return wallDirection;
	}
	
	public Direction awayFromWall() {
		if (wallDirection == Direction.LEFT) {
			return Direction.RIGHT;
		} else {
			return Direction.LEFT;
		}
	}
}
