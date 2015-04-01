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
 * 	This class provides all methods that involve giving a movement-related order
 * 	to the robot. None of these methods provide any form of correction or avoidance.
 * 	This class is mainly a utility class that should be used by the localization, 
 * 	navigation and avoidance algorithms.
 * 
 * @author Oleg 
 */

public class Driver {

	private final static int FWD_SPEED = 300;
	private final static int TURN_SPEED = 200;
	
	private final static int MOVE_ACCEL = 3000;
	private final static int STOP_ACCEL = 9000;				// Stopping uses a larger acceleration to avoid overshooting
	
	private final static int DRIFT_FACTOR = 95;				// Bigger = drift tighter , Smaller = larger
	
	private final static double WHL_RADIUS = 2.12;			// smaller radius = go further
	private final static double WHL_SEPARATION = 16.6;		// smaller width = turn less

	private final static NXTRegulatedMotor leftMotor = Motor.B, rightMotor = Motor.A;
	
	private static boolean isTurning = false;
	private static boolean isMovingBackwards = false;
	private static boolean isDrifting = false;

//--------------------------------------- MOVE ---------------------------------------	
	
	/**
	 * Moves continuously until stop() is called.
	 * @param direction	The direction in which to move: FWD or BACK
	 */
	public static void move(Direction direction) {
		move(direction, FWD_SPEED, FWD_SPEED);
	}
	
	/**
	 * 	General method interacting with the leJOS API to move the robot's wheels at given speeds.
	 * 
	 * @param direction		The direction in which to move, in cm: FWD or BACK.
	 * @param leftSpeed		The speed at which to turn the left wheel, in degrees per second.
	 * @param rightSpeed	The speed at which to turn the right wheel, in degrees per second.
	 */
	private static void move(Direction direction, int leftSpeed, int rightSpeed) {
		if (!validMoveDirection(direction)) {	// Return if LEFT or RIGHT.
			return;
		}
		
		setAcceleration(MOVE_ACCEL);
		setSpeed(leftSpeed, rightSpeed);
		
		if (direction == Direction.FWD) {
			leftMotor.forward();
			rightMotor.forward();
		}
		else {
			isMovingBackwards = true;
			
			leftMotor.backward();
			rightMotor.backward();
			
			isMovingBackwards = false;	// ??
		}
	}
	
	/**
	 * 	Moves forward a given distance, no error checking with odometer.
	 *  No immediate return, so control is handed over once the method has finished execution.
	 * 
	 * @param distance	The distance by which to move, in cm
	 */
	public static void move(double distance) {
		move(distance, false);
	}
	
	/**
	 * 	Moves forward a given distance, no error checking with odometer.
	 * 	.
	 * 
	 * @param distance	The distance by which to move, in cm
	 * @param immediateReturn With immediate return, the method finishes running 
	 * before the robot is done moving the provided distance
	 */
	public static void move(double distance, boolean immediateReturn) {
		
		setAcceleration(MOVE_ACCEL);
		setSpeed(FWD_SPEED);
		
		isMovingBackwards = distance < 0;
		
		int rotations = convertDistance(distance);
		
		leftMotor.rotate(rotations, true);
		rightMotor.rotate(rotations, immediateReturn);
		
		isMovingBackwards = false;	// ?
	}
	
	/**
	 * Returns the state of the robot.
	 * @return	Returns true if the robot is moving.
	 */
	public static boolean isMoving(){
		return rightMotor.isMoving() || leftMotor.isMoving();
	}
	
	/**
	 * Returns the state of the robot.
	 * @return	Returns true if the robot is moving backwards.
	 */
	public static boolean isMovingBackwards() {
		return isMovingBackwards;
	}

//--------------------------------------- TURN ---------------------------------------	
	
	/**
	 * 	Turns continuously until stop() is called.
	 * @param direction The direction in which to turn: LEFT or RIGHT.
	 */
	public static void turn(Direction direction) {
		if (!validTurnDirection(direction)) {
			return;
		}
		
		isTurning = true;
		
		setAcceleration(MOVE_ACCEL);
		setSpeed(TURN_SPEED);
		
		if (direction == Direction.LEFT) {
			leftMotor.backward();
			rightMotor.forward();
		}
		else {
			leftMotor.forward();
			rightMotor.backward();
		}
		
		isTurning = false;
	}
	
