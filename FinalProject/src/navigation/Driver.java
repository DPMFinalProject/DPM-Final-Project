/**
 * 	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Driver.java
 *	Created On:	Feb 24, 2015
 */
package navigation;

import navigation.avoidance.ObstacleDetection;
import util.Direction;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Motor;

/**
 * 	This class provides abstraction from the robot's driving functionality.
 *	Obstacle evasion should be integrated in here and not in navigator.
 * @author Oleg 
 */

public class Driver {

	private final int FWD_SPEED = 150;
	private final int FWD_ACCEL = 200;
	private final int TURN_SPEED = 100;
	private final int DRIFT_FACTOR = 50;
	
	private final double WHL_RADIUS = 2.15;
	private final double WHL_SEPARATION = 15.3;

	private final NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.B;
	private Object lock;
	
	private final ObstacleDetection detection;
	public Driver() {
		//this(100, 100, 100, 50, 2.5, 15, Motor.A, Motor.B);
//		leftMotor.setAcceleration(FWD_ACCEL);
//		rightMotor.setAcceleration(FWD_ACCEL);
		detection = new ObstacleDetection();
		lock = new Object();
		
		// start the obstacle detector.
		//(new Thread(detection)).start();
	}
	
	/*	The constructors nested here should only be used for testing.
	public Driver(double wheelRadius, double wheelSeparation) {
		this(400, 100, 100, 50, wheelRadius, wheelSeparation, Motor.A, Motor.B);
	}
	
	public Driver(double wheelRadius, double wheelSeparation, 
			NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor) {
		this(100, 100, 100, 50, wheelRadius, wheelSeparation, leftMotor, rightMotor);
	}
	
	private Driver(int fwdSpeed, int fwdAccel, int turnSpeed,
			double wheelRadius, double wheelSeparation, 
			NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor) {
		
		this(fwdSpeed, fwdAccel, turnSpeed, 20, wheelRadius, wheelSeparation, leftMotor, rightMotor);
	}
	
	private Driver(int fwdSpeed, int fwdAccel, int turnSpeed, int driftFactor,
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
	}*/
	
	/**
	 * Moves continuously until stop() is called.
	 * @param direction	The direction in which to move: FWD or BACK
	 */
	public void move(Direction direction) {
		if (!validMoveDirection(direction)) {
			return;
		}
		
		move(direction, FWD_SPEED, FWD_SPEED);
	}
	
	/**
	 * 	Naively moves forward a given distance, no error checking with odometer.
	 * 
	 * @param distance	The distance by which to move, in cm
	 * @param returnNow return immediately if true
	 */
	public void move(double distance, boolean returnNow) {
		setSpeed(FWD_SPEED);

//		detection.setRunning(true);
		int a=convertDistance(WHL_RADIUS, distance);
				
			leftMotor.rotate(a, true);
			rightMotor.rotate(a, returnNow);
		
//		detection.setRunning(false);
	}
	
	/*
	 * 	Moves the robot with different wheel spins. This method should stay hidden.
	 */
	private void move(Direction direction, int leftSpeed, int rightSpeed) {
		if (!validMoveDirection(direction)) {
			return;
		}
		
		detection.setRunning(true);
		
		setSpeed(leftSpeed, rightSpeed);
		
		if (direction == Direction.FWD) {
			leftMotor.forward();
			rightMotor.forward();
		}
		else {
			leftMotor.backward();
			rightMotor.backward();
		}
		
		detection.setRunning(false);
	}
	
	private boolean validMoveDirection(Direction direction) {
		if (direction == Direction.LEFT || direction == Direction.RIGHT) {
			System.out.println("Cannot move " + direction + "\n");
			System.out.println("Must move forward or backward");
			return false;
		}
		return true;
	}
	
	/**
	 * 	Turns left or right, but not on itself. 
	 * 	the robot will follow a curved path in the direction specified
	 * @param direction	The direction in which to turn: LEFT or RIGHT
	 */
	public void drift(Direction direction) {
		if (!validTurnDirection(direction)) {
			return;
		}
		
		if(direction == Direction.LEFT) {
			move(Direction.FWD, FWD_SPEED - DRIFT_FACTOR, FWD_SPEED + DRIFT_FACTOR);
		}
		else {
			move(Direction.FWD, FWD_SPEED + DRIFT_FACTOR, FWD_SPEED - DRIFT_FACTOR);
		}
	}
	
