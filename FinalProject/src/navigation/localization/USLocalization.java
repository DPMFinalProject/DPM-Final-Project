/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	USLocalization.java
 *	Created On:	Feb 26, 2015
 */
package navigation.localization;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import sensors.FilteredColorSensor;
import sensors.FilteredSensor;
import sensors.FilteredUltrasonicSensor;
import sensors.filters.OutlierFilter;
import util.Direction;
import navigation.Driver;
import navigation.Navigation;
import navigation.odometry.Odometer;

/**
 * Performs localization using the ultrasonic sensor.
 * @author Gregory Brookes
 */
//###################################################################
//#            IDEA: Use the two US to come closer to 1st tile		#	
//#					and position yourself at 45 degrees or so		#
//#				then only one one US to do the localization			#
//###################################################################


public class USLocalization extends Localization {
	private FilteredUltrasonicSensor usL;
	private FilteredUltrasonicSensor usR;
	private double[] usLpos = {5,-3};
	private double[] usRpos = {5,3};
	
	private double[] pos = new double[3];
	private int usSensorOutlier = 255;
	
	public USLocalization(Odometer odo, Driver driver, Navigation nav) {
		super(odo, driver, nav);
	}

	/**
	 * @see navigation.localization.Localization#doLocalization()
	 */
	@Override
	public void doLocalization() {
		usL = new FilteredUltrasonicSensor(SensorPort.S3 ,new OutlierFilter(3, usSensorOutlier));
		usR = new FilteredUltrasonicSensor(SensorPort.S2 ,new OutlierFilter(3, usSensorOutlier));
		
		// order: L/R
		double[] readings = new double[2];
		
		//IDEA: turn until the robot faces both walls, move towards the farthest wall
		//until it reaches the first tile, then do the same localization as in lab 4
		
		/*
		driver.turn(Direction.LEFT);
		while(!isfacingBothWalls(readings)){
			try {	Thread.sleep(50);	} catch (InterruptedException e) {}
		}
		driver.stop();		
		
		System.out.println("Is facing both walls");
		*/
		/*
		while(!isInFirstTile(readings)){
			moveTowardsFurthestWall(readings);
		}
		System.out.println("is in first tile");
		*/
		
		fallingEdgeLocalization();
		
	}
	
	//Localization same as lab 4, using FallingEdge method;
	private void fallingEdgeLocalization(){
		double[] angles = new double [2];
	
		//make sure to face away from anywall at first
		driver.turn(Direction.RIGHT);
		faceAwayFromWall();
		
		//get the angles for each falling edges
		angles[0] = getThetaFallingEdge();
		
		driver.turn(Direction.LEFT);
		getThetaRisingEdge();
		
		angles[1]= getThetaFallingEdge();
		
		
		driver.stop();
		odo.setTheta(localizationHeading(angles));
		
		/*
		nav.turnTo(270);
		odo.setX(usR.getFilteredData());
		
		nav.turnTo(180);
		odo.setY(usR.getFilteredData());
		*/
		
		
	}
	
	//returns true if the robot is sort of facing both wall (this is a rough approximation)
	private boolean isfacingBothWalls(double[] readings){
		readings[0]=usL.getFilteredData();
		readings[1]=usR.getFilteredData();
		if(readings[0] < 80 && readings[1] < 80){
			return true;
		}
		return false;
	}
	//returns true if the robot is in the first tile (might want to change the values: i made them larger to deal with the errors)
	private boolean isInFirstTile(double[] readings) {
		readings[0]=usL.getFilteredData();
		readings[1]=usR.getFilteredData();
		System.out.println(readings[0]+ "\t"+ readings[1]);
		if(readings[0] < 30 && readings[1] < 30){
			return true;
		}
		return false;
	}
	//move towards thefarthestWall by increments of 10
	private void moveTowardsFurthestWall(double[] readings) {
		if(readings[0]>readings[1]){
			System.out.println("left is the closest wall");
			driver.turn(Direction.LEFT, 45);
			driver.move(10, false);
			driver.turn(Direction.RIGHT, 45);
		}else{
			System.out.println("Right is the closest wall");
			driver.turn(Direction.RIGHT, 45);
			driver.move(10, false);
			driver.turn(Direction.LEFT, 45);
		}
		
	}

/*	private boolean isnear45deg(){		
		if(Math.abs(usL.getFilteredData()-usR.getFilteredData()) < 10){
			return true;
		}
		return false;
	}

	private int inWhichTile(){
		double valL=usL.getFilteredData();
		double valR=usR.getFilteredData();
													//		^
		if(valL < 30 && valR <45){					//		|				|
			return 0;								//		|		1		|		3
		}else if(valL < 30){						//		|				|
			return 1;								//		|_______________|_________________
		}else if(valR < 30){						//		|				|
			return 2;								//		|				|
		}											//		|		0		|		2
		return 3;									//		|_______________|_________________>
	}*/
	
	//make sur to be facing away from wall;
	private void faceAwayFromWall(){
		double val;
		do{
			val=usR.getFilteredData();
		}while(val<50);
	}
	
	//returns the average of the entering of the wall and the exit
	private double getThetaFallingEdge() {
		double threshold=35, noiseMargin=3;
		double val=usR.getFilteredData();
		double[] angles = new double[2];
		
		//enters upper margin of error
		while(val>threshold+noiseMargin){
			val=usR.getFilteredData();
			System.out.println("\t" + val);
		}
		System.out.println("inside upper margin of error");
			
		odo.getPosition(pos);
		angles[0]=pos[2];
	
		//leaves lower margin of error
		while(val>threshold-noiseMargin)
			val=usR.getFilteredData();
		System.out.println("outside lower margin of error");
		
		odo.getPosition(pos);
		angles[1]=pos[2];
		
		return (angles[0]+angles[1])/2;
		
	}
	//returns the average of the exiting of the wall and the entering
	private double getThetaRisingEdge(){
		double threshold=35, noiseMargin=3;
		double val=usR.getFilteredData();
		double[] angles = new double[2];
		
		//enters lower margin of error
		while(val<threshold-noiseMargin)
			val=usR.getFilteredData();
		
		odo.getPosition(pos);
		angles[0]=pos[2];
	
		//leaves upper margin of error
		while(val<threshold+noiseMargin)
			val=usR.getFilteredData();
		
		odo.getPosition(pos);
		angles[1]=pos[2];
			
		return (angles[0]+angles[1])/2;
	}
	//calculate the heading after doing the localization
	//This is there the tester should play with the values.
	//TODO: calibrate the first number of each returns and look is the sensor Offset is fine.
	private double localizationHeading(double[] angles){
		double sensorOffset = 45;
		if (angles[0] < angles[1]){
			return 45- ((angles[0] + angles[1]-360)/2) - sensorOffset;
		}else{
			return 225-((angles[0]-360 + angles[1])/2) - sensorOffset;
		}
		
	}
	
	
}
