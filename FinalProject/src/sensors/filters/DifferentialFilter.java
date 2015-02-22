/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	MovingAverageFilter.java
 *	Created On:	Feb 22, 2015
 */
package sensors.filters;

import util.MovingWindow;

/**
 * @author Auguste
 *	A Filter that uses the discrete derivative to detect sudden changes in sensor input
 */
public class DifferentialFilter extends Filter {
	private MovingWindow window;
	
	public DifferentialFilter() {
		window = new MovingWindow(2);
	}
	
	@Override
	public double filter(double value) {
		window.add(value);
		return window.discreteDerivative();
	}
}
