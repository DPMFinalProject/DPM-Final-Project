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
 *
 * @author Oleg 
 */

public class Driver {

	private final static int FWD_SPEED = 300;
	private final static int ACCEL = 3000;
	
	private final static int TURN_SPEED = 200;
	private final static int DRIFT_FACTOR = 95;
	
	private final static double WHL_RADIUS = 2.09;			//smaller radius = go further
	private final static double WHL_SEPARATION = 16.09;//16.1;//16.175;		//smaller width = turn less

	private final static NXTRegulatedMotor leftMotor = Motor.B, rightMotor = Motor.A;
	
	private static boolean isTurning = false;

//--------------------------------------- MOVE ---------------------------------------	
	
	/**
	 * Moves continuously until stop() is called.
	 * @param direction	The direction in which to move: FWD or BACK
	 */
	public static void move(Direction direction) {

		move(direction, FWD_SPEED, FWD_SPEED);
	}
	
	/**
	 * 	Moves the robot with different wheel spins. This method should stay hidden.
	 * 
	 * @param direction
	 * @param leftSpeed
	 * @param rightSpeed
	 */
	private static void move(Direction direction, int leftSpeed, int rightSpeed) {
		if (!validMoveDirection(direction)) {
			return;
		}
		
		setAcceleration(ACCEL);
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
	
	/**
	 * 	Naively moves forward a given distance, no error checking with odometer.
	 *  returns only once the robot has finished moving
	 * 
	 * @param distance	The distance by which to move, in cm
	 */
	public static void move(double distance) {
			
		move(distance, false);

	}
	
	/**
	 * 	same as above but can be made to return immediately
	 * 
	 * @param distance	The distance by which to move, in cm
	 * @param immediateReturn return immediately if true
	 */
	public static void move(double distance, boolean immediateReturn) {
		
		setAcceleration(ACCEL);
		setSpeed(FWD_SPEED);

		int rotations = convertDistance(WHL_RADIUS, distance);
		
		leftMotor.rotate(rotations, true);
		rightMotor.rotate(rotations, immediateReturn);
		
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
		
		setAcceleration(ACCEL);
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
	 * returns only once the robot has finished turning
	 * 
	 * @param direction	The direction in which to turn. RIGHT or LEFT.
	 * @param angle The angle by which to turn, in degrees. 
	 */
	public static void turn(Direction direction, double angle) {
		turn(direction, angle, false);
	}
	
	/**
	 * same as above, but can be made to return immediately
	 * 
	 * @param direction
	 * @param angle
	 * @param immediateReturn
	 */
	public static void turn(Direction direction, double angle, boolean immediateReturn) {
		if (!validTurnDirection(direction)) {
			return;
		}
		
		if (angle < 0) {
			System.out.println("Cannot turn by a negative angle");
			return;
		}
		
		isTurning = true;
		
		setAcceleration(ACCEL);
		setSpeed(TURN_SPEED);
		
		int rotations = convertAngle(WHL_RADIUS, WHL_SEPARATION, angle);
		
		if (direction == Direction.LEFT) {
			leftMotor.rotate(-rotations, true);
			rightMotor.rotate(rotations, immediateReturn);
		} 
		else if (direction == Direction.RIGHT) {
			leftMotor.rotate(rotations, true);
			rightMotor.rotate(-rotations, immediateReturn);
		}
		
		isTurning = false;
	}

//--------------------------------------- MISCELLANEOUS ---------------------------------------
	
	/**
	 * 	Turns left or right, but not on itself. 
	 * 	the robot will follow a curved path in the direction specified
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
	 * 	Stops any movement.
	 */
	public static void stop() {
		setAcceleration(9000);
		
		leftMotor.stop(true);
		rightMotor.stop();
		
		setAcceleration(ACCEL);
	}
	
	/**
	 * Returns the state of the robot.
	 * @return	Returns true if the robot is moving.
	 */
	public static boolean isMoving(){
		return rightMotor.isMoving() || leftMotor.isMoving();
	}
	
	public static boolean isTurning() {
		return isTurning;
	}
	
	private static boolean validMoveDirection(Direction direction) {
		
		if (direction == Direction.LEFT || direction == Direction.RIGHT) {
			System.out.println("Cannot move " + direction + "\n");
			System.out.println("Must move forward or backward");
			return false;
		}
		return true;
	}
	
	private static boolean validTurnDirection(Direction direction) {
		if (direction == Direction.FWD || direction == Direction.BACK) {
			System.out.println("Cannot turn " + direction + "\n");
			System.out.println("Must turn left or right");
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
	
	/**
	 *  make robot drive circle path
	 *  only used for testing purposes in the util.Paths class
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
			rightSpeed = FWD_SPEED;//(int)(((radius-(WHL_SEPARATION/2))/(radius+(WHL_SEPARATION/2)))*FWD_SPEED);
			leftDistance = convertDistance(WHL_RADIUS, 2*Math.PI*(radius+(WHL_SEPARATION/2)));
			rightDistance = convertDistance(WHL_RADIUS, 2*Math.PI*(radius-(WHL_SEPARATION/2)));;
		}
		else {
			leftSpeed = FWD_SPEED;//(int)(((radius-(WHL_SEPARATION/2))/(radius+(WHL_SEPARATION/2)))*FWD_SPEED);
			rightSpeed = (int)(((radius+(WHL_SEPARATION/2))/(radius-(WHL_SEPARATION/2)))*FWD_SPEED);
			leftDistance = convertDistance(WHL_RADIUS, 2*Math.PI*(radius-(WHL_SEPARATION/2)));;
			rightDistance = convertDistance(WHL_RADIUS, 2*Math.PI*(radius+(WHL_SEPARATION/2)));;
		}
		
		setSpeed(leftSpeed, rightSpeed);
		leftMotor.rotate(leftDistance, true);
		rightMotor.rotate(rightDistance);
		
	}
	
//--------------------------------------- Methods to complement the odometer class ---------------------------------------
	
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
	
//--------------------------------------- Utility methods provided in lab 2 ---------------------------------------
	 
	/**
	  * determine necessary wheel rotation for robot to turn angle on itself
	  * 
	  * @param radius
	  * @param width
	  * @param angle
	  * @return wheel rotation in degrees to rotate by angle
	  */
	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	
	/**
	 *  determine necessary wheel rotation to travel distance based on wheel radius
	 * 
	 * @param radius
	 * @param distance
	 * @return wheel rotation in degrees to travel distance 
	 */
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

}
