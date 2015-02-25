/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	OutlierFilter.java
 *	Created On:	Feb 21, 2015
 */
package sensors.filters;

import util.MovingWindow;

/**
 * @author Oleg
 *	Formalization of filter provided in lab 1 that removes non consecutive false positives.
 */
public class OutlierFilter extends Filter {
	private MovingWindow window;
	private int outlierValue;
	private double lastValue;
	
	public OutlierFilter(int threshold, int outlierValue) {
		window = new MovingWindow(threshold);
		this.outlierValue = outlierValue;
	}
	
	@Override
	public double filter(double value) {
		if (value != outlierValue) { // If the value is normal
			window.add(value);
			lastValue = value;
			return value;
		} else {	// if the sensor provides an outlier
			if (window.isConstant(outlierValue)) {	// if the value was a true max
				lastValue = value;
				return outlierValue;
			} else {	// filter out the outlier if it was not a true max
				window.add(value);
				return lastValue;
			}
		}
	}
}
