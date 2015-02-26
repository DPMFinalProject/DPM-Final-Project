/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Driver.java
 *	Created On:	Feb 24, 2015
 */
package navigation;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;

/**
 * @author Oleg
 *	This class provides abstraction from the robot's driving functionality.
 */
public class Driver {
	private final int FWD_SPEED;
	private final int FWD_ACCEL;
	private final int TURN_SPEED;
	
	public static enum Direction {
		LEFT, RIGHT, FWD, BACK
	}
	
	public Driver() {
		this(100, 100, 100);
	}
	
	public Driver(int fwdSpeed, int fwdAccel, int turnSpeed) {
		FWD_SPEED = fwdSpeed;
		FWD_ACCEL = fwdAccel;
		TURN_SPEED = turnSpeed;
	}
	
	/**
	 * Moves continuously until stop() is called.
	 * @param direction	The direction in which to move: FWD or BACK
	 */
	public void move(Direction direction) {
		if (direction == Direction.LEFT || direction == Direction.RIGHT) {
			System.out.println("Cannot move " + direction + "\n");
			System.out.println("Must move forward or backward");
			return;
		}
		
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 	Naively moves forward a given distance, no error checking with odometer.
	 * @param distance	The distance by which to move, in cm
	 */
	public void move(double distance) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 	Turns continuously until stop() is called.
	 * @param direction The direction in which to turn: LEFT or RIGHT.
	 */
	public void turn(Direction direction) {
		if (direction == Direction.FWD || direction == Direction.BACK) {
			System.out.println("Cannot turn " + direction + "\n");
			System.out.println("Must turn left or right");
			return;
		}
		
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Naively turns in the provided direction, no error checking with odometer.
	 * @param direction	The direction in which to turn, LEFT or RIGHT
	 * @param angle The angle in which to turn, in degrees.
	 */
	public void turn(Direction direction, double angle) {
		if (direction == Direction.LEFT || direction == Direction.RIGHT) {
			System.out.println("Cannot turn " + direction + "\n");
			System.out.println("Must turn left or right");
			return;
		}
		
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 	Stops any movement.
	 */
	public void stop() {
		throw new UnsupportedOperationException();
	}
	
	private void setSpeed(int speed) {
		throw new UnsupportedOperationException();
	}
	
	// Utility methods provided in lab 2
	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}
}
