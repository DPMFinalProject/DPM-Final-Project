/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	MotorPrecision.java
 *	Created On:	Feb 20, 2015
 */
package tests.sensors;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import tests.TestCase;

/**
 * @author Oleg
 *	Example Test: Retrieves ultrasonic sensor values with all 255 values removed.
 * 
 */
public class MotorPrecision extends TestCase {

	@Override
	public void runTest() {
		int buttonChoice = 0;
		int turns = 0;
		int rotations=0;
		NXTRegulatedMotor motorA =Motor.A;
		

		do {
			// clear the display
			LCD.clear();

			// Shows the options for the number of ping ponb balls to shoow
			LCD.drawString("Number of ping pong", 0, 0);
			LCD.drawString("  Balls to shoot  ", 0, 1);
			LCD.drawInt( turns, 5, 2);
			LCD.drawString("  <  -- | ++   >   ", 0, 3);
	
			
		buttonChoice = Button.waitForAnyPress();
		
		//increment/decrement the number of ping pong balls depending on thebutton input
		if(buttonChoice==Button.ID_LEFT) {
					turns-=5;
		}
		else if(buttonChoice==Button.ID_RIGHT) {
					turns+=5;
		}
		
	
		try {	Thread.sleep(100);	} catch (InterruptedException e) {}

		
		} while (buttonChoice != Button.ID_ENTER);
		
		
		motorA.rotate(360*turns);
			
		while(motorA.isMoving()) {
			rotations=motorA.getTachoCount()%360;
			LCD.drawInt( rotations, 5, 2);
		}	
			
		System.exit(0);
	
	}

}
