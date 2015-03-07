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
	private final Driver driver;
	
	//keeps track of position change
	int[] delTacho= {0, 0};								
	int[] tachoTotal= {0, 0};
	double[] posChange= {0, 0};
	
	private static final long ODOMETER_PERIOD = 15;// odometer update period, in ms			
	private Object lock;									

	public Odometer(Driver driver) {
		this(0.0, 0.0, 0.0, driver);
	}
	
	private Odometer(double xpos, double ypos, double Theta, Driver driver) {
		
		x = xpos;
		y = ypos;
		theta = Theta;
		this.driver = driver;
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
			
			//System.out.println("" + x + "," + y + "," + theta);
			
			
			driver.getDelTachoCount(tachoTotal,delTacho);
			
			getTotalTachoCount();
			delPos();
			
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
		}
	}

	//Utilities methods
	
	private void getTotalTachoCount(){
		tachoTotal[0]+=delTacho[0];
		tachoTotal[1]+=delTacho[1];
	}
	
	private void delPos(){
		//get the change in position
		posChange[0] = driver.getDelArc(delTacho);
		posChange[1] = driver.getDelTheta(delTacho);
	}
	
	private void odometerUpdate(double delArc,double delTheta){
		//update position of the center of rotation of the robot
		x += delArc * (Math.sin(Math.toRadians(theta+delTheta/2)));
		y += delArc * (Math.cos(Math.toRadians(theta+delTheta/2)));
		
		//make sure the reported angle goes from 0 to 360 degrees
		if((theta+delTheta) % 360 < 0)
			theta = ((theta+delTheta) % 360) + 360;
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
