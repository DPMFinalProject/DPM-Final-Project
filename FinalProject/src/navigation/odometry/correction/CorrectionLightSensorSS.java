/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	CorrectionLightSensorSS.java
 *	Created On:	Mar 04, 2015
 */
package navigation.odometry.correction;

import navigation.odometry.Odometer;
import util.Direction;
import util.GridManager;

/**
 * Odometry correction assuming two light sensors both placed at the front of the robot
 * @author Auguste
 */
public class CorrectionLightSensorSS extends OdometryCorrection {
	
	final double SIZE_OF_TILE = 30.48;
	final GridManager gridMana = new GridManager();
	
	public CorrectionLightSensorSS(Odometer odo) {
		super(odo);
		(new Thread(gridMana)).start();
	}
	
	@Override
	public void run() {
		while(true)
		{
			while(!gridMana.lineDetected()) {
				pause(10);
			}

			Direction csDir = gridMana.whichSensorDetected();
		
			correctPos(gridMana.getSensorCoor(csDir));
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
