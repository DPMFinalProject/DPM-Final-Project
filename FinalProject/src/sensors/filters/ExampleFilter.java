/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	ExampleFilter.java
 *	Created On:	Feb 25, 2015
 */
package sensors.filters;

public class ExampleFilter extends Filter {
	double lastValue = 0;
	@Override
	
	// Removes all 255 values.
	public double filter(double value) {
		if (value != 255) {
			lastValue = value;
		}
		
		return lastValue;
	}
}
