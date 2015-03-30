/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	CorrectionLightSensorSS.java
 *	Created On:	Mar 04, 2015
 */
package navigation.odometry.correction;

import navigation.Driver;
import navigation.odometry.Odometer;
import sensors.managers.GridManager;
import util.Measurements;
import util.SensorID;
import static util.Utilities.*;

/**
 * Odometry correction assuming two light sensors both placed at the front of the robot
 * this class corrects both odometry position and orientation.
 * @author Auguste
 */
public class CorrectionLightSensorSS extends OdometryCorrection {
	
	private enum Line {xAxis, yAxis, unsure};
	
	private final GridManager grid;
	
	// Orientation correction flags and position variables
	private boolean rightCrossed = false, leftCrossed = false;
	private boolean waitingForSecondCross = false;
	private Line axisCrossed;
	private double[] firstCross = new double[3];
	private double[] secondCross = new double[3];
	
	
	public CorrectionLightSensorSS(Odometer odo) {
		super(odo);
		grid = GridManager.getGridManager();
	}
	
	@Override
	public void run() {
		while(true) {
			 
			// Wait for a line
			while(!grid.lineDetected()) {
				pause(10);
				
				while(Driver.isTurning() || Driver.isMovingBackwards() || Driver.isDrifting()) {
					pause(100);
					setFlags(false);
				}
			}
			
			SensorID sensor = grid.whichSensorDetected();
			
			correctOrientation(sensor);
			
			if (rightCrossed && leftCrossed) {
				// Position correction is only called once both sensors have been
				// detected by orientation correction so as to not affect the distance
				// traveled used in theta calculations
				
				correctPosition(sensor);
				
				setFlags(false);
				
				pause(100);
			}
		}
	}
	
	/**
	 * corrects orientation:
	 * this is done in a two step process:
	 * 1. when 1 sensor detects a line, its ID is passed and the sensor position is recorded
	 * flags are set to indicate that one sensor has detected a line
	 * 2. when the second sensor detects a line, its ID is passed and its position is calculated
	 * using the sensor separation and distance traveled by the robot between the two detections,
	 * the orientation of the robot is calibrated.
	 * 
	 * the flags are reset externally
	 * 
	 * @param sensor ID of sensor that has just detected a line
	 */
	private void correctOrientation(SensorID sensor) {
		
		sensor = checkIfCrossed(sensor);
		
		switch (sensor) {
		case BOTH:
			setFlags(true);

			Line rightLineCrossed = whichLineCrossed(SensorID.RIGHT);
			Line leftLineCrossed = whichLineCrossed(SensorID.LEFT);

			if (rightLineCrossed != leftLineCrossed || rightLineCrossed == Line.unsure) {
				break;
			}

			double newTheta = Math.round(odo.getTheta()/90)*90;

			odo.setTheta(newTheta);

			break;

		case RIGHT:
			rightCrossed = true;

			errorCheck();

			if (!leftCrossed) {
				axisCrossed = whichLineCrossed(sensor);

				if (axisCrossed == Line.unsure) {
					setFlags(true);
					break;
				}

				odo.getPosition(firstCross);
			}
			else if (axisCrossed == whichLineCrossed(sensor)) {
				odo.getPosition(secondCross);

				double roundedTheta = Math.round(odo.getTheta()/90.0)*90;

				odo.setTheta(roundedTheta + thetaOffSet());
			}

			break;

		case LEFT:
			leftCrossed = true;

			errorCheck();

			if (!rightCrossed) {
				axisCrossed = whichLineCrossed(sensor);

				if (axisCrossed == Line.unsure) {
					setFlags(true);
					break;
				}

				odo.getPosition(firstCross);
			}
			else if (axisCrossed == whichLineCrossed(sensor)) {
				odo.getPosition(secondCross);

				double roundedTheta = Math.round(odo.getTheta()/90.0)*90;

				odo.setTheta(roundedTheta - thetaOffSet());
			}

			break;

		case NONE: 	//no line detected
			break;
		}
	}
	
	/**
	 * Calculates theta offset from 0 degrees, based on distance traveled between two line detections
	 * 
	 * @return theta offset from 0 degree heading
	 */
	private double thetaOffSet() {
		double distanceTravelled = euclideanDistance(firstCross, secondCross);
		double sensorSeperation = euclideanDistance(grid.getSensorCoor(SensorID.LEFT), grid.getSensorCoor(SensorID.RIGHT));

		double thetaOffSet = Math.toDegrees(Math.atan(distanceTravelled/sensorSeperation));
		
		return thetaOffSet;
	}
	
	/**
	 * Checks if the sensor has already crossed a line
	 * and returns a sensor which has not crossed if possible
	 * 
	 * @param sensor which has just detected a line
	 * @return sensor which has just detected a line but has not yet been taken into account
	 */
	private SensorID checkIfCrossed(SensorID sensor) {
		if (sensor == SensorID.BOTH && rightCrossed) {
			return SensorID.LEFT;
		}
		else if (sensor == SensorID.BOTH && leftCrossed) {
			return SensorID.RIGHT;
		}
		else if (sensor == SensorID.RIGHT && rightCrossed) {
			pause(10);
			return SensorID.NONE;
		}
		else if (sensor == SensorID.LEFT && leftCrossed) {
			pause(10);
			return SensorID.NONE;
		}
		else {
			return sensor;
		}
	}
	