	/**
	 * Naively turns in the provided direction, no error checking with odometer.
	 * Only returns once the robot has finished turning.
	 * 
	 * @param direction	The direction in which to turn. RIGHT or LEFT.
	 * @param angle	The angle by which to turn, in degrees. 
	 */
	public static void turn(Direction direction, double angle) {
		turn(direction, angle, false);
	}
	
	/**
	 * Naively turns in the provided direction, no error checking with odometer.
	 * 
	 * @param direction	The direction in which to turn. RIGHT or LEFT.
	 * @param angle	The angle by which to turn, in degrees. 
	 * @param immediateReturn With immediate return, the method finishes running 
	 * before the robot is done moving the provided distance
	 */
	public static void turn(Direction direction, double angle, boolean immediateReturn) {
		if (!validTurnDirection(direction)) {
			return;
		}
		
		if (angle < 0) {
//			System.out.println("Cannot turn by a negative angle");
			return;
		}
		
		isTurning = true;
		
		setAcceleration(MOVE_ACCEL);
		setSpeed(TURN_SPEED);
		
		// Compute the required amount of wheel rotations to execute the command
		int rotations = convertAngle(angle);	
		
		if (direction == Direction.LEFT) {
			leftMotor.rotate(-rotations, true);
			rightMotor.rotate(rotations, immediateReturn);
		} 
		else {
			leftMotor.rotate(rotations, true);
			rightMotor.rotate(-rotations, immediateReturn);
		}
		
		isTurning = false;
	}
	
	/**
	 * Returns the state of the robot.
	 * @return	Returns true if the robot is turning.
	 */
	public static boolean isTurning() {
		return isTurning;
	}

//--------------------------------------- DRIFT ---------------------------------------
	
	/**
	 * 	Turns left or right, but not on itself. 
	 * 	
	 * 	The robot will follow a curved path in the direction specified
	 * 	based on the DRIFT_FACTOR specified in this class
	 * 
	 * 	Because this method returns immediately, the isDrifting status variable
	 * 	should be set externally.
	 * 
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
	 * 	Turns left or right, but not on itself. 
	 * 	the robot will follow a curved path with specified radius
	 * 
	 * 	Because this method returns immediately, the isDrifting status variable
	 * 	should be set externally
	 * 
	 * @param direction	The direction in which to turn: LEFT or RIGHT
	 * @param turnRadius The radius of the path to follow
	 */
	public static void drift(Direction direction, int turnRadius) {
		int leftSpeed;
		int rightSpeed;
		
		if(direction == Direction.RIGHT) {
			leftSpeed = (int)(((turnRadius+(WHL_SEPARATION/2))/turnRadius)*FWD_SPEED);
			rightSpeed = (int)(((turnRadius-(WHL_SEPARATION/2))/turnRadius)*FWD_SPEED);
		}
		else {
			leftSpeed = (int)(((turnRadius-(WHL_SEPARATION/2))/turnRadius)*FWD_SPEED);
			rightSpeed = (int)(((turnRadius+(WHL_SEPARATION/2))/turnRadius)*FWD_SPEED);
		}
		
		move(Direction.FWD, leftSpeed, rightSpeed);
	}
	
	/**
	 * Returns the state of the robot.
	 * @return	Returns true if the robot is drifting.
	 */
	public static boolean isDrifting() {
		return isDrifting;
	}
	
	/**
	 * set drifting status flag.
	 */
	public static void setDrifting(boolean status) {
		isDrifting = status;
	}
	
//--------------------------------------- MISCELLANEOUS ---------------------------------------

