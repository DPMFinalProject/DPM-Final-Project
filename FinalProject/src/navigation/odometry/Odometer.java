/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Odometer.java
 *	Created On:	Feb 26, 2015
 */
package navigation.odometry;

import navigation.Driver;
import static util.Utilities.pause;

/**
 * Keeps track of the position and orientation of the robot.
 * Coordinate system used:
 * 		0 degrees faces towards the positive y-axis.
 * 		Positive angle is clockwise.
 * 		Range: [0, 359] in degrees
 * 
 * @author Gregory Brookes
 */
public class Odometer implements Runnable {
	private double x, y, theta;	
	
	//keeps track of position change
	private int[] delTacho = {0, 0};								
	private int[] tachoTotal = {0, 0};
	private double[] posChange = {0, 0};
	
	private static final long ODOMETER_PERIOD = 15;// odometer update period, in ms	
	
	private final Object lock = new Object();									

	public Odometer() {
		this(0,0,0);
	}
	
	private Odometer(double xPos, double yPos, double theta) {
		x = xPos;
		y = yPos;
		this.theta = theta;
	}


	/**
	 * 	Main odometer loop.
	 */
	@Override
	public void run() {
		long updateStartTime, updateEndTime;
		
		while (true) {
			updateStartTime = System.currentTimeMillis();
			
			Driver.getDelTachoCount(tachoTotal,delTacho);
			
			getTotalTachoCount();
			delPos();
			
			synchronized (lock) {
				odometerUpdate(posChange[0],posChange[1]);
			}
			
			// this ensures that the odometer only runs once every period
			updateEndTime = System.currentTimeMillis();
			if (updateEndTime - updateStartTime < ODOMETER_PERIOD) {
				pause((int)(ODOMETER_PERIOD - (updateEndTime - updateStartTime)));
			}
		}
	}

//--------------------------------------- UTILITY METHODS ---------------------------------------
	
	private void getTotalTachoCount(){
		tachoTotal[0] += delTacho[0];
		tachoTotal[1] += delTacho[1];
	}
	
	private void delPos(){
		//get the change in position
		posChange[0] = Driver.getDelArc(delTacho);
		posChange[1] = Driver.getDelTheta(delTacho);
	}
	
	private void odometerUpdate(double delArc,double delTheta){
		//update position of the center of rotation of the robot
		x += delArc * Math.sin(Math.toRadians(theta + (delTheta/2)));
		y += delArc * Math.cos(Math.toRadians(theta + (delTheta/2)));
		
		//make sure the reported angle goes from 0 to 360 degrees
		double newTheta = (theta + delTheta) % 360;
		theta = (newTheta < 0) ? (newTheta + 360) : newTheta;
	}
	
//--------------------------------------- ACCESSORS & MUTATORS ---------------------------------------
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getTheta() {
		return theta;
	}
	
	public void setX(double x) {
		synchronized (lock) {
			this.x = x;
		}
	}

	public void setY(double y) {
		synchronized (lock) {
			this.y = y;
		}
	}

	public void setTheta(double theta) {
		synchronized (lock) {
			this.theta = theta;
		}
	}
	
	public void getPosition(double [] pos) {
		synchronized (lock) {
			pos[0]=x;
			pos[1]=y;
			pos[2]=theta;
		}
	}
	
	public void getPosition(double [] pos, boolean[] update) {
		synchronized (lock) {
			if (update[0]) pos[0]=x;
			if (update[1]) pos[1]=y;
			if (update[2]) pos[2]=theta;
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