	/**
	 * Corrects robot position based on which sensor has just crossed a line
	 * 
	 * @param sensor which has just crossed a line
	 */
	private void correctPosition(SensorID sensor) {
		
		double[] sensorPos = getSensorPos(grid.getSensorCoor(sensor));
		
		if(whichLineCrossed(sensorPos) == Line.xAxis) {
			
			if (isParallelToX()) {
				return;
			}
			
			double yError = positionError(sensorPos[1]);
			
			odo.setY(odo.getY() - yError);
		}
		else if (whichLineCrossed(sensorPos) == Line.yAxis) {
			
			if (isParallelToY()) {
				return;
			}
			
			double xError = positionError(sensorPos[0]);
			
			odo.setX(odo.getX() - xError);
		}
	}
	
	/**
	 * Calculates position error of the robot based on one sensor global coordinate (i.e x or y)
	 * assumed to be called when sensor has just crossed a line
	 * 
	 * @param sensorPos	sensor global coordinate corresponding to one dimension
	 * @return position error in the given dimension
	 */
	private double positionError(double sensorPos) {
		double error = sensorPos % Measurements.TILE;
		
		if (error > (Measurements.TILE/2)) {
			error -= Measurements.TILE;
		}
		else if (error < (-Measurements.TILE/2)) {
			error += Measurements.TILE;
		}
		
		return error;
	}
	
	/**
	 * Same as below, but will calculate sensor global coordinates based on SensorID
	 * 
	 * @param sensor SensorID representing the sensor which has in theory just crossed a line
	 * @return
	 */
	private Line whichLineCrossed(SensorID sensor) {
		return whichLineCrossed(getSensorPos(grid.getSensorCoor(sensor)));
	}
	
	/**
	 * Approximates which line was crossed from the sensor global coordinates
	 * 
	 * @param sensorPos	sensor global coordinates
	 * @return line which was just crossed (i.e "xAxis", "yAxis") or "unsure" if the lines cannot be differentiated 
	 */
	private Line whichLineCrossed(double[] sensorPos) {
		
		double xError = Math.abs(sensorPos[0]) % Measurements.TILE;
		xError = (xError > (Measurements.TILE/2)) ? xError - Measurements.TILE : xError;

		double yError = Math.abs(sensorPos[1]) % Measurements.TILE;
		yError = (yError > (Measurements.TILE/2)) ? yError - Measurements.TILE : yError;

		double delta = Math.abs(xError) - Math.abs(yError);

		if(Math.abs(delta) < 1) {
			return Line.unsure;
		}
		else if (delta < 0) {
			return Line.yAxis;
		}
		else {
			return Line.xAxis;
		}
	}
	
	/**
	 * Return sensor position on the global coordinate system
	 * 
	 * @param sensorCoor  sensor coordinates relative to the robot origin
	 * @return array representing global sensor coordinate {x, y}
	 */
	private double[] getSensorPos(double[] sensorCoor) {
		
		double[] sensorPolar = convertToPolar(sensorCoor);
		double[] sensorPos = new double[2];
		
		double sensorTheta = Math.toRadians(odo.getTheta()) + sensorPolar[1];
		
		sensorPos[0] = odo.getX() + sensorPolar[0]*Math.sin(sensorTheta);
		sensorPos[1] = odo.getY() + sensorPolar[0]*Math.cos(sensorTheta);
		
		return sensorPos;
	}
	
	/**
	 * converts cartesian coordinates to polar coordinates {x, y} --> {r, theta}
	 * 
	 * @param cartCoor coordinates to be converted
	 * @return polar coordinates in the form {r, theta}
	 */
	private double[] convertToPolar(double[] cartCoor) {
		double distance = Math.sqrt(Math.pow(cartCoor[0], 2)+Math.pow(cartCoor[1],2));
		double angle = Math.atan(cartCoor[0]/cartCoor[1]);
		
		double[] result = {distance, angle};
		return result;
	}
	
	/**
	 * @return true if robot is within 10 degrees of being parallel to the yAxis. false otherwise 
	 */
	private boolean isParallelToY() {
		return isNear(0, odo.getTheta() % 180, 10) || isNear(180, odo.getTheta() % 180, 10);
//		if (isNear(0, odo.getTheta() % 180, 10) || isNear(180, odo.getTheta() % 180, 10)) {
//			return true;
//		}
//		return false;
	}
	
	/**
	 * @return true if robot is within 10 degrees of being parallel to the xAxis. false otherwise 
	 */
	private boolean isParallelToX() {
		return isNear(90, odo.getTheta() % 180, 10) || isNear(270, odo.getTheta() % 180, 10);
//		if (isNear(90, odo.getTheta() % 180, 10) || isNear(270, odo.getTheta() % 180, 10)) {
//			return true;
//		}
//		return false;
	}
	
	/**
	 * Calculate the Euclidean distance between two points.
	 * 
	 * @param pointA Cartesian coordinates of first point {x, y, ...}
	 * @param pointB Cartesian coordinates of second point {x, y, ...}
	 * @return distance The distance between the two points, in cm.
	 */
	private double euclideanDistance(double[] pointA, double[] pointB) {
		return Math.sqrt(Math.pow(pointA[0]-pointB[0], 2) + Math.pow(pointA[1]-pointB[1], 2));
	}
	
	/**
	 * starts a timer which will reset the orientation correction flags
	 * if the second sensor does not detect a line within 2 seconds
	 */
	private void errorCheck() {
		(new Thread() {
			public void run() {
				if (waitingForSecondCross) {
					return;
				}
				
				waitingForSecondCross = true;
				
				pause(2000);
				if (leftCrossed || rightCrossed) {
					//if either is still true. error occurred! reset flags!
					setFlags(false);
				}
			}
		}).start();
	}
	
	/**
	 * Set all orientation correction flags to value bool 
	 */
	private void setFlags(boolean bool)
	{
		rightCrossed = bool;
		leftCrossed = bool;
		waitingForSecondCross = bool;
	}
}