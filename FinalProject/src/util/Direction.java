/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Direction.java
 *	Created On:	Feb 28, 2015
 */
package util;

/**
 * 	Enum representing the four commonly needed directions for usage in actuating classes.
 * @author Oleg
 */
public enum Direction {
	
	LEFT (-90),
	RIGHT (90),
	FWD (0),
	BACK (180);
	
	private Direction(double angle) {
		this.angle = angle;
	}
	
	private final double angle;
	
	public double getAngle() {
		return angle;
	}
	
	public static Direction opposite(Direction direction) {
		switch(direction) {
		case LEFT:
			return Direction.RIGHT;
		case RIGHT:
			return Direction.LEFT;
		case FWD:
			return Direction.BACK;
		case BACK:
			return Direction.FWD;
		default:
			return null;
		}
	}
}
