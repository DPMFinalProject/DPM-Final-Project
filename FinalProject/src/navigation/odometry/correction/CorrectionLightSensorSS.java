/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	CorrectionLightSensorSS.java
 *	Created On:	Mar 04, 2015
 */
package navigation.odometry.correction;

import lejos.nxt.SensorPort;
import navigation.odometry.Odometer;
import sensors.FilteredColorSensor;
import sensors.filters.DifferentialFilter;
import util.Direction;

/**
 * Odometry correction assuming two light sensors both placed at the front of the robot
 * @author Auguste
 */
public class CorrectionLightSensorSS extends OdometryCorrection {
	
	FilteredColorSensor csRight, csLeft;
	final double SIZE_OF_TILE = 30.48;
	private final double[] leftSensorCoor = {-4.8, 6.5};//{x, y}
	private final double[] rightSensorCoor = {4.8, 6.5};//{x, y}
	
	public CorrectionLightSensorSS(Odometer odo) {
		super(odo);
		csLeft = new FilteredColorSensor(SensorPort.S1,new DifferentialFilter(2));
		csRight = new FilteredColorSensor(SensorPort.S2,new DifferentialFilter(2));
	}
	
	@Override
	public void run() {
		while(true)
		{
			while(!hasDetectedLine()) {
				pause(20);
			}
			
			Direction csDir = whichSensorDetected();
			
			if(csDir == Direction.RIGHT) {
			//csRight detected line
				correctPos(rightSensorCoor);
			}
			else {
			//csLeft detected line
				correctPos(leftSensorCoor);
			}
		}
	}
	
	public boolean hasDetectedLine() {
		if(csRight.getFilteredData()>5 || csLeft.getFilteredData()>5) {
			return true;
		}
		return false;
	}
	
	public Direction whichSensorDetected()
	{
		if(csRight.getFilteredData()>5) {
			return Direction.RIGHT;
		}
		else {
			return Direction.LEFT;
		}
	}
	
	public void correctPos(double[] sensorCoor) {
		
		double[] sensorPos = getSensorPos(sensorCoor);
		
		if(sensorPos[0]%SIZE_OF_TILE > sensorPos[1]%SIZE_OF_TILE) {
		//detected x-axis line
			double yError = (sensorPos[1] % SIZE_OF_TILE) - (SIZE_OF_TILE/2);
			
			odo.setY(odo.getY()-yError);
		}
		else {
		//detected y-axis line
			double xError = (sensorPos[0] % SIZE_OF_TILE) - (SIZE_OF_TILE/2);
			
			odo.setX(odo.getX()-xError);
		}
	}
	
	public double[] getSensorPos(double[] sensorCoor) {
		
		double[] sensorPolar = convertToPolar(sensorCoor);
		double[] sensorPos = new double[2];
		
		double sensorTheta = odo.getTheta() + sensorPolar[1];
		
		sensorPos[0] = odo.getX() + sensorPolar[0]*Math.cos(sensorTheta);
		sensorPos[1] = odo.getY() + sensorPolar[0]*Math.sin(sensorTheta);
		
		return sensorPos;
	}
	
	public double[] convertToPolar(double[] cartCoor) {
		double angle = Math.atan(cartCoor[1]/cartCoor[0]);
		double distance = Math.sqrt(Math.pow(cartCoor[0], 2)+Math.pow(cartCoor[1],2));
		double[] result = {distance, angle};
		return result;//{distance, angle}
	}
	
	private void pause(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {}
	}
}
