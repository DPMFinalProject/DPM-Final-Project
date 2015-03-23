/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	SensorID.java
 *	Created On:	Mar 7, 2015
 */
package util;

/**
 * 	Enum used to identify which sensor(s) detected something
 * @author Auguste
 */
public enum SensorID {
	LEFT, RIGHT, BOTH, NONE;
	
	public SensorID opposite() {
		return opposite(this);
	}
	
	public static SensorID opposite(SensorID ID) {
		switch(ID) {
		case LEFT:
			return SensorID.RIGHT;
		case RIGHT:
			return SensorID.LEFT;
		case BOTH:
			return SensorID.NONE;
		case NONE:
			return SensorID.BOTH;
		default:
			return null;
		}
	}
}