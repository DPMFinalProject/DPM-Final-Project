/**
a *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	LSLocalization.java
 *	Created On:	Feb 26, 2015
 */
package navigation.localization;

import lejos.nxt.ColorSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import sensors.FilteredColorSensor;
import sensors.FilteredSensor;
import sensors.filters.DifferentialFilter;
import util.Direction;
import util.GridManager;
import navigation.Driver;
import navigation.Navigation;
import navigation.odometry.Odometer;

/**
 * 	Performs localization using the light sensor
 * @author Gregory Brookes
 */
//###################################################################
//#            TODO: Figure out how to make the robot be 			#
//#					  perpendicular to a line.						#
//#																	#
//###################################################################


public class LSLocalizationIntercept extends Localization {
	final GridManager grid = new GridManager();
	private Direction triggeredSensor ;
	
	private double[] pos = new double[3];
	
	public LSLocalizationIntercept(Odometer odo, Driver driver, Navigation nav) {
		super(odo, driver, nav);
		(new Thread(grid)).start();
	}

	/**
	 * Find out the position by intercepting a line and rotating 
	 * until both sensor and ON the line. 
	 * @see navigation.localization.Localization#doLocalization()
	 */
	@Override
	public void doLocalization() {		
		//probelem: if in tile 1, more towardscloseWall??
																							//		|	2	|	3
		//The goal is to intercept the first X or first Y grid line, so we want to move				|-------|-------
		//away from the closest wall or towards it if we are in the 3rd tile: following this -->	|___0___|___1___				
		if(isInTile3()){
			moveTowardsClosestWall();
		}else{
			moveAwayClosestWall();
		}
		
		//orient the robot perpendicular to the line
		perpendicularToLine();
		getCorrectedPos(pos);
		
		//orient the robot facing the other line
		toOtherLine();
		
		//orient the robot perpendicular to the line
		perpendicularToLine();
		getCorrectedPos(pos);
		
		odo.setPosition(pos, new boolean[] {true,true,true});
			
	}
	//#######################################
	//Note: the wheels are wired on the robot used so it turns the wrong way
	
	//Utilities methods
	private boolean isInTile3 (){
		odo.getPosition(pos);
		if(pos[0]>30.48 && pos[1]>30.48){
			return true;
		}
		return false;
	}
	private void moveTowardsClosestWall(){
		if(pos[0]<pos[1]){
			nav.turnTo(270);
		}else{
			nav.turnTo(180);
		}
		driver.move(Direction.FWD);
	}
	private void moveAwayClosestWall(){
		if(pos[0]<pos[1]){
			optimalRotation(0);
		}else{
			optimalRotation(1);
		}
		while(driver.isMoving()){
			try {	Thread.sleep(100);	} catch (InterruptedException e) {}
		}
		System.out.println("Forward");
		driver.move(Direction.FWD);
	}
	private void optimalRotation(int i){
		if (i == 0){
			if(pos[i]<30.48){
				nav.turnTo(0);
			}else{
				nav.turnTo(180);
			}
		}else if (i == 1){
			if(pos[i]<30.48){
				nav.turnTo(90);
			}else{
				nav.turnTo(270);
			}
		}else{
			System.out.println("Invatind input to optimalRotation method in LSLocalisationIntercept");
		}
	}
	
	
	//recursive method who will orient the robot perpendicular to a line
	private void perpendicularToLine(){
		//wait until a line is detected, then stop and turn depending on which sensor detected the line
		while(!grid.lineDetected()) {
			try {Thread.sleep(10);} catch (InterruptedException e) {}
		}
		driver.stop();
		driver.turn(rotationDirection());
		
		//find the what is the other sensor
		Direction untriggeredSensor = findUntriggeredSensor();
		
		//now: if triggered ls is off line, stop, move forward by 1 and rotate again
		//rotate as long as the triggered light sensor is on the line and until the second is on a line aswell
		while(grid.isOnLine(triggeredSensor)){
			if(grid.isOnLine(untriggeredSensor) ){
				driver.stop();
				return;
			}
		}
		//if the triggered light sensor leaves the line, recall this method
		driver.stop();
		driver.move(Direction.FWD);
		perpendicularToLine();
	}
	private Direction findUntriggeredSensor() {
		if(triggeredSensor == Direction.RIGHT){
			return Direction.LEFT;
		}
		return Direction.RIGHT;
	}
	//find out which sensor detected a line and return the direction to turn
	private Direction rotationDirection(){
		triggeredSensor=grid.whichSensorDetected();
		
		if(triggeredSensor == Direction.RIGHT){
			return Direction.LEFT;
		}
		return Direction.RIGHT;
	}

	//knowing one position and the angle, orient the robot towards the other line, by making it go forward and then turning clockwise of counter clockwaise
	private void toOtherLine() {
		driver.move(15, false);
		if(needClockWiseMovement(pos[2])){
			driver.turn(Direction.RIGHT, 90, false);
		}else{
			driver.turn(Direction.LEFT, 90, false);
		}
		
		driver.move(Direction.FWD);
		
	}
	
	private boolean needClockWiseMovement(double theta){
		int threshold = 15;
		//if just over a multiple of 90, turn clockwise, else turn counterclockwise
		if(theta%90 < 15){
			return true;
		}else if( theta%90-90 > -threshold){
			return false;
		}
		System.out.println("An error happened in the method: <<needClockWiseMovement>> 4"
				+ "maybe the treshold is too small or the robot was not on a line");
		return false;
	}
	
	
	private void getCorrectedPos(double[] pos) {
		double[] tempPos = new double[3];
		odo.getPosition(tempPos);
		
		//figure out which line and at what angle the robot is by roughly knowing it's orientation and knowing it's on a line.
		//to know this, depends near which angle is the robot positioned.
		tempPos[2]/=90;
		
		if(tempPos[2] < 0.5){
			pos[0]=30.48;
			pos[2]=0;
		}else if (tempPos[2] < 1.5){
			pos[1]= 30.48;
			pos[2]=90;
		}else if(tempPos[2] < 2.5){
			pos[0]=30.48;
			pos[2]=180;
		}else if (tempPos[2] < 3.5 ){
			pos[1]=30.48;
			pos[2]=270;
		}else{
			pos[0]=30.48;
			pos[2]=0;
		}
	}
	
}




	


