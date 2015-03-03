/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Driver.java
 *	Created On:	Feb 24, 2015
 */
package navigation;

import util.Direction;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Motor;

/**
 * @author Oleg
 *	This class provides abstraction from the robot's driving functionality.
 *	Obstacle evasion should be integrated in here and not in navigator. 
 */
public class Driver {
	private final int FWD_SPEED;
	private final int FWD_ACCEL;
	private final int TURN_SPEED;
	private final int DRIFT_FACTOR;
	private final double WHL_RADIUS;
	private final double WHL_SEPARATION;
	private final NXTRegulatedMotor leftMotor, rightMotor;
	
	public Driver() {
		this(100, 100, 100, 50, 2.5, 15, Motor.A, Motor.B);
	}
	
	public Driver(double wheelRadius, double wheelSeparation, 
			NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor) {
		this(100, 100, 100, 50, wheelRadius, wheelSeparation, leftMotor, rightMotor);
	}
	
	public Driver(int fwdSpeed, int fwdAccel, int turnSpeed,
			double wheelRadius, double wheelSeparation, 
			NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor) {
		
		this(fwdSpeed, fwdAccel, turnSpeed, 20, wheelRadius, wheelSeparation, leftMotor, rightMotor);
	}
	
	public Driver(int fwdSpeed, int fwdAccel, int turnSpeed, int driftFactor,
			double wheelRadius, double wheelSeparation, 
			NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor) {
		
		FWD_SPEED = fwdSpeed;
		FWD_ACCEL = fwdAccel;
		TURN_SPEED = turnSpeed;
		DRIFT_FACTOR = driftFactor;
		WHL_RADIUS = wheelRadius;
		WHL_SEPARATION  = wheelSeparation;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
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
		}
		else {
			leftMotor.backward();
			rightMotor.backward();
		}
	}
	
	/**
	 * 	Naively moves forward a given distance, no error checking with odometer.
	 * 	returns only once the robot has finished moving
	 * @param distance	The distance by which to move, in cm
	 */
	public void move(double distance) {
		setSpeed(FWD_SPEED);

		leftMotor.rotate(convertDistance(WHL_RADIUS, distance), true);
		rightMotor.rotate(convertDistance(WHL_RADIUS, distance), false);
	}
	
	/**
	 * 	Turns left or right, but not on itself. 
	 * 	the robot will follow a curved path in the direction specified
	 * @param direction	The direction in which to turn: LEFT or RIGHT
	 */
	public void drift(Direction direction) {
		if (direction == Direction.FWD || direction == Direction.BACK) {
			System.out.println("Cannot turn " + direction + "\n");
			System.out.println("Must turn left or right");
			return;
		}
		
		if(direction == Direction.LEFT) {
			leftMotor.setSpeed(FWD_SPEED - DRIFT_FACTOR);
			rightMotor.setSpeed(FWD_SPEED + DRIFT_FACTOR);
			leftMotor.forward();
			rightMotor.forward();
		}
		else {
			leftMotor.setSpeed(FWD_SPEED + DRIFT_FACTOR);
			rightMotor.setSpeed(FWD_SPEED - DRIFT_FACTOR);
			leftMotor.forward();
			rightMotor.forward();
		}
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
		
		setSpeed(TURN_SPEED);
		
		if (direction == Direction.LEFT) {
			leftMotor.backward();
			rightMotor.forward();
		}
		else {
			leftMotor.forward();
			rightMotor.backward();
		}
	}
	
	/**
	 * Naively turns in the provided direction, no error checking with odometer.
	 * returns only once the robot has finished turning
	 * @param angle The angle in which to turn, in degrees. Will turn left if angle is positive, right otherwise
	 */
	public void turn(double angle) {
		setSpeed(TURN_SPEED);
		
		leftMotor.rotate(-convertAngle(WHL_RADIUS, WHL_SEPARATION, angle), true);
		rightMotor.rotate(convertAngle(WHL_RADIUS, WHL_SEPARATION, angle), false);
	}
	
	/**
	 * 	Stops any movement.
	 */
	public void stop() {
		leftMotor.stop();
		rightMotor.stop();
	}
	
	public void setSpeed(int speed) {
		leftMotor.setSpeed(speed);
		rightMotor.setSpeed(speed);
	}
	
	public boolean isMoving(){
		if(rightMotor.isMoving() || leftMotor.isMoving()){
			return true;
		}
		return false;
	}
	
	//methods to compliment the odometer class
	public void getTotalTachoCount(int[] tachoTotal){
		tachoTotal[0]=rightMotor.getTachoCount();
		tachoTotal[1]=leftMotor.getTachoCount();
	}
	public double delArc(int[] delTacho){
		return ((delTacho[0]+delTacho[1])*WHL_RADIUS*Math.PI)/360;
	}
	public double delTheta(int[] delTacho){
		return ((delTacho[0]-delTacho[1])*WHL_RADIUS)/(WHL_SEPARATION/2)/2;
	}
	
	// Utility methods provided in lab 2
	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}
}
