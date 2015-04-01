/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Filter.java
 *	Created On:	Feb 25, 2015
 */
package sensors.filters;

/**
 *  Provides a basic definition of a filter.
 * @author Oleg
 */
public abstract class Filter {
	
	/**
	 * Abstract definition of a filter method that allows the application of
	 * mathematical operations on a signal received in real time.
	 * @param value
	 * @return
	 */
	public abstract double filter (double value);
}
