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
import sensors.managers.GridManager;
import util.SensorID;

/**
 * Odometry correction assuming <<TWO>> light sensors both placed at the <<FRONT>> of the robot
 * @author Auguste
 */
public class CorrectionLightSensorSS extends OdometryCorrection {
	
	private enum Line {xAxis, yAxis};
	
	final double SIZE_OF_TILE = 30.48;
	final GridManager grid;
	
	//Orientationcorrection flags and position variables
	private boolean rightCrossed = false, leftCrossed = false;
	private Line axisCrossed;
	private double[] firstCross = new double[3];
	private double[] secondCross = new double[3];
	
	//stall flag - stall() should be called whenever the robot is turning
	private boolean stall = false;
	
	public CorrectionLightSensorSS(Odometer odo) {
		super(odo);
		grid = GridManager.getGridManager();
	}
	
	@Override
	public void run() {
		while(true)
		{
			while(stall) {
				pause(100);
			}
			
			while(!grid.lineDetected()) {
				pause(10);
			}
			
			correctOrientation();
			
			if (rightCrossed && leftCrossed) {
				
				correctPosition();
				setFlags(false);
				
				pause(100);
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
		SensorID sensor = grid.whichSensorDetected();
		System.out.println("correcting... orientation");
		
		System.out.println(""+sensor+" sensor(s) detected");
		
		switch (sensor) {
			case BOTH:
				setFlags(true);

				double newTheta = Math.round(odo.getTheta()/90)*90;
				
				System.out.println("correcting to: "+newTheta);
				
				odo.setTheta(newTheta);

				break;	
			case RIGHT:
				rightCrossed = true;

				if (!leftCrossed) {
					axisCrossed = whichLineCrossed();
					odo.getPosition(firstCross);
				}
				else if (axisCrossed == whichLineCrossed()) {
					odo.getPosition(secondCross);

					double distanceTravelled = euclideanDistance(firstCross, secondCross);
					double sensorSeperation = euclideanDistance(grid.getSensorCoor(SensorID.LEFT), grid.getSensorCoor(SensorID.RIGHT));
					
					double odoTheta = odo.getTheta()%90;
					double correctionTheta = Math.toDegrees(Math.atan(distanceTravelled/sensorSeperation));
					
					double thetaError;
					
					if (odoTheta < correctionTheta) {
						thetaError = odoTheta - correctionTheta;
					}
					else {
						thetaError = correctionTheta - odoTheta;
					}
					
					System.out.println("correcting to: "+ (odo.getTheta() - thetaError));
					
					odo.setTheta(odo.getTheta() - thetaError);
				}
				else {
					System.out.println("detected different lines... reseting");
				}

				break;
			case LEFT:
				leftCrossed = true;

				if (!rightCrossed) {
					leftCrossed = true;
					axisCrossed = whichLineCrossed();
					odo.getPosition(firstCross);
				}
				else if (axisCrossed == whichLineCrossed()) {
					odo.getPosition(secondCross);

					double distanceTravelled = euclideanDistance(firstCross, secondCross);
					double sensorSeperation = euclideanDistance(grid.getSensorCoor(SensorID.LEFT), grid.getSensorCoor(SensorID.RIGHT));

					double odoTheta = odo.getTheta()%90;
					double correctionTheta = 90 - Math.toDegrees(Math.atan(distanceTravelled/sensorSeperation));
					
					double thetaError;
					
					if (odoTheta < correctionTheta) {
						thetaError = correctionTheta - odoTheta;
					}
					else {
						thetaError = odoTheta - correctionTheta;
					}
					
					System.out.println("correcting to: "+ (odo.getTheta() - thetaError));
					
					odo.setTheta(odo.getTheta() - thetaError);
				}
				else {
					System.out.println("detected different lines... reseting");
				}

				break; 
			case NONE:
				System.out.println("ERROR: no line detected");
				break;
		}
	}
	
	private void correctPosition() {
		
		double[] sensorPos = getSensorPos(grid.getSensorCoor(grid.whichSensorDetected()));
		
		System.out.println("correcting... position");
		
		if(whichLineCrossed(sensorPos) == Line.xAxis) {
			
			double correction = sensorPos[1] % SIZE_OF_TILE;
			
			double yError;
			
			if (correction > (SIZE_OF_TILE/2)) {
				yError = correction - SIZE_OF_TILE;
			}
			else {
				yError = correction;
			}
			
			System.out.println("correcting y to:"+odo.getY()+" - "+yError);
			
			odo.setY(odo.getY() - yError);
		}
		else {
			
			double correction = sensorPos[1] % SIZE_OF_TILE;
			
			double xError;
			
			if (correction > (SIZE_OF_TILE/2)) {
				xError = correction - SIZE_OF_TILE;
			}
			else {
				xError = correction;
			}
			
			System.out.println("correcting x to: "+odo.getX()+" - "+xError);
			
			odo.setX(odo.getX() - xError);
		}
	}
	
	private Line whichLineCrossed() {
		return whichLineCrossed(getSensorPos(grid.getSensorCoor(grid.whichSensorDetected())));
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
		
		sensorPos[0] = odo.getX() + sensorPolar[0]*Math.sin(sensorTheta);
		sensorPos[1] = odo.getY() + sensorPolar[0]*Math.cos(sensorTheta);
		
		return sensorPos;
	}
	
	private double[] convertToPolar(double[] cartCoor) {
		double distance = Math.sqrt(Math.pow(cartCoor[0], 2)+Math.pow(cartCoor[1],2));
		double angle = Math.atan(cartCoor[0]/cartCoor[1]);
		
		double[] result = {distance, angle};
		return result;
	}
	
	private double euclideanDistance(double[] posA, double[] posB) {
		return Math.sqrt(Math.pow(posA[0]-posB[0], 2) + Math.pow(posA[1]-posB[1], 2));
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