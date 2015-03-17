/**
 * 	DPM Final Project
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
 * 	This class provides abstraction from the robot's driving functionality.
 *	Obstacle evasion should be integrated in here and not in navigator.
 * @author Oleg 
 */

public class Driver {

	private final static int FWD_SPEED = 500;
	private final static int FWD_ACCEL = 6000;
	private final static int TURN_SPEED = 200;
	private final static int DRIFT_FACTOR = 50;
	
	private static double WHL_RADIUS = 2.085;//2.09			//smaller radius = go further
	private static double WHL_SEPARATION = 17.355;		//smaller width = turn less

	private final static NXTRegulatedMotor leftMotor = Motor.B, rightMotor = Motor.A;
	private static Object lock;
	
	/**
	 * Moves continuously until stop() is called.
	 * @param direction	The direction in which to move: FWD or BACK
	 */
	public static void move(Direction direction) {
		if (!validMoveDirection(direction)) {
			return;
		}
		
		move(direction, FWD_SPEED, FWD_SPEED);
	}
	
	/**
	 * 	Naively moves forward a given distance, no error checking with odometer.
	 * 
	 * @param distance	The distance by which to move, in cm
	 * @param immediateReturn return immediately if true
	 */
	public static void move(double distance, boolean immediateReturn) {
		int acc=leftMotor.getAcceleration();
		setAcceleration(acc/10);
			
		setSpeed(FWD_SPEED);

//		detection.setRunning(true);
		int a=convertDistance(WHL_RADIUS, distance);
				
		leftMotor.rotate(a, true);
		rightMotor.rotate(a,immediateReturn);
			
		setAcceleration(acc);
		
//		detection.setRunning(false);
	}
	
	private static void setAcceleration(int acceleration) {
		leftMotor.setAcceleration(acceleration);
		rightMotor.setAcceleration(acceleration);
		
	}

	/*
	 * 	Moves the robot with different wheel spins. This method should stay hidden.
	 */
	private static void move(Direction direction, int leftSpeed, int rightSpeed) {
		if (!validMoveDirection(direction)) {
			return;
		}
		
		setSpeed(leftSpeed, rightSpeed);
		
		if (direction == Direction.FWD) {
			leftMotor.forward();
			rightMotor.forward();
		}
		else {
			leftMotor.backward();
			rightMotor.backward();
		}
	}
	
	private static boolean validMoveDirection(Direction direction) {
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
	public static void drift(Direction direction) {
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
	public static void turn(Direction direction) {
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
	public static void turn(Direction direction, double angle) {
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
	public static void turn(Direction direction, double angle, boolean immediateReturn) {
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
	
	private static void angleTurn(Direction direction, double angle, boolean immediateReturn) {
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
	
	private static boolean validTurnDirection(Direction direction) {
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
	public static void stop() {
		leftMotor.stop(true);
		rightMotor.stop();
		
		leftMotor.flt();
		rightMotor.flt();
	}
	
	private static void setSpeed(int speed) {
		setSpeed(speed, speed);
	}
	
	private static void setSpeed(int leftSpeed, int rightSpeed) {
			leftMotor.setSpeed(leftSpeed);
			rightMotor.setSpeed(rightSpeed);
	}
	
	/**
	 * Returns the state of the robot.
	 * @return	Returns true if the robot is moving.
	 */
	public static boolean isMoving(){
		return rightMotor.isMoving() || leftMotor.isMoving();
	}
	
	//methods to complement the odometer class
	
	/**
	 * Update the change in the tachometer reading since the last time this metod was called
	 * @param tachoTotal
	 * @param delTacho
	 */
	public static void getDelTachoCount(int[] tachoTotal, int[] delTacho){
		delTacho[0]=rightMotor.getTachoCount() - tachoTotal[0];
		delTacho[1]=leftMotor.getTachoCount() - tachoTotal[1];
	}
	
	/**
	 * returns the change in arclength since the last time this method was called
	 * @param delTacho
	 * @return Returns the charge in arclength
	 */
	public static double getDelArc(int[] delTacho){
		return ((delTacho[0]+delTacho[1])*WHL_RADIUS*Math.PI)/360;
	}
	
	/**
	 * returns the change in heading (theta) since   time this method was called
	 * @param delTacho
	 * @return Returns the change in heading
	 */
	public static double getDelTheta(int[] delTacho){
		return ((delTacho[1]-delTacho[0])*WHL_RADIUS)/WHL_SEPARATION;
	}
	
	// Utility methods provided in lab 2
	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

}
