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
import util.SensorID;
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
			
			System.out.println("correcting...");
			
			SensorID csDir = gridMana.whichSensorDetected();
			
			System.out.println("direction..."+csDir);
			
			correctPos(gridMana.getSensorCoor(csDir));
		}
	}
	
	public void correctPos(double[] sensorCoor) {
		
		double[] sensorPos = getSensorPos(sensorCoor);
		
		System.out.println("SensorPos: "+sensorPos[0]+", "+sensorPos[1]);
		
		if(isCloserToLine(sensorPos[1], sensorPos[0])) {
		//detected x-axis line
			double yError = (sensorPos[1] % SIZE_OF_TILE) - (SIZE_OF_TILE/2);
			
			System.out.println("correcting y to:"+odo.getY()+" - "+yError);
			
			odo.setY(odo.getY()-yError);
		}
		else {
		//detected y-axis line
			double xError = (sensorPos[0] % SIZE_OF_TILE) - (SIZE_OF_TILE/2);
			
			System.out.println("correcting x to:"+odo.getX()+" - "+xError);
			
			odo.setX(odo.getX()-xError);
		}
	}
	
	public double[] getSensorPos(double[] sensorCoor) {
		
		double[] sensorPolar = convertToPolar(sensorCoor);
		double[] sensorPos = new double[2];
		
		double sensorTheta = Math.toRadians(odo.getTheta()) + sensorPolar[1];
		
		System.out.println("sensorPolarTheta: "+Math.toDegrees(sensorPolar[1]));
		System.out.println("odoTheta: "+ odo.getTheta());
		System.out.println("sensorTheta: "+Math.toDegrees(sensorTheta));
		
		sensorPos[0] = odo.getX() + sensorPolar[0]*Math.sin(sensorTheta);
		sensorPos[1] = odo.getY() + sensorPolar[0]*Math.cos(sensorTheta);
		
		return sensorPos;
	}
	
	public double[] convertToPolar(double[] cartCoor) {
		double angle = Math.atan(cartCoor[0]/cartCoor[1]);
		
		angle = (cartCoor[0] < 0) ? (angle - 180) : angle;
		
		double distance = Math.sqrt(Math.pow(cartCoor[0], 2)+Math.pow(cartCoor[1],2));
		double[] result = {distance, angle};
		return result;
	}
	
	private boolean isCloserToLine(double thisOne, double thatOne) {
		//is thisOne closer to the line than thatOne
		return Math.abs((thisOne % SIZE_OF_TILE)-(SIZE_OF_TILE/2)) < Math.abs((thatOne % SIZE_OF_TILE)-(SIZE_OF_TILE/2));
	}
	
	private void pause(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {}
	}
}
