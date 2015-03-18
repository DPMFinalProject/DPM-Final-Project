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
	
	private final FilteredColorSensor leftCS = new FilteredColorSensor(SensorPort.S4, new DifferentialFilter(2));
	private final FilteredColorSensor rightCS = new FilteredColorSensor(SensorPort.S1, new DifferentialFilter(2));
	private final double LINE_THRESHOLD = 2.0;
	private final double NO_LINE_THRESHOLD = 0.4;
	private boolean leftCSOnLine = false;
	private boolean rightCSOnLine = false;
	private final double[] leftSensorCoor = {-7.5, 6.0};//{x, y}
	private final double[] rightSensorCoor = {7.5, 6.0};//{x, y}
	
	private GridManager() {}
	
	public static GridManager getGridManager() { 
		if (gridManager == null) {
			gridManager = new GridManager();
			gridManager.start();
		}
		
		return gridManager;
	}
	
	@Override
	public void execute() {
		
		double leftCSMeasure = leftCS.getFilteredData();

		if (leftCSMeasure < -LINE_THRESHOLD) {
			setLeftCSDetected(true);
		}
		else if (leftCSMeasure > NO_LINE_THRESHOLD) {
			setLeftCSDetected(false);
		}

		double rightCSMeasure = rightCS.getFilteredData();

		if (rightCSMeasure < -LINE_THRESHOLD) {
			setRightCSDetected(true);
		}
		else if (rightCSMeasure > NO_LINE_THRESHOLD) {
			setRightCSDetected(false);
		}

		pause(30);
	}
	
	private void setLeftCSDetected(boolean detected) {
		leftCSOnLine = detected;
	}
	
	private void setRightCSDetected(boolean detected) {
		rightCSOnLine = detected;
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
	
	public double[] getSensorCoor(SensorID ID) {
		if (ID == SensorID.LEFT) {
			return leftSensorCoor;
		}
		else {
			return rightSensorCoor;
		}
	}
	
	public boolean isOnLine(SensorID ID){
		if (ID == SensorID.BOTH) {
			return (rightCSOnLine && leftCSOnLine);
		}
		else if (ID == SensorID.RIGHT) {
			return rightCSOnLine;
		}
		else if (ID == SensorID.LEFT) {
			return leftCSOnLine;
		}
		else {
			return (!rightCSOnLine && !leftCSOnLine);
		}
	}
}