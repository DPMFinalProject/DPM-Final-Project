/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	FullLocalization.java
 *	Created On:	Apr 2, 2015
 */
package navigation.localization;

import util.Direction;
import navigation.Driver;
import navigation.Navigation;
import navigation.odometry.Odometer;

/**
 * 	Mix between USLocalization and LSLocalizationIntercept. It snaps to every line
 * 	by using LSLocalization, but finds the correct line using USLocalization principles. 
 * @author Oleg
 */
public class FullLocalization extends Localization {
	Localization lsLocalization, usLocalization;
	
	public FullLocalization(Odometer odo, Navigation nav) {
		super(odo, nav);
		lsLocalization = new LSLocalizationIntercept(odo, nav);
		usLocalization = new USLocalizationDiagonal(odo, nav);
	}
	
	/**
	 * Perform a full localization routine using both localization methods.
	 * @param x The x coordinate where the robot will be located after localizing
	 * @param y The y coordinate where the robot will be located after localizing
	 * @param theta The angle at which the robot will be oriented when it will be done
	 */
	@Override
	public void doLocalization(double x, double y, double theta) {
		usLocalization.doLocalization(x, y, theta);
		lsLocalization.doLocalization();
		Driver.turn(Direction.RIGHT, 90);
		lsLocalization.doLocalization();
		Driver.turn(Direction.LEFT, 90);
		
		odo.setX(x);
		odo.setY(y);
		odo.setTheta(theta);
	}
}