	/**
	 * 	Turns continuously until stop() is called.
	 * @param direction The direction in which to turn: LEFT or RIGHT.
	 */
	public void turn(Direction direction) {
		if (!validTurnDirection(direction)) {
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
	 * @param direction	The direction in which to turn. RIGHT or LEFT.
	 * @param angle The angle by which to turn, in degrees. 
	 */
	public void turn(Direction direction, double angle) {
		if (!validTurnDirection(direction)) {
			return;
		}
		if (angle < 0) {
			System.out.println("Cannot turn by a negative angle");
			return;
		}
		
		setSpeed(TURN_SPEED);
		
		angleTurn(direction, angle, false);
	}
	
	/**
	 * Naively turns in the provided direction, no error checking with odometer.
	 * returns only once the robot has finished turning
	 * @param direction	The direction in which to turn. RIGHT or LEFT.
	 * @param angle The angle by which to turn, in degrees. 
	 */
	public void turn(Direction direction, double angle, boolean immediateReturn) {
		if (!validTurnDirection(direction)) {
			return;
		}
		if (angle < 0) {
			System.out.println("Cannot turn by a negative angle");
			return;
		}
		
		setSpeed(TURN_SPEED);
		
		angleTurn(direction, angle, immediateReturn);
	}
	
	private void angleTurn(Direction direction, double angle, boolean immediateReturn) {
		if (direction == Direction.LEFT) {
			leftMotor.rotate(-convertAngle(WHL_RADIUS, WHL_SEPARATION, angle), true);
			rightMotor.rotate(convertAngle(WHL_RADIUS, WHL_SEPARATION, angle), immediateReturn);
		} else if (direction == Direction.RIGHT){
			leftMotor.rotate(convertAngle(WHL_RADIUS, WHL_SEPARATION, angle), true);
			rightMotor.rotate(-convertAngle(WHL_RADIUS, WHL_SEPARATION, angle), immediateReturn);
		} else {
			System.out.println("Cannot turn forward or backwards.");
		}
	}
	private boolean validTurnDirection(Direction direction) {
		if (direction == Direction.FWD || direction == Direction.BACK) {
			System.out.println("Cannot turn " + direction + "\n");
			System.out.println("Must turn left or right");
			return false;
		}
		return true;
	}
	
	/**
	 * 	Stops any movement.
	 */
	public void stop() {
		leftMotor.stop(true);
		rightMotor.stop();
	}
	
	private void setSpeed(int speed) {
		setSpeed(speed, speed);
	}
	
	private void setSpeed(int leftSpeed, int rightSpeed) {
		synchronized (lock){
			leftMotor.setSpeed(leftSpeed);
			rightMotor.setSpeed(rightSpeed);
		}
		
	}
	
	/**
	 * Returns the state of the robot.
	 * @return	Returns true if the robot is moving.
	 */
	public boolean isMoving(){
		return rightMotor.isMoving() || leftMotor.isMoving();
	}
	
	//methods to complement the odometer class
	
	/**
	 * Update the change in the tachometer reading since the last time this metod was called
	 * @param tachoTotal
	 * @param delTacho
	 */
	public void getDelTachoCount(int[] tachoTotal, int[] delTacho){
		delTacho[0]=rightMotor.getTachoCount() - tachoTotal[0];
		delTacho[1]=leftMotor.getTachoCount() - tachoTotal[1];
		
	}
	/**
	 * returns the change in arclength since the last time this method was called
	 * @param delTacho
	 * @return Returns the charge in arclength
	 */
	public double getDelArc(int[] delTacho){
		return ((delTacho[0]+delTacho[1])*WHL_RADIUS*Math.PI)/360;
	}
	/**
	 * returns the change in heading (theta) since   time this method was called
	 * @param delTacho
	 * @return Returns the change in heading
	 */
	public double getDelTheta(int[] delTacho){
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
