/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	OdometerCorrection.java
 *	Created On:	Feb 26, 2015
 */
package navigation.odometry.correction;

import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import navigation.Driver;
import navigation.Navigation;
import navigation.odometry.Odometer;
import sensors.FilteredColorSensor;
import sensors.filters.DifferentialFilter;

/**
 * Uses light sensors to correct the odometer's coordinates.
 * Should not have ties to any other classes except the odometer and the light sensors.
 * @author Gregory Brookes
 */
public class CorrectionLightSensorFB extends OdometryCorrection {
	
	private FilteredColorSensor csForward,csBackward;
	private final double CSF_DIST = 11.7;
	private final double CSB_DIST=5;
	private double[] pos = new double[3];
	private static final long CORRECTION_PERIOD = 10;
	
	public CorrectionLightSensorFB (Odometer odo) {
		super(odo);
		csForward = new FilteredColorSensor(SensorPort.S1,new DifferentialFilter(3));
		csBackward = new FilteredColorSensor(SensorPort.S2,new DifferentialFilter(3));
	}
	
	@Override
	public void run() {
		long correctionStart, correctionEnd;
		FilteredColorSensor lineDetection;
		
		
		/*TODO:find a way to correct the angle:
			when LS1 cross line, start tachometer until LS2 cross the line 
			theta= atan( deltaY/deltaX)*/
	
		while(true){
			correctionStart = System.currentTimeMillis();
		
			//will cycle through each sensor until one on a black line
			do{
				try {Thread.sleep(10);} catch (InterruptedException e) {}
				lineDetection=lineListener();
			}while(lineDetection!=null);
		
			//update position to the position at the middle of the line
			filterAnalysisXY(lineDetection);
		
			//correct the odometer
			correctXY(lineDetection);
				
			correctionEnd = System.currentTimeMillis();
			if (correctionEnd - correctionStart < CORRECTION_PERIOD) {
				try {Thread.sleep(CORRECTION_PERIOD- (correctionEnd - correctionStart));} catch (InterruptedException e) {}
			}
		}
	}
	
	private void correctXY(FilteredColorSensor cs){
		if (cs == csForward){
			snap(CSF_DIST);
		}else{
			snap(CSB_DIST);
		}
	}
	
	private void snap(double csDist){
		double[]csPos = new double[2];
		csPos[0]=pos[0]-csDist*Math.cos(Math.toRadians(pos[2]));
		csPos[1]=pos[1]-csDist*Math.sin(Math.toRadians(pos[2]));
		
		csPos[0]=(csPos[1]%30.48)-15.24;
		csPos[1]=(csPos[0]%30.48)-15.24;
	
		updateOdometer(csPos[0],csPos[1]);
		
	}
	
	private void updateOdometer(double x, double y) {
		odo.getPosition(pos);
		//find which line is the closest ( x or y line) and correct accordingly
		if( Math.abs(y) < Math.abs(x)){
			odo.setY(pos[1]-y);
		}else if ( Math.abs(x) < Math.abs(y)){
			odo.setX(pos[0]-x);
		}
		
	}

	//Line 70: experimental number--| differential filter peak height
	private void filterAnalysisXY(FilteredColorSensor cs){ 
		double[][] filteredPos= new double [2][3];
		double[] val =new double [2];
	
		
		//make sure to be in a peak (differential filter)
		do{
			try {Thread.sleep(10);} catch (InterruptedException e) {}
		}while(isPeak(cs,val));
		
		//positive peak
		if(val[0]>0){
			//wait until we cross the line from above
			differentialCrossing(val, cs,true);
			
			odo.getPosition(filteredPos[0], new boolean [] {true, true, true});
			val[0]=val[1];
			
			//wait until we cross the line from below
			differentialCrossing(val, cs, false);
			
			odo.getPosition(filteredPos[1], new boolean [] {true, true, true});
		}else if(val[0] < 0){
			//wait until we cross the line from below
			differentialCrossing(val,cs, false);
			
			odo.getPosition(filteredPos[0], new boolean [] {true, true, true});
			val[0]=val[1];
			
			//wait until we cross the line from above
			differentialCrossing(val, cs, true);
			
			odo.getPosition(filteredPos[1], new boolean [] {true, true, true});
		}
		
		pos[0]=(filteredPos[0][0]+filteredPos[1][0])/2;
		pos[1]=(filteredPos[0][1]+filteredPos[1][1])/2;
		pos[2]=(filteredPos[0][2]+filteredPos[1][2])/2;
	}
	
	//detects the zero crossing of the differential filter
	private void differentialCrossing(double[] val, FilteredColorSensor cs, boolean above){
			if(above){
				//crossing the line from above
				while (val[0] > 0 && val[1] > 0){
					val[0] = val[1];
					val[1] = cs.getFilteredData();
				}
			}else{
				//crossing the line from below
				while (val[0] < 0 && val[1] < 0){
					val[0] = val[1];
					val[1] = cs.getFilteredData();
				}
			}
		}
		
	private FilteredColorSensor lineListener() {
		double[] frontFilterVal=new double [2];
		double[] backFilterVal=new double [2];
		
		if(isPeak(csForward,frontFilterVal)){
			return csForward;
		} else if(isPeak(csBackward,backFilterVal)){
			return csBackward;
		}
		return null;	
	}
	
	//Line 70: experimental number--| differential filter peak height
	private boolean isPeak(FilteredColorSensor cs, double[] val){
		updatePeakVal(cs,val);
		if(Math.abs(val[0]) <7 && Math.abs(val[1]) <7){
			return true;
		}
		return false;
		
	}
	
	private void updatePeakVal(FilteredColorSensor cs, double[] val){
		val[0] = cs.getFilteredData();
		val[1] = val[0];
	}
}
