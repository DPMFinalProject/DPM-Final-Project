/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	BasicAvoider.java
 *	Created On:	Mar 19, 2015
 */

package navigation.avoidance;

import navigation.Driver;
import navigation.odometry.Odometer;
import sensors.managers.ObstacleDetection;
import util.Direction;
import static util.Utilities.isNear;

public class BasicAvoider extends ObstacleAvoidance{
	private int MIN_DISTANCE_FROM_WALL = 18;
	private double initialOrientation;
	ObstacleDetection detector;
	
	public BasicAvoider(Direction wallDirection, Odometer odo){
		super(wallDirection,odo);
		detector = ObstacleDetection.getObstacleDetection();
		BAND_CENTER = 18;
		BAND_WIDTH = 8;
	}
	
	@Override
	public void avoid(){
		
		Driver.stop();

		if(!hasAvoided()){
			if(wallDirection == Direction.FWD){
				if(isNear(Direction.LEFT) && isNear(Direction.RIGHT)){
					//turn 180
					//wall = right
					//follow wall
					Driver.turn(Direction.opposite(wallDirection), 90);
					setWallDirection(Direction.LEFT);
					avoider();
				}
				else if(isNear(Direction.LEFT)){
					//drift right
				}
				else if(isNear(Direction.RIGHT)){
					//drift left
				}
			}
			else if(wallDirection == Direction.LEFT){
				//drift right
			}
			else if(wallDirection == Direction.RIGHT){
				//drift left
			}
		}
		
		Driver.stop();
	}
	
	private void avoider(){
		double error = BAND_CENTER - detector.wallDistance(wallDirection);
		
		if (Math.abs(error)<BAND_WIDTH)	{
			Driver.move(Direction.FWD);
		}
		else if (error > 0) {
			Driver.drift(wallDirection);
		}
		else {
			Driver.drift(Direction.opposite(wallDirection));
		}
	}
	
	private boolean hasAvoided(){
		
		return (isNear(Direction.LEFT) || isNear(Direction.RIGHT)) ? false : true;
	}
	
	private boolean isNear(Direction direction){
		
		if(detector.wallDistance(direction) < MIN_DISTANCE_FROM_WALL){
			return true;
		}
		else{return false;}
	}
}