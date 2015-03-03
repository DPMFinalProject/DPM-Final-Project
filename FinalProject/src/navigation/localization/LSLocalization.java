/**
a *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	LSLocalization.java
 *	Created On:	Feb 26, 2015
 */
package navigation.localization;

import lejos.nxt.Sound;
import sensors.FilteredColorSensor;
import sensors.FilteredSensor;
import util.Direction;
import navigation.Driver;
import navigation.Navigation;
import navigation.odometry.Odometer;

/**
 * 	Performs localization using the light sensor
 * @author Oleg
 */
public class LSLocalization extends Localization {
	private FilteredColorSensor cs;
	
	private final double CS_DIST = 11.7;
	
	private double[] pos = new double[3];
	private double[]lineAngle = new double [4];
	
	public LSLocalization(Odometer odo, Driver driver, Navigation nav) {
		super(odo, driver, nav);
	}

	/**
	 * @see navigation.localization.Localization#doLocalization()
	 */
	@Override
	public void doLocalization() {
		/*
		 *  Remember to instantiate a desired FilteredSensor with your desired filters
		 *  You also have access to the odometer and driver from the parent class.
		 */
		
		// Initialize Color Sensor yourself
		
		//driver.move(/*-2, -2*/);
		//driver.turn(/* 0 */);
		//driver.setSpeed(-20); //rotate counter clockwise
		nav.travelTo(-2, -2, 0);
		driver.turn(Direction.LEFT, 360); // make one full CCW turn 
		
		getlineAngle(lineAngle);
		
		updateOdometer(lineAngle,pos);
	}
	
	
	//Utilities methods
	private double[] filterAnalysis(){
		double[] filteredAngles= new double [2];
		double angleA = cs.getFilteredData(), angleB = angleA;
		
		while (angleA > 0 && angleB > 0){
			angleA = angleB;
			angleB = cs.getFilteredData();
		}
		
		filteredAngles[0]=odo.getTheta();
		angleA=angleB;
		
		while (angleA < 0 && angleB < 0){
			angleA = angleB;
			angleB = cs.getFilteredData();
		}
		filteredAngles[1] = odo.getTheta();
		
		return filteredAngles;
	}
	
	private void getlineAngle(double[] lineAngle){
		while(driver.isMoving()){
			for(byte i=3; i>=0; i--){
				lineAngle[i]=getLineAngleMid(filterAnalysis(), i);

			try {	Thread.sleep(1000);	} catch (InterruptedException e) {}
			}
			driver.setSpeed(0);
		}
	}
	
	private double getLineAngleAvg( double[] filteredAngles){
		return (filteredAngles[0] + filteredAngles[1])/2;
	}
	
	private double getLineAngleMid(double[] filteredAngles, byte lineNumber){
		//vertical line, assuming counterclockwise , facing north
		if(lineNumber == 0 || lineNumber == 2){
			return Math.acos( ( Math.cos(filteredAngles[0])+Math.cos(filteredAngles[1]) ) /2);
		//horizontal line
		}else {
			return Math.acos( ( Math.sin(filteredAngles[0])+Math.sin(filteredAngles[1]) ) /2);
		}
	}

	private void updateOdometer(double[] lineAng, double[] pos){
		odo.getPosition(pos);	
		odo.setPosition(new double [] {correctX(lineAng), correctY(lineAng),
				delTheta(lineAng)+pos[2]}, new boolean [] {true, true, true});
	}
	private double delTheta(double[] lineAng){
		return 270-((lineAng[0]+lineAng[2])/2);
	}
	private double correctX(double[] lineAng){
		return -CS_DIST*Math.cos(Math.toRadians( (lineAng[0]-lineAng[2])/2 ) );
	}
	private double correctY(double[] lineAng){
		return CS_DIST*Math.cos(Math.toRadians( ( lineAng[1]+360-lineAng[3])/2) );
	}
}
