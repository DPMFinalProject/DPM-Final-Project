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
import util.SensorID;
import static util.Utilities.*;

/**
 * Odometry correction assuming <<TWO>> light sensors both placed at the <<FRONT>> of the robot
 * @author Auguste
 */
public class CorrectionLightSensorSS extends OdometryCorrection {
	
	private enum Line {xAxis, yAxis};
	
	final double SIZE_OF_TILE = util.Measurements.TILE;
	final GridManager grid;
	
	//Orientationcorrection flags and position variables
	private boolean rightCrossed = false, leftCrossed = false;
	private boolean waitingForSecondCross = false;
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
				
				if(!Driver.isTurning()) {
					resume();
				}
			}
			
			while(!grid.lineDetected()) {
				pause(10);
				
				if(Driver.isTurning()) {
					stall();
					break;
				}
			}
			
			if (stall) {
				continue;
			}
			
			if(!isNear(odo.getTheta()%90, 45, 15)) {
				correctOrientation();
			}
			else {
				setFlags(true);
			}
			
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
//		System.out.println("correcting... orientation");
//		
//		System.out.println(""+sensor+" sensor(s) detected");
		
		if (sensor == SensorID.BOTH && rightCrossed) {
//			System.out.println("RIGHT already detected... correcting LEFT");
			
			sensor = SensorID.LEFT;
		}
		else if (sensor == SensorID.BOTH && leftCrossed) {
//			System.out.println("LEFT already detected... correcting LEFT");
			
			sensor = SensorID.RIGHT;
		}
		else if (sensor == SensorID.RIGHT && rightCrossed) {
			pause(10);
			return;
		}
		else if (sensor == SensorID.LEFT && leftCrossed) {
			pause(10);
			return;
		}
		
		switch (sensor) {
			case BOTH:
				setFlags(true);

				double newTheta = Math.round(odo.getTheta()/90)*90;
				
//				System.out.println("correcting theta to: "+newTheta);
				
				odo.setTheta(newTheta);

				break;	
			case RIGHT:
				rightCrossed = true;
				
				errorCheck();

				if (!leftCrossed) {
					axisCrossed = whichLineCrossed();
					odo.getPosition(firstCross);
				}
				else if (axisCrossed == whichLineCrossed()) {
					odo.getPosition(secondCross);

					double distanceTravelled = euclideanDistance(firstCross, secondCross);
					double sensorSeperation = euclideanDistance(grid.getSensorCoor(SensorID.LEFT), grid.getSensorCoor(SensorID.RIGHT));
					
					double odoTheta = Math.round(odo.getTheta()/90.0)*90;
					double correctionTheta = Math.toDegrees(Math.atan(distanceTravelled/sensorSeperation));
					
//					System.out.println("correcting theta to: "+ (odoTheta + correctionTheta));
					
					odo.setTheta(odoTheta + correctionTheta);
				}
				else {
//					System.out.println("detected different lines... reseting");
				}

				break;
			case LEFT:
				leftCrossed = true;
				
				errorCheck();

				if (!rightCrossed) {
					leftCrossed = true;
					axisCrossed = whichLineCrossed();
					odo.getPosition(firstCross);
				}
				else if (axisCrossed == whichLineCrossed()) {
					odo.getPosition(secondCross);

					double distanceTravelled = euclideanDistance(firstCross, secondCross);
					double sensorSeperation = euclideanDistance(grid.getSensorCoor(SensorID.LEFT), grid.getSensorCoor(SensorID.RIGHT));

					double odoTheta = Math.round(odo.getTheta()/90.0)*90;
					double correctionTheta = Math.toDegrees(Math.atan(distanceTravelled/sensorSeperation));
					
//					System.out.println("correcting theta to: "+ (odoTheta - correctionTheta));
					
					odo.setTheta(odoTheta - correctionTheta);
				}
				else {
//					System.out.println("detected different lines... reseting");
				}

				break; 
			case NONE:
//				System.out.println("ERROR: no line detected");
				break;
		}
		
//		System.out.println("------------------------");
	}
	
	private void correctPosition() {
		
		double[] sensorPos = getSensorPos(grid.getSensorCoor(grid.whichSensorDetected()));
		
//		System.out.println("correcting... position");
		
		if(whichLineCrossed(sensorPos) == Line.xAxis) {
			
//			System.out.println("Robot COOR x: "+odo.getX()+" y: "+odo.getY());
//			System.out.println("Sensor COOR x: "+sensorPos[0]+" y: "+sensorPos[1]);
			
			if (isParallelToX()) {
//				System.out.println("Tried to correct Y");
//				System.out.println("Robot is too close to being parallel to x-axis");
				return;
			}
			
			double yError = sensorPos[1] % SIZE_OF_TILE;
			
			if (yError > (SIZE_OF_TILE/2)) {
				yError -= SIZE_OF_TILE;
			}
			
//			System.out.println("correcting sensorY to: "+ (sensorPos[1]-yError));
			
			odo.setY(odo.getY() - yError);
		}
		else {
			
//			System.out.println("Robot COOR x: "+odo.getX()+" y: "+odo.getY());
//			System.out.println("Sensor COOR x: "+sensorPos[0]+" y: "+sensorPos[1]);
			
			if (isParallelToY()) {
//				System.out.println("Tried to correct X");
//				System.out.println("Robot is too close to being parallel to y-axis");
				return;
			}
			
			double xError = sensorPos[0] % SIZE_OF_TILE;
			
			if (xError > (SIZE_OF_TILE/2)) {
				xError -= SIZE_OF_TILE;
			}
			
//			System.out.println("correcting sensorX to: "+(sensorPos[0]-xError));
			
			odo.setX(odo.getX() - xError);
		}
		
//		System.out.println("------------------------");
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
	
	private boolean isParallelToY() {
		if (isNear(0, odo.getTheta() % 180, 20) || isNear(180, odo.getTheta() % 180, 20)) {
			return true;
		}
		
		return false;
	}
	
	private boolean isParallelToX() {
		if (isNear(90, odo.getTheta() % 180, 20) || isNear(270, odo.getTheta() % 180, 20)) {
			return true;
		}
		
		return false;
	}
	
	private double euclideanDistance(double[] posA, double[] posB) {
		return Math.sqrt(Math.pow(posA[0]-posB[0], 2) + Math.pow(posA[1]-posB[1], 2));
	}
	
	private boolean isCloserToLine(double thisOne, double thatOne) {
		//is thisOne closer to the line than thatOne
		
		thisOne = thisOne % SIZE_OF_TILE;
		double thisOneError = (thisOne > (SIZE_OF_TILE/2)) ? thisOne - SIZE_OF_TILE : thisOne;
		
		thatOne = thatOne % SIZE_OF_TILE;
		double thatOneError = (thatOne > (SIZE_OF_TILE/2)) ? thatOne - SIZE_OF_TILE : thatOne;
		
		return Math.abs(thisOneError) < Math.abs(thatOneError);
	}
	
	private void errorCheck() {
		(new Thread() {
			public void run() {
				if (waitingForSecondCross) {
					return;
				}
				
				waitingForSecondCross = true;
				
//				System.out.println("error checking");
				
				pause(1500);//wait 1.5 seconds
				if (leftCrossed || rightCrossed)//if either is still true. error occurred! reset flags!
//					System.out.println("error occured, reseting flags...");
					setFlags(false);
			}
		}).start();
	}
	
	private void setFlags(boolean bool)
	{
		rightCrossed = bool;
		leftCrossed = bool;
		waitingForSecondCross = bool;
	}
}