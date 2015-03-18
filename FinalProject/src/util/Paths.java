/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Paths.java
 *	Created On:	Mar 17, 2015
 */
package util;

import navigation.Driver;

/**
 * 	defines paths which the robot can be made to do for testing purposes.
 * @author Auguste
 */
public class Paths {
	
	public static void square() {
		square(Direction.RIGHT, Measurements.TILE);
	}
	
	public static void square(double sideLength) {
		square(Direction.RIGHT, sideLength);
	}
	
	public static void square(Direction direction, double sideLength) {
		rectangle(direction, sideLength, sideLength);
	}
	
	public static void rectangle(double length, double width) {
		rectangle(Direction.RIGHT, length, width);
	}
	
	public static void rectangle(Direction direction, double length, double width) {
		Driver.move(length);
		Driver.turn(direction, 90);
		Driver.move(width);
		Driver.turn(direction, 90);
		Driver.move(length);
		Driver.turn(direction, 90);
		Driver.move(width);
		Driver.turn(direction, 90);
	}
	
	public static void circle(Direction direction, double radius) {
		Driver.driveCircle(direction, radius);
	}
}