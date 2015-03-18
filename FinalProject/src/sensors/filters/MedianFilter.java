/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	MedianFilter.java
 *	Created On:	Feb 20, 2015
 */
package sensors.filters;

import util.MovingWindow;

/**
 *	This filter takes the median inside of a moving window.
 *	Be aware that an even-sized filter takes the average of the two middle values.
 * @author Oleg
 */
public class MedianFilter extends Filter {
	private MovingWindow window;
	
	public MedianFilter(int windowSize) {
		window = new MovingWindow(windowSize);
	}
	
	@Override
	public double filter(double value) {
		window.add(value);
		return window.median();
	}
}
