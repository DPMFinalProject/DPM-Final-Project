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

/**
 *	This will take take of everything that deals with the colorsensors
 *	start this class in a thread at the beginning of any class interacting with the color sensors
 * @author GregoryBrookes, Auguste
 */

public class GridManager extends SensorManager {
	
	private final FilteredColorSensor leftCS = new FilteredColorSensor(SensorPort.S1, new DifferentialFilter(2));
	private final FilteredColorSensor rightCS = new FilteredColorSensor(SensorPort.S2, new DifferentialFilter(2));
	private final double LINE_THRESHOLD = 1.5;
	private boolean leftCSOnLine = false;
	private boolean rightCSOnLine = false;
	private final double[] leftSensorCoor = {-4.8, 6.5};//{x, y}
	private final double[] rightSensorCoor = {4.8, 6.5};//{x, y}
	
	private GridManager () {
		
	}
	
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
		else if (leftCSMeasure > LINE_THRESHOLD) {
			setLeftCSDetected(false);
		}

		double rightCSMeasure = rightCS.getFilteredData();

		if (rightCSMeasure < -LINE_THRESHOLD) {
			setRightCSDetected(true);
		}
		else if (rightCSMeasure > LINE_THRESHOLD) {
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
	
///**
// * 	Returns boolean condition if just entered a line
// * @return Return true if just ENTERED a line
// */
//	public boolean hasEnterLine( ){
//		return lineCrossingCondition(-lineCrossingThreshold, true);
//	}
//	/**
//	 * 	Returns boolean condition if just exited a line
//	 * @return Return true if just EXITED a line
//	 */
//	public boolean hasExitLine(){
//		return lineCrossingCondition(lineCrossingThreshold, false);
//	}
//	
//	//returns boolean value of a change in light happenning
//	private boolean lineCrossingCondition(double treshold, boolean below){
//		double val;
//		val= cs.getFilteredData();
//		try {	Thread.sleep(10);	} catch (InterruptedException e) {}
//		
//		if(below){			
//			if(val < treshold ){	return true;	}
//		}else{
//			if(val > treshold ){	return true;	}
//		}
//		
//		return false;
//		
//		
//	}
//		
//	/**
//	 * Update position to the midpoint between the line entry and exit
//	 * @param pos
//	 */
//	public void getPosMidLineCrossing(double[] pos){
//		double[] lineEnter = new double[3];
//		double[] lineExit = new double[3];
//		
//		while(!hasEnterLine()){
//		}
//		odo.getPosition(lineEnter);
//		
//		while(!hasExitLine()){
//		}
//		odo.getPosition(lineExit);
//	
//		for(int i=0; i<pos.length;i++){
//			pos[i]=(lineEnter[i]+lineExit[i])/2;
//		}
//	}
}
	
