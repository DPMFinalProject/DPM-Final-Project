/**
a *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	LSLocalizationIntercept.java
 *	Created On:	Feb 26, 2015
 */
package navigation.localization;

import lejos.nxt.Sound;
import sensors.managers.GridManager;
import util.Direction;
import util.SensorID;
import navigation.Driver;
import navigation.Navigation;
import navigation.odometry.Odometer;
import static util.Utilities.pause;

/**
 * 	Performs localization using the light sensor by positioning the robot perpendicular to a line
 * @author Gregory Brookes
 */

public class LSLocalizationIntercept extends Localization {
	final GridManager grid = GridManager.getGridManager();
	private SensorID triggeredSensor , untriggeredSensor;
	private double[] sensorCoor = new double[2];
	
	public LSLocalizationIntercept(Odometer odo, Navigation nav) {
		super(odo, nav);
		(new Thread(grid)).start();
	}

	/**
	 * Find out the position by intercepting a line and rotating 
	 * until both sensor and ON the line. 
	 * @see navigation.localization.Localization#doLocalization()
	 */
	@Override
	public void doLocalization() {	
		
		/*
		 * Execute the line intercept 3 times for a better position
		 */
		perpendicularToLine(3);
		grid.setRunning(false);
		odo.setX(0);
		odo.setY(0);
		odo.setTheta(0);
	}
		
	//runs the algorithm a set number of times.
	private void perpendicularToLine(int efficiency){
		for(int i=0; i<efficiency; i++){
			//Set up for the next interception
			Driver.move(-2);
			triggeredSensor = SensorID.NONE;
			Driver.move(Direction.FWD);
			perpendicularToLine();
		}
	}
	
	private void perpendicularToLine(){
		while(triggeredSensor == SensorID.NONE) {
			triggeredSensor = grid.whichSensorDetected();
		}
		Driver.stop();
		
		if(triggeredSensor == SensorID.BOTH){
			return;
		}
		untriggeredSensor = findUntriggeredSensor();
		
		/*
		 * Rotate only the motor the side of the untriggered sensor.
		 */
		rotateToLine();
		
		 
		while(!grid.isOnLine(untriggeredSensor)) {
			pause(10);
		}
		Driver.stop();
		pause(200);
		
	}
	
	private void rotateToLine() {
		if(triggeredSensor == SensorID.LEFT){
			Driver.moveOneMotor(Direction.RIGHT,Direction.FWD);
		}else if(triggeredSensor == SensorID.RIGHT){
			Driver.moveOneMotor(Direction.LEFT,Direction.FWD);
		}
		
	}

	private SensorID findUntriggeredSensor() {
		if(triggeredSensor == SensorID.RIGHT){
			return SensorID.LEFT;
		}
		return SensorID.RIGHT;
	}

	

}