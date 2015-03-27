/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	GridManager.java
 *	Created On:	Mar 4, 2015
 */

package sensors.managers;

import lejos.nxt.SensorPort;
import sensors.FilteredColorSensor;
import sensors.filters.DifferentialFilter;
import util.SensorID;
import static util.Utilities.pause;

/**
 *	This will take take of everything that deals with the color sensors
 *	start this class in a thread at the beginning of any class interacting with the color sensors
 * @author GregoryBrookes, Auguste
 */

public class GridManager extends SensorManager {
	
	private final int SAMPLING_RATE = 30;
	private final double LINE_THRESHOLD = 2.5;
	private final double NO_LINE_THRESHOLD = 0.4;
	
	private final FilteredColorSensor leftCS = new FilteredColorSensor(SensorPort.S4, new DifferentialFilter(2));
	private final FilteredColorSensor rightCS = new FilteredColorSensor(SensorPort.S1, new DifferentialFilter(2));
	private final double[] leftSensorCoor = {-7.5, 6.0};//{x, y}
	private final double[] rightSensorCoor = {7.5, 6.0};//{x, y}
	
	private boolean leftCSOnLine = false;
	private boolean rightCSOnLine = false;
	
	private GridManager() {}
	
	public static GridManager getGridManager() { 
		if (gridManager == null) {
			gridManager = new GridManager();
			gridManager.start();
		}
		
		gridManager.setRunning(true);
		
		return gridManager;
	}
	
	@Override
	public void execute() {
		
		double leftCSMeasure = leftCS.getFilteredData();

		if (leftCSMeasure < -LINE_THRESHOLD) {
			leftCSOnLine = true;
		}
		else if (leftCSMeasure > NO_LINE_THRESHOLD) {
			leftCSOnLine = false;
		}

		double rightCSMeasure = rightCS.getFilteredData();

		if (rightCSMeasure < -LINE_THRESHOLD) {
			rightCSOnLine = true;
		}
		else if (rightCSMeasure > NO_LINE_THRESHOLD) {
			rightCSOnLine = false;
		}

		pause(SAMPLING_RATE);
	}
	
	public boolean lineDetected() {
		if (rightCSOnLine || leftCSOnLine) {
			return true;
		}
		return false;
	}
	
	public boolean lineDetectedRS() {
		return rightCSOnLine;
	}
	
	public boolean lineDetectedLS() {
		return leftCSOnLine;
	}
	
	public SensorID whichSensorDetected()
	{
		if (rightCSOnLine && leftCSOnLine) {
			return SensorID.BOTH;
		}
		else if (rightCSOnLine) {
			return SensorID.RIGHT;
		}
		else if (leftCSOnLine) {
			return SensorID.LEFT;
		}
		else {
			return SensorID.NONE;
		}
	}
	
	public boolean isOnLine(SensorID ID){
		switch(ID) {
		case BOTH:
			return (rightCSOnLine && leftCSOnLine);
		case RIGHT:
			return rightCSOnLine;
		case LEFT:
			return leftCSOnLine;
		case NONE:
			return (!rightCSOnLine && !leftCSOnLine);
		default:
			return false;
		}
	}
	
	public double[] getSensorCoor(SensorID ID) {
		switch(ID) {
		case LEFT:
			return leftSensorCoor;
		case RIGHT:
			return rightSensorCoor;
		case NONE:
//			System.out.println("_________________________________\n"
//					+ "_________________________________\n"
//					+ "_________________________________\n"
//					+ "_________________________________\n"
//					+ "Cannot get NONE sensor coordinate"
//					+ "_________________________________\n"
//					+ "_________________________________\n"
//					+ "_________________________________\n"
//					+ "_________________________________\n");
		default:
			return rightSensorCoor;
		}
	}
}