	/**
	 * Move one motor in specified direction
	 * 
	 * @param motor The motor that should turn (LEFT or RIGHT one)
	 * @param fwdOrBack The direction in which the robot will move during the turn, FWD or BACK.
	 */
	public static void moveOneMotor(Direction motor, Direction fwdOrBack){
		// Check that the method was called correctly, return if not.
		if (motor == Direction.FWD || motor == Direction.BACK || 
				fwdOrBack == Direction.LEFT || fwdOrBack == Direction.RIGHT) {
			return;
		}
		
		if (motor == Direction.LEFT) {
			if (fwdOrBack == Direction.FWD) {
				leftMotor.forward();
			} else {
				leftMotor.backward();
			}
		} else if (motor == Direction.RIGHT) {
			if (fwdOrBack == Direction.FWD) {
				rightMotor.forward();
			} else {
				rightMotor.backward();
			}
		}
	}
	
	/**
	 * 	Stops any movement.
	 */
	public static void stop() {
		setAcceleration(STOP_ACCEL);
		
		leftMotor.stop(true);	// immediate return to stop both wheels
		rightMotor.stop();
		
		setAcceleration(MOVE_ACCEL);
	}
	
	/**
	 *  Make robot drive in a circle
	 *  Only used for testing purposes in the util.Paths class
	 * 
	 * @param direction
	 * @param radius
	 */
	public static void driveCircle(Direction direction, double radius) {
		
		int leftSpeed;
		int rightSpeed;
		int leftDistance;
		int rightDistance;
		
		if(direction == Direction.RIGHT) {
			leftSpeed = (int)(((radius+(WHL_SEPARATION/2))/(radius-(WHL_SEPARATION/2)))*FWD_SPEED);
			rightSpeed = FWD_SPEED;
			leftDistance = convertDistance(2*Math.PI*(radius+(WHL_SEPARATION/2)));
			rightDistance = convertDistance(2*Math.PI*(radius-(WHL_SEPARATION/2)));;
		}
		else {
			leftSpeed = FWD_SPEED;
			rightSpeed = (int)(((radius+(WHL_SEPARATION/2))/(radius-(WHL_SEPARATION/2)))*FWD_SPEED);
			leftDistance = convertDistance(2*Math.PI*(radius-(WHL_SEPARATION/2)));;
			rightDistance = convertDistance(2*Math.PI*(radius+(WHL_SEPARATION/2)));;
		}
		
		setSpeed(leftSpeed, rightSpeed);
		leftMotor.rotate(leftDistance, true);
		rightMotor.rotate(rightDistance);
		
	}
	
	private static boolean validMoveDirection(Direction direction) {
		if (direction == Direction.LEFT || direction == Direction.RIGHT) {
//			System.out.println("Cannot move " + direction + "\n");
//			System.out.println("Must move forward or backward");
			return false;
		}
		return true;
	}
	
	private static boolean validTurnDirection(Direction direction) {
		if (direction == Direction.FWD || direction == Direction.BACK) {
//			System.out.println("Cannot turn " + direction + "\n");
//			System.out.println("Must turn left or right");
			return false;
		}
		return true;
	}
	
	private static void setSpeed(int speed) {
		setSpeed(speed, speed);
	}
	
	private static void setSpeed(int leftSpeed, int rightSpeed) {
		leftMotor.setSpeed(leftSpeed);
		rightMotor.setSpeed(rightSpeed);
	}
	
	private static void setAcceleration(int acceleration) {
		leftMotor.setAcceleration(acceleration);
		rightMotor.setAcceleration(acceleration);
	}
	
	
//--------------------------------------- COMPLEMENTARY ODOMETRY METHODS ---------------------------------------
	
	/**
	 * Update the change in the tachometer reading since the last time this metod was called
	 * @param tachoTotal
	 * @param delTacho
	 */
	public static void getDelTachoCount(int[] tachoTotal, int[] delTacho){
		delTacho[0] = rightMotor.getTachoCount() - tachoTotal[0];
		delTacho[1] = leftMotor.getTachoCount() - tachoTotal[1];
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
	
//--------------------------------------- UTILITY METHODS ---------------------------------------
	 

	private static int convertAngle(double angle) {
		return convertDistance(Math.PI * WHL_SEPARATION * angle / 360.0);
	}
	
	private static int convertDistance(double distance) {
		return (int) ((180.0 * distance) / (Math.PI * WHL_RADIUS));
	}
}
