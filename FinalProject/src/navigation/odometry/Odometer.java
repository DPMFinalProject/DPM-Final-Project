/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Odometer.java
 *	Created On:	Feb 26, 2015
 */
package navigation.odometry;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import navigation.Driver;
import navigation.odometry.correction.OdometryCorrection;

/**
 * Keeps track of the position and orientation of the robot.
 * @author Gregory Brookes
 */
public class Odometer implements Runnable {
	private double x, y, theta;	
	private final Driver driver;
	private OdometryCorrection correction;
	
	//keeps track of position change
	int[] delTacho=new int[2];								
	int[] tachoTotal=new int[2];
	double[] posChange=new double[2];
	
	private static final long ODOMETER_PERIOD = 15;			// odometer update period, in ms			
	private Object lock;									

	public Odometer(Driver driver,OdometryCorrection odometerCorrector ) {
		this(0.0,0.0,0.0,driver, odometerCorrector);
		lock = new Object();
	}
	
	public Odometer(double xpos, double ypos, double Theta, Driver driver, 
			OdometryCorrection odometerCorrector) {
		
		x=xpos;
		y=ypos;
		theta=Theta;
		this.driver=driver;
		correction = odometerCorrector;
		lock = new Object();
				
	}


	/**
	 * 	Main odometer loop.
	 */
	@Override
	public void run() {
		long updateStart,updateEnd;
		
		while (true) {
			updateStart = System.currentTimeMillis();
			
			synchronized (lock){
				driver.getTotalTachoCount(tachoTotal);
			}
			
			delPos(posChange,tachoTotal);
			
			synchronized (lock) {
				odometerUpdate(posChange[0],posChange[1]);
			}
			
			// this ensures that the odometer only runs once every period
			updateEnd = System.currentTimeMillis();
			if (updateEnd - updateStart < ODOMETER_PERIOD) {
				try {
					Thread.sleep(ODOMETER_PERIOD - (updateEnd - updateStart));
				} catch (InterruptedException e) {
				}
			}
			throw new UnsupportedOperationException();
		}
	}

	//Utilities methods
	
	public void delTachoCount(int[] delTacho, int[]tachoTotal){
		delTacho[0]= tachoTotal[0]-delTacho[0];
		delTacho[1]= tachoTotal[1]-delTacho[1];
	}
	public void delPos(double[] posChange, int[]tachoTotal){
		//get the change in position
		delTachoCount(delTacho,tachoTotal);
		posChange[0]= driver.delArc(delTacho);
		posChange[1]=driver.delTheta(delTacho);
	}
	public void odometerUpdate(double delArc,double delTheta){
		//update position of the center of rotation of the robot
		x+=delArc*(Math.sin(Math.toRadians(theta+delTheta/2)));
		y+=delArc*(Math.cos(Math.toRadians(theta+delTheta/2)));
		
		//make sure the reported angle goes from 0 to 360 degrees
		if((theta+delTheta)%360 < 0)
			theta = (theta+delTheta)%360+360;
		else
			theta=(theta+delTheta)%360;
	}
	

	// Getters and Setters
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}

	public double getTheta() {
		return theta;
	}
	public void setTheta(double theta) {
		this.theta = theta;
	}
	
	public void getPosition(double [] pos) {
		synchronized (lock) {
			pos[0] = x;
			pos[1] = y;
			pos[2] = theta;
		}
	}
	public void setPosition(double [] pos, boolean [] update) {
		synchronized (lock) {
			if (update[0]) x = pos[0];
			if (update[1]) y = pos[1];
			if (update[2]) theta = pos[2];
		}
	}
}
