/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Test.java
 *	Created On:	Mar 4, 2015
 *	
 */

package util;

import lejos.nxt.SensorPort;
import navigation.odometry.Odometer;
import sensors.FilteredColorSensor;
import sensors.filters.DifferentialFilter;
import util.Direction;

//TODO: Find out what where does the light sensor detects a line (+- constant)
//		add the corrected values depending on position of sensor

/**
 *	This will take take of everything that deals with the gridLines
 * @author GregoryBrookes
 */

public class GridManager implements Runnable{
	private final FilteredColorSensor cs = new FilteredColorSensor(SensorPort.S1, new DifferentialFilter(2));
	private final double LINE_THRESHOLD = 2;
	private boolean lineDetected = false;
	private final double[] leftSensorCoor = {-4.8, 6.5};//{x, y}
	private final double[] rightSensorCoor = {4.8, 6.5};//{x, y}
	
	
	@Override
	public void run() {
		while(true) {
			
			double data = cs.getFilteredData();
			
			if (data < -LINE_THRESHOLD) {
				setLineDetected(true);
			}
			else if (data > LINE_THRESHOLD) {
				setLineDetected(false);
			}
			
			pause(20);
		}
	}
	
	private void setLineDetected(boolean detected) {
		lineDetected = detected;
	}
	
	public boolean isOnLine() {
		return lineDetected;
	}
	
	public double[] getSensorCoor(Direction direction) {
		if (direction == Direction.RIGHT) {
			return rightSensorCoor;
		}
		else {
			return leftSensorCoor;
		}
	}
	
	private void pause(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {}
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
	
