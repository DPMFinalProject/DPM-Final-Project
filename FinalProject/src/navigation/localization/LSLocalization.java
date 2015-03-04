/**
a *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	LSLocalization.java
 *	Created On:	Feb 26, 2015
 */
package navigation.localization;

import lejos.nxt.ColorSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import sensors.FilteredColorSensor;
import sensors.FilteredSensor;
import sensors.filters.DifferentialFilter;
import util.Direction;
import navigation.Driver;
import navigation.Navigation;
import navigation.odometry.Odometer;

/**
 * 	Performs localization using the light sensor
 * @author Oleg
 */
public class LSLocalization extends Localization {
	private FilteredColorSensor cs;
	
	private final double CS_DIST = 5;
	
	private double[] pos = new double[3];
	private double[]lineAngle = new double [4];
	
	public LSLocalization(Odometer odo, Driver driver, Navigation nav) {
		super(odo, driver, nav);
	}

	/**
	 * Move to (-2,-2,0) and detect all 4 lines while rotating counterclockwise
	 * Update the odometer (x,y,theta) with the corrected values
	 * @see navigation.localization.Localization#doLocalization()
	 */
	@Override
	public void doLocalization() {		
		cs = new FilteredColorSensor(SensorPort.S1,new DifferentialFilter(3));

		//nav.travelTo(-2, -2, 0);
		driver.turn(Direction.LEFT, 360); // make one full CCW turn 
		
		getlineAngle(lineAngle);
		
		updateOdometer(lineAngle,pos);
	}
	
	
	//Utilities methods
	//Line 70: experimental number--| differential filter peak height
	private double[] filterAnalysis(){ 
		double[] filteredAngles= new double [2];
		double[] val =new double [2];
		
		//make sure to be in a peak (differential filter)
		do{
			val[0] = cs.getFilteredData();
			val[1] = val[0];
		}while(Math.abs(val[0]) <7 && Math.abs(val[1]) <7);
		
		//positive peak
		if(val[0]>0){
			//wait until we cross the line from above
			differentialCrossing(val, true);
			
			filteredAngles[0]=odo.getTheta();
			val[0]=val[1];
			
			//wait until we cross the line from below
			differentialCrossing(val, false);
			
			filteredAngles[1] = odo.getTheta();
			Sound.beep();
		}else if(val[0] < 0){
			//wait until we cross the line from below
			differentialCrossing(val,false);
			
			filteredAngles[0]=odo.getTheta();
			val[0]=val[1];
			
			//wait until we cross the line from above
			differentialCrossing(val, true);
			
			filteredAngles[1] = odo.getTheta();
			Sound.beep();
		}
		
		
		return filteredAngles;
	}
	
	//detects the zero crossing of the differential filter
	private void differentialCrossing(double[] val, boolean above){
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
	
	//get the angles at wich the color sensor detected a line
	private void getlineAngle(double[] lineAngle){
		byte i=0;
		//set array to test the number of line crossed at the end of the rotation
		for(double val : lineAngle) {
			val = -1;
		}

		
		while(driver.isMoving()){
			lineAngle[i]=getLineAngleMid(filterAnalysis(), i);
			i++;
			
			//prevent array out of bounds error
			if(i>3){
				lineAngle[3]=-1;
				break;
			}
			try {	Thread.sleep(1000);	} catch (InterruptedException e) {}
			}
			
		//test if all fours lines were detected
		if(lineAngle[3]==-1){
			getlineAngle(lineAngle);
		}
		
	}
	
	//gives the average of the entering/leaving of a black line angles
	private double getLineAngleAvg( double[] filteredAngles){
		return (filteredAngles[0] + filteredAngles[1])/2;
	}
	
	//give the middle point between entering/leaving of a black line angles
	private double getLineAngleMid(double[] filteredAngles, byte lineNumber){
		//vertical line, assuming counterclockwise , facing north
		if(lineNumber == 0 || lineNumber == 2){
			return Math.acos( ( Math.cos(filteredAngles[0])+Math.cos(filteredAngles[1]) ) /2);
		//horizontal line
		}else {
			return Math.acos( ( Math.sin(filteredAngles[0])+Math.sin(filteredAngles[1]) ) /2);
		}
	}

	
	private void updateOdometer(double[] lineAng, double[] pos){
		pos[2]=odo.getTheta();	
		odo.setPosition(new double [] {correctX(lineAng), correctY(lineAng),
				delTheta(lineAng)+pos[2]}, new boolean [] {true, true, true});
	}
	private double delTheta(double[] lineAng){
		return 270-((lineAng[0]+lineAng[2])/2);
	}
	private double correctX(double[] lineAng){
		return -CS_DIST*Math.cos(Math.toRadians( (lineAng[0]-lineAng[2])/2 ) );
	}
	private double correctY(double[] lineAng){
		return CS_DIST*Math.cos(Math.toRadians( ( lineAng[1]+360-lineAng[3])/2) );
	}
}
