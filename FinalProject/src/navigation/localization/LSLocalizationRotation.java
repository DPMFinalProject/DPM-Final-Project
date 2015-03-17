/**
a *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	LSLocalization.java
 *	Created On:	Feb 26, 2015
 */
package navigation.localization;

import lejos.nxt.Sound;
import sensors.managers.GridManager;
import util.Direction;
import util.SensorID;
import navigation.Driver;
import navigation.Navigation;
import navigation.odometry.Odometer;

/**
 * 	Performs localization using the light sensor
 * @author Gregory Brookes
 */

//###################################################################
//#            TODO: make sure the position of the 					#
//#					light sensors doesn't make this code 			#
//#						fall apart									#	
//#				   if it does, decrease all the angles by 45deg?	#
//#				TODO: add a trhead.stop? 							#
//###################################################################
public class LSLocalizationRotation extends Localization {
	private GridManager grid = GridManager.getGridManager();
	
	private final double CS_DIST;
	private double[] pos = new double[3];
	private double[]lineAngle = new double [4];
	
	public LSLocalizationRotation(Odometer odo, Navigation nav) {
		super(odo, nav);
		(new Thread(grid)).start();
		double[] temp = new double[2];
		temp=grid.getSensorCoor(SensorID.RIGHT);
		CS_DIST= ( Math.pow(temp[0], 2)+Math.pow(temp[1], 2) )/2;
	}

	/**
	 * Move to (-2,-2,0) and detect all 4 lines while rotating counterclockwise
	 * Update the odometer (x,y,theta) with the corrected values
	 * @see navigation.localization.Localization#doLocalization()
	 */
	@Override
	public void doLocalization() {		
		//nav.travelTo(-2, -2, 0);
		Driver.turn(Direction.LEFT); // make one full CCW turn 
		
		getlineAngle(lineAngle);
		
		while(Driver.isMoving()){
			try {	Thread.sleep(500);	} catch (InterruptedException e) {}
		}
		updateOdometer(lineAngle,pos);
		
		nav.travelTo(0, 0,0);
		
	}
	
	
	//Utilities methods
	
	//get the angles at wich the color sensor detected a line
	private void getlineAngle(double[] lineAngle){
		
		//set array to test the number of line crossed at the end of the rotation
		for(int i=0; i<lineAngle.length;i++) {
			lineAngle[i] = -1;
		}
		for(int i=0 ; i<4 ; i++){
			System.out.println(i+ "      " +lineAngle[i]);
			while(lineAngle[i]==-1){
				
				while(!grid.lineDetectedRS()){
					try { Thread.sleep(10); } catch (InterruptedException e) {}
				}
				odo.getPosition(pos);
				System.out.println(pos[2]);
				lineAngle[i]=pos[2];
			}
				
			Sound.twoBeeps();
			System.out.println("The angle retrived is:" + lineAngle[i]);
			}
		Driver.stop();
	}
	
/*	//gives the average of the entering/leaving of a black line angles
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
	//Line 70: experimental number--| differential filter peak height
	private double[] filterAnalysis(){ 
		double[] filteredAngles= new double [2];
	//	double[] val =new double [2];
		double val= 0;
		//make sure to be in a peak (differential filter)
		do{
			val[0] = cs.getFilteredData();
			System.out.println(val[0]);
			try {	Thread.sleep(50);	} catch (InterruptedException e) {}
//			if(!driver.isMoving()){
//				return val;
//			}
		}while(Math.abs(val[0])<2 );
		System.out.println("-->found a line");
//			val[1] = val[0];
		
		//positive peak
		if(val[0]>0){
			//wait until we cross the line from above
			System.out.println("\t val positive");
			val=differentialCrossing(val, true);
			System.out.println("\t val negative");
			
			filteredAngles[0]=odo.getTheta();
//			val[0]=val[1];
			
			//wait until we cross the line from below
			val=differentialCrossing(val, false);
			System.out.println("\t val positive");
			
			filteredAngles[1] = odo.getTheta();
			System.out.println("we got a filtered angle");
		}else if(val[0] < 0){
			
			//wait until we cross the line from below
			System.out.println("\t val negative");
			val=differentialCrossing(val,false);
			
			System.out.println("\t val positive");
			filteredAngles[0]=odo.getTheta();
//			val[0]=val[1];
			
			//wait until we cross the line from above
			val=differentialCrossing(val, true);
			System.out.println("\t val negative");
//			
			filteredAngles[1] = odo.getTheta();
			System.out.println("we got a filtered angle");
		}
		
		
		return filteredAngles;
	}
	//detects the zero crossing of the differential filter
	private double differentialCrossing(double[] val, boolean above){
		if(above){
			//crossing the line from above
			while (val[0] > 0 && val[1] > 0){
//				val[0] = val[1];
				val[1 0] = cs.getFilteredData();
				System.out.println("\t " +val);
				try {	Thread.sleep(50);	} catch (InterruptedException e) {}
			}
		}else{
			//crossing the line from below
			while (val[0] < 0 && val[1] < 0){
//				val[0] = val[1];
				val[1 0] = cs.getFilteredData();
				System.out.println("\t " +val);
				try {	Thread.sleep(50);	} catch (InterruptedException e) {}
			}
		}
		return 0;
	}
	
	*/
	private void updateOdometer(double[] lineAng, double[] pos){
		pos[2]=odo.getTheta();	
		//TODO: this math is not good, need to adapt depending on position of sensor/rotation
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
