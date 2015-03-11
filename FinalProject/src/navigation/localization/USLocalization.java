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
import sensors.filters.DifferentialFilter;
import sensors.filters.OutlierFilter;
import sensors.managers.ObstacleDetection;
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
	//private FilteredUltrasonicSensor leftUSSensor;
	//private FilteredUltrasonicSensor rightUSSensor;
	
	private ObstacleDetection obstacleDetection;
	
	private double[] leftUSPosition = {5,-3};
	private double[] rightUSPosition = {5,3};
	
	private double[] pos = new double[3];
	private int usSensorOutlier = 255;
	
	public USLocalization(Odometer odo, Driver driver, Navigation nav) {
		super(odo, driver, nav);
		
		//leftUSSensor = new FilteredUltrasonicSensor(SensorPort.S3 ,new OutlierFilter(3, usSensorOutlier));
		//rightUSSensor = new FilteredUltrasonicSensor(SensorPort.S2 ,new OutlierFilter(3, usSensorOutlier));
		
		obstacleDetection = ObstacleDetection.getObstacleDetection();
	}

	/**
	 * @see navigation.localization.Localization#doLocalization()
	 */
	@Override
	public void doLocalization() {
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
		obstacleDetection.setRunning(true);
		driver.turn(Direction.RIGHT);
		faceAwayFromWall();
		driver.stop();
		angles[0] = obstacleDetection.rightDistance();
		//get the angles for each falling edges
		//angles[0] = getThetaFallingEdge();
		
		obstacleDetection.setRunning(false);
		driver.turn(Direction.LEFT);
		//getThetaRisingEdge();
		pause(1000);
		obstacleDetection.setRunning(true);
		pause(1000);
		
		faceAwayFromWall();
		driver.stop();
		angles[1] = obstacleDetection.leftDistance();
		//angles[1] = getThetaFallingEdge();
		
		odo.setTheta(localizationHeading(angles));
		
		
//		nav.turnTo(270);
//		odo.setX(obstacleDetection.rightDistance());
//		
//		nav.turnTo(180);
//		odo.setY(obstacleDetection.rightDistance());
		
	}
	
	//returns true if the robot is sort of facing both wall (this is a rough approximation)
	private boolean isfacingBothWalls(double[] readings){
		readings[0] = obstacleDetection.leftDistance();
		readings[1] = obstacleDetection.rightDistance();
		
		//readings[0]=leftUSSensor.getFilteredData();
		//readings[1]=rightUSSensor.getFilteredData();
		
		if(readings[0] < 80 && readings[1] < 80){
			return true;
		}
		return false;
	}
	//returns true if the robot is in the first tile (might want to change the values: i made them larger to deal with the errors)
	private boolean isInFirstTile(double[] readings) {
		// readings[0]=leftUSSensor.getFilteredData();
		// readings[1]=rightUSSensor.getFilteredData();
		readings[0] = obstacleDetection.leftDistance();
		readings[1] = obstacleDetection.rightDistance();
		
		//System.out.println(readings[0]+ "\t"+ readings[1]);
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
	
	//make sure to be facing away from wall;
	private void faceAwayFromWall(){
		double val;
		DifferentialFilter dFilter = new DifferentialFilter(2);
		OutlierFilter oFilter = new OutlierFilter(2, 250);
		do{
			//val=rightUSSensor.getFilteredData();
			// Use edge triggering by applying the differential filter.
			val = obstacleDetection.rightDistance();
			System.out.println(val);
			val = oFilter.filter(dFilter.filter(val));
			pause(10);
		} while(val < 50);
		
	}
	
	//returns the average of the entering of the wall and the exit
	private double getThetaFallingEdge() {
		double threshold=35, noiseMargin=3;
		//double val=rightUSSensor.getFilteredData();
		double val = obstacleDetection.rightDistance();
		double[] angles = new double[2];
		
		//enters upper margin of error
		while(val>threshold+noiseMargin){
			//val=rightUSSensor.getFilteredData();
			obstacleDetection.rightDistance();
			//System.out.println("\t" + val);
		}
		System.out.println("inside upper margin of error");
			
		odo.getPosition(pos);
		angles[0]=pos[2];
	
		//leaves lower margin of error
		while(val>threshold-noiseMargin) {
			//val=rightUSSensor.getFilteredData();
			obstacleDetection.rightDistance();
		}
		System.out.println("outside lower margin of error");
		
		odo.getPosition(pos);
		angles[1]=pos[2];
		
		return (angles[0]+angles[1])/2;
		
	}
	//returns the average of the exiting of the wall and the entering
	private double getThetaRisingEdge(){
		double threshold=35, noiseMargin=3;
		//double val=rightUSSensor.getFilteredData();
		double val = obstacleDetection.rightDistance();
		double[] angles = new double[2];
		
		//enters lower margin of error
		while(val<threshold-noiseMargin) {
			//val=rightUSSensor.getFilteredData();
			obstacleDetection.rightDistance();
		}
			
		
		odo.getPosition(pos);
		angles[0]=pos[2];
	
		//leaves upper margin of error
		while(val<threshold+noiseMargin) {
			// val=rightUSSensor.getFilteredData();
			obstacleDetection.rightDistance();
		}
			
		
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
	
	private void pause(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
