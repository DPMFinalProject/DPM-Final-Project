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
	
	private enum Line {xAxis, yAxis};
	
	final double SIZE_OF_TILE = 30.48;
	final GridManager gridMana = new GridManager();
	
	//Orientationcorrection flags and position variables
	private boolean rightCrossed = false, leftCrossed = false;
	private Line axisCrossed;
	private double[] firstCross = new double[3];
	
	//stall flag - stall() should be called whenever the robot is turning
	private boolean stall = false;
	
	public CorrectionLightSensorSS(Odometer odo) {
		super(odo);
		(new Thread(gridMana)).start();
	}
	
	@Override
	public void run() {
		while(true)
		{
			while(stall) {
				pause(100);
			}
			
			while(!gridMana.lineDetected()) {
				pause(10);
			}
			
			System.out.println("correcting...");
			
			correctOrientation();
			
			if (rightCrossed && leftCrossed) {
				correctPosition();
				setFlags(false);
			}
		}
	}
	
	public void stall() {
		//set all orientation correction flags to default
		setFlags(false);
		
		//stall
		stall = true;
	}
	
	public void resume() {
		stall = false;
	}
	
	private void correctOrientation() {
		SensorID sensor = gridMana.whichSensorDetected();
		
		switch (sensor) {
		case BOTH:
			//robot is traveling perpendicular to the detected line
			//set theta accordingly
			
			setFlags(true);
			
			double thetaError = odo.getTheta()%90;
			
			double newTheta = (thetaError > 45) ? (odo.getTheta() + (90 - thetaError)) : (odo.getTheta() - thetaError);
			
			odo.setTheta(newTheta);
			
		case RIGHT:
			rightCrossed = true;
			
			if (!leftCrossed) {
				axisCrossed = whichLineCrossed();
				odo.getPosition(firstCross);
			}
			else if (axisCrossed == whichLineCrossed()) {
				//correct angle
			}
			else {
				return;
			}
		
		case LEFT:
			leftCrossed = true;
			
			if (!rightCrossed) {
				leftCrossed = true;
				axisCrossed = whichLineCrossed();
				odo.getPosition(firstCross);
			}
			else if (axisCrossed == whichLineCrossed()) {
				//correct angle
			}
			else {
				return;
			}
		
		default: 
			System.out.println("no line detected");
		}
	}
	
	private void correctPosition() {
		
		double[] sensorPos = getSensorPos(gridMana.getSensorCoor(gridMana.whichSensorDetected()));
		
		if(whichLineCrossed(sensorPos) == Line.xAxis) {
			
			double yError = (sensorPos[1] % SIZE_OF_TILE) - (SIZE_OF_TILE/2);
			
			//System.out.println("correcting y to:"+odo.getY()+" - "+yError);
			
			odo.setY(odo.getY()-yError);
		}
		else {
		//detected y-axis line
			double xError = (sensorPos[0] % SIZE_OF_TILE) - (SIZE_OF_TILE/2);
			
			//System.out.println("correcting x to:"+odo.getX()+" - "+xError);
			
			odo.setX(odo.getX()-xError);
		}
	}
	
	private Line whichLineCrossed() {
		return whichLineCrossed(getSensorPos(gridMana.getSensorCoor(gridMana.whichSensorDetected())));
	}
	
	private Line whichLineCrossed(double[] sensorPos) {
		
		if(isCloserToLine(sensorPos[1], sensorPos[0])) {
			return Line.xAxis;
		}
		else {
			return Line.yAxis;
		}
	}
	
	private double[] getSensorPos(double[] sensorCoor) {
		
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
	
	private double[] convertToPolar(double[] cartCoor) {
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
	
	private void setFlags(boolean bool)
	{
		rightCrossed = bool;
		leftCrossed = bool;
	}
	
	private void pause(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {}
	}
}