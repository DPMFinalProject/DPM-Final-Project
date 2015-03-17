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
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import sensors.FilteredColorSensor;
import sensors.FilteredSensor;
import sensors.FilteredUltrasonicSensor;
import sensors.filters.DifferentialFilter;
import sensors.filters.OutlierFilter;
import sensors.managers.ObstacleDetection;
import util.Direction;
import util.Measurements;
import navigation.Driver;
import navigation.Navigation;
import navigation.odometry.Odometer;

/**
 * Performs localization using the ultrasonic sensor.
 * @author Gregory Brookes, Oleg Zhilin
 */
//###################################################################
//#            IDEA: Use the two US to come closer to 1st tile		#	
//#					and position yourself at 45 degrees or so		#
//#				then only one one US to do the localization			#
//###################################################################


public class USLocalization extends Localization {
	
	private ObstacleDetection obstacleDetection;
	
	private double[] leftUSPosition = {5,-3};
	private double[] rightUSPosition = {5,3};
	
	private double[] pos = new double[3];
	private int usSensorOutlier = 255;
	
	private final double SENSOR_OFFSET = 9.3; 
	
	public USLocalization(Odometer odo, Driver driver, Navigation nav) {
		super(odo, driver, nav);
		
		obstacleDetection = ObstacleDetection.getObstacleDetection();
	}

	/**
	 * @see navigation.localization.Localization#doLocalization()
	 */
	@Override
	public void doLocalization() {
		
		faceWall();
		/* 
		 * Do multiple iterations of adjustments of the X and Y positions,
		 * the robot will converge onto (0, 0) provided SENSOR_OFFSET is calibrated
		 * correctly.
		 */
		for (int i = 0; i < 2; i++) {
			adjustYPosition(true);
			adjustXPosition(true);
		}
		
		/*
		 * 	It will have an approximate final orientation of 0 degrees,
		 * 	which should be accurate enough for the LS localization to adjust.
		 */
		driver.turn(Direction.RIGHT, 90);
		
	}
	
	private void adjustXPosition(boolean move) {
		double xPosition;
		
		obstacleDetection.setRunning(true);
		faceAwayFromWall(Direction.RIGHT);
		driver.turn(Direction.RIGHT, 30);
		xPosition = obstacleDetection.leftDistance() + SENSOR_OFFSET - Measurements.TILE;
		driver.turn(Direction.LEFT, 30);
		
		System.out.println("XPos: " + xPosition);
		
		if (move)
			driver.move(xPosition, false);
		
		odo.setX(0);
	}
	
	private void adjustYPosition(boolean move) {
		double yPosition;
		
		faceAwayFromWall(Direction.LEFT);
		driver.turn(Direction.LEFT, 20);
		yPosition = obstacleDetection.rightDistance() + SENSOR_OFFSET - Measurements.TILE;
		driver.turn(Direction.RIGHT, 20);
		
		System.out.println("YPos: " + yPosition);
		
		if (move)
			driver.move(yPosition, false);
		
		odo.setY(0);
	}
	
	// turn until facing away from wall;
	private void faceAwayFromWall(Direction sensorDirection){
		double val = 255;
		DifferentialFilter dFilter = new DifferentialFilter(2);
		
		driver.turn(sensorDirection);
		do{
			// Use edge triggering by applying the differential filter.
			if (sensorDirection == Direction.RIGHT) { 
				val = obstacleDetection.rightDistance();
			} else if (sensorDirection == Direction.LEFT) {
				val = obstacleDetection.leftDistance();
			} else {
				System.out.println("Cannot use faceAwayFromWall with Direction == FRONT currently");
			}
			val = dFilter.filter(val);
			pause(40);
		} while(val < 50 || val > 245);
		driver.stop();
	}
	
	private void faceWall() {
		// Turn until facing a wall
		obstacleDetection.setRunning(true);
		driver.turn(Direction.RIGHT);
		while(!obstacleDetection.isFrontObstacle()) {
			pause(20);
		}
		driver.stop();
		driver.turn(Direction.RIGHT, 90);
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
