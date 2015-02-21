/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	MovingAverageFilter.java
 *	Created On:	Feb 20, 2015
 */
package sensors.filters;

import util.MovingWindow;

/**
 * @author Oleg
 *	A Filter that uses a moving average with the aim of smoothing out signal noise
 */
public class AveragingFilter extends Filter {
	private MovingWindow window;
	
	public AveragingFilter(int windowSize) {
		window = new MovingWindow(windowSize);
	}
	
	@Override
	public double filter(double value) {
		window.add(value);
		return window.mean();
	}
}
