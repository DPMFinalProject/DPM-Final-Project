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
 *	It currently assumes two motors are used for the wheels and needs to be reviewed.
 */
public class Driver {
	private NXTRegulatedMotor leftMotor = Motor.A;
	private NXTRegulatedMotor rightMotor = Motor.B;
	
	// placeholders
	private static double radius = 2.1, width = 16.1;
	
	// Random values
	private int FWD_SPEED = 200;
	private int FWD_ACCEL = 200;
	private int TURN_SPEED = 100;
	
	public static enum Direction {
		LEFT, RIGHT, FWD, BACK
	}
	
	public Driver() {}
	
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
		
		setSpeed(FWD_SPEED);
		
		if (direction == Direction.FWD) {
			leftMotor.forward();
			rightMotor.forward();
		} else {
			leftMotor.backward();
			rightMotor.backward();
		}
	}
	
	/**
	 * 	Naively moves forward a given distance, no error checking with odometer.
	 * @param distance	The distance by which to move, in cm
	 */
	public void move(double distance) {
		setSpeed(FWD_SPEED);
		
		leftMotor.rotate(convertDistance(radius, distance), true);
		rightMotor.rotate(convertDistance(radius, distance), false);
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
		
		stop();	// Interrupt all previous movement
		setSpeed(TURN_SPEED);
		
		if (direction == Direction.LEFT) {
			leftMotor.backward();
			rightMotor.forward();
		} else {
			rightMotor.backward();
			leftMotor.forward();
		}
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
		
		stop();	// Interrupt all previous movement
		setSpeed(TURN_SPEED);
		
		if (direction == Direction.LEFT) {
			leftMotor.rotate(-convertAngle(radius, width, angle), true);
			rightMotor.rotate(convertAngle(radius, width, angle), false);
		} else {
			leftMotor.rotate(convertAngle(radius, width, angle), true);
			rightMotor.rotate(-convertAngle(radius, width, angle), false);
		}
	}
	
	/**
	 * 	Stops any movement.
	 */
	public void stop() {
		leftMotor.flt();
		rightMotor.flt();
		leftMotor.stop();
		rightMotor.stop();
	}
	
	private void setSpeed(int speed) {
		leftMotor.setSpeed(speed);
		rightMotor.setSpeed(speed);
	}
	
	// Utility methods provided in lab 2
	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}
}
