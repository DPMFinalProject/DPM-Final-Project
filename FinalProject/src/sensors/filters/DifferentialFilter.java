/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	DifferentialFilter.java
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
	
	public DifferentialFilter(int windowSize) {
		window = new MovingWindow(windowSize);
	}
	
	@Override
	public double filter(double value) {
		window.add(value);
		return window.discreteDerivative();
	}
}
