/**
a *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	LSLocalizationIntercept.java
 *	Created On:	Feb 26, 2015
 */
package navigation.localization;

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
	private final GridManager grid = GridManager.getGridManager();
	private SensorID triggeredSensor , untriggeredSensor;
	
	/*
	 * Distance by which the robot has to move back in order to ensure that the
	 * light sensor does not see a line that was initially directly under it. 
	 */
	private final double LS_WIDTH = 2.0;
	
	private final int ITERATIONS = 1;
	private final int LINE_CHECK_PERIOD = 10;
	
	public LSLocalizationIntercept(Odometer odo, Navigation nav) {
		super(odo, nav);
	}
	
	/**
	 * Find out the position by intercepting a line and rotating 
	 * until both sensor and ON the line. 
	 * @see navigation.localization.Localization#doLocalization()
	 */
	@Override
	public void doLocalization(double x, double y, double theta) {
		/*
		 * Increasing the iterations makes the localization more accurate but takes additional time.
		 */
		for(int i = 0; i < ITERATIONS; i++) {
			//Set up for the next interception
			Driver.move(-LS_WIDTH);
			triggeredSensor = SensorID.NONE;
			Driver.move(Direction.FWD);
			perpendicularToLine();
		}
		
		odo.setX(x);
		odo.setY(y);
		odo.setTheta(theta);
	}
	
	/**
	 * Private method that checks if the light sensors are perpendicular to a line.
	 */
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
			pause(LINE_CHECK_PERIOD);
		}
		
		Driver.stop();
		pause(200);
	}
	/**
	 * Calling this method checks which motor is on a line (triggered). 
	 * Then rotate the opposite motor until it also detects a line.
	 */
	private void rotateToLine() {
		if(triggeredSensor == SensorID.LEFT){
			Driver.moveOneMotor(Direction.RIGHT,Direction.FWD);
		}else if(triggeredSensor == SensorID.RIGHT){
			Driver.moveOneMotor(Direction.LEFT,Direction.FWD);
		}
		
	}

	private SensorID findUntriggeredSensor() {
		return triggeredSensor.opposite();
	}
}