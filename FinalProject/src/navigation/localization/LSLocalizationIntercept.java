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
import util.GridManager;
import navigation.Driver;
import navigation.Navigation;
import navigation.odometry.Odometer;

/**
 * 	Performs localization using the light sensor
 * @author Gregory Brookes
 */
//###################################################################
//#            TODO: Figure out how to make the robot be 			#
//#					  perpendicular to a line.						#
//#			   TODO: update the GrigManager after August changes	#
//###################################################################


public class LSLocalizationIntercept extends Localization {
	private FilteredColorSensor csR;
	private FilteredColorSensor csL;
	private GridManager gridR;
	private GridManager gridL;
	private boolean ronLine;
	private boolean lonLine;
	private FilteredColorSensor triggered ;
	private FilteredColorSensor temp ;
	
	private double[] pos = new double[3];
	
	public LSLocalizationIntercept(Odometer odo, Driver driver, Navigation nav) {
		super(odo, driver, nav);
	}

	/**
	 * Find out the position by intercepting a line and rotating 
	 * until both sensor and ON the line. 
	 * @see navigation.localization.Localization#doLocalization()
	 */
	@Override
	public void doLocalization() {		
		csR = new FilteredColorSensor(SensorPort.S1,new DifferentialFilter(3));
		csL = new FilteredColorSensor(SensorPort.S2,new DifferentialFilter(3));
		gridR = new GridManager(csR,0,odo);
		gridL = new GridManager(csL,0,odo);

//		System.out.println("initialisation working");
		
		//probelem: if in tile 1, more towardscloseWall
		if(isInTile3()){
			moveTowardsClosestWall();
		}else{
			moveAwayClosestWall();
		}
		
		parallelToLine();		
		
		
	}
	//#######################################3
	//Note: the wheels are wired on the robot used so it turns the wrong way
	
	
	//Utilities methods
	private boolean isInTile3 (){
		odo.getPosition(pos);
		if(pos[0]>30.48 && pos[1]>30.48){
			return true;
		}
		return false;
	}
	private void moveTowardsClosestWall(){
		if(pos[0]<pos[1]){
			nav.turnTo(270);
		}else{
			nav.turnTo(180);
		}
//		System.out.println("Forward");
		driver.move(Direction.FWD);
	}
	private void moveAwayClosestWall(){
		if(pos[0]<pos[1]){
//			System.out.println("optimal rotation 0");
			optimalRotation(0);
		}else{
//			System.out.println("optimal rotation 1");
			optimalRotation(1);
		}
		while(driver.isMoving()){
			try {	Thread.sleep(100);	} catch (InterruptedException e) {}
		}
		System.out.println("Forward");
		driver.move(Direction.FWD);
	}
	private void optimalRotation(int i){
		if (i == 0){
			if(pos[i]<30.48){
//				System.out.println("turn to 0");
				nav.turnTo(0);
			}else{
//				System.out.println("turn to 180");
				nav.turnTo(180);
			}
		}else if (i == 1){
			if(pos[i]<30.48){
//				System.out.println("turn to 90");
				nav.turnTo(90);
			}else{
//				System.out.println("turn to 270");
				nav.turnTo(270);
			}
		}else{
			System.out.println("Invatind input to optimalRotation method in LSLocalisationIntercept");
		}
	}
	
	
	private void parallelToLine(){
		
		waitforSensorEnteringLine();
		
		driver.turn(rotationDirection());
		
		/*while(sensorOffLine()==triggered){
			temp=sensorOnLine();
			if(temp!=triggered && temp!=null){
				driver.stop();
				return;
			}
		}
		driver.stop();
		try {	Thread.sleep(500);	} catch (InterruptedException e) {}
		driver.move(1,true);
		parallelToLine();*/
	}
	
	private void waitforSensorEnteringLine(){
		System.out.println("wait for Sensor entering Line");
		do{
			triggered=sensorEnteringLine();
		}while(triggered==null);
	}

	//returns the sensor who JUST entered a line
	private FilteredColorSensor sensorEnteringLine(){
		if(gridR.hasEnterLine()){
			driver.stop();
			ronLine=true;
						System.out.println("RightSensor entering Line");
						try {	Thread.sleep(500);	} catch (InterruptedException e) {}
			return csR;
		}else if(gridL.hasEnterLine()){
			lonLine=true;
			driver.stop();
						System.out.println("LestSensor entering Line");
						try {	Thread.sleep(500);	} catch (InterruptedException e) {}
			return csL;
		}
		System.out.print("\t null");
		
		return null;
	}
	//returns the sensor who JUST exited a line
	private FilteredColorSensor sensorOffLine(){
		if(gridR.hasExitLine()){
			System.out.println("RightSensor Off Line");
			driver.stop();
			try {	Thread.sleep(500);	} catch (InterruptedException e) {}
			return csR;
		}else if(gridL.hasExitLine()){
			System.out.println("LestSensor On Line");
			try {	Thread.sleep(500);	} catch (InterruptedException e) {}
			driver.stop();
			return csL;
		}
		
		return null;
	}

	private Direction rotationDirection(){
		if(triggered==csR){
			System.out.println("TURNING LEFT");
			return Direction.LEFT;
		}
		System.out.println("TURNING RGHT");
		return Direction.RIGHT;
		
	}

}




	


