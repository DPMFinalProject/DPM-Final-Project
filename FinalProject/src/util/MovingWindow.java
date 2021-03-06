/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	MovingWindow.java
 *	Created On:	Feb 20, 2015
 */
package util;

import java.util.Arrays;
import java.util.LinkedList;

/**
 *  Represents a moving window of doubles with methods allowing statistical
 *	calculations
 * @author Oleg
 */
public class MovingWindow {
	private LinkedList<Double> window = new LinkedList<Double>();
	private int maxWindowSize;
	
	public MovingWindow(int size) {
		maxWindowSize = size;
	}
	
	/**
	 * 	Adds a new entry to the end of the list, and removes the oldest element
	 * 	if the window was already full.
	 * @param entry
	 */
	public void add(double entry) {
		if (window.size() >= maxWindowSize) {
			window.remove(0);
			window.add(entry);
		} else {
			window.add(entry);
		}
	}
	
	/**
	 * Finds the median value in the moving window.
	 * @return Returns the median, or 0 if the window is not full yet.
	 */
	public double median() {
		// Returns 0 if the moving window is not full yet.
		if (window.size() == 1) {
			return window.get(0);
		}
		Double[] windowArray = (Double[]) window.toArray(new Double[window.size()]);
		
		Arrays.sort(windowArray);
		
		int middle = (window.size() / 2) - 1;
		
		/*
		 *  If the moving window has an even number of entries, 
		 *  return the mean of the two middle entries
		 */
		if (isSizeEven()) {
			return (windowArray[middle] + windowArray[middle + 1]) / 2.0;
		} else {
			return windowArray[middle];
		}
	}
	
	/**
	 * 	Finds the mean of the moving window.
	 * @return	Returns the mean.
	 */
	public double mean() {
		double result = 0;
		
		for (Double d : window) {
			result += d;
		}
		
		return result/window.size();
	}
	
	/**
	 * 	Calculates a weighted average based on a given array of coefficients
	 * @param 	weighting coefficients
	 * @return	Returns the weightedAverage.
	 */
	public double weightedAverage(double[] weightingCoefs) {
		double result = 0;
		
		for (int i=0; i<window.size();i++) {
			result += (window.get(i)*weightingCoefs[i]);
		}
		
		return result;
	}
	
	/**
	 * 	Computes the standard deviation inside of the moving window.
	 * @return The standard deviation.
	 */
	public double stdDev() {
		double mean = mean();
		
		double variance = 0;
		for (Double d : window) {
			variance += (d - mean) * (d - mean);
		}
		
		return Math.sqrt(variance / (window.size() - 1));
	}
	
	/**
	 * 	Computes the discrete derivative of the last two entries.
	 * @return The discrete derivative.
	 */
	public double discreteDerivative() {
		if(window.size()< maxWindowSize)
		{
			return 0.0;
		}
		return window.get(window.size()-1) - window.get(window.size()-2);
	}
	
	/**
	 * Checks to see if the window is filled with a specific value.
	 * @param constant	The value that is being verified (typically an outlier)
	 * @return	Returns true if all of the elements in the list are the provided constant 
	 */
	public boolean isConstant(double constant) {
		for (double value : window) {
			if (value != constant) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isSizeEven() {
		return (maxWindowSize % 2) == 0;
	}
	
	/**
	 * 
	 * @return true if the Moving Window has no missing elements
	 */
	public boolean isFull() {
		return window.size() == maxWindowSize;
	}
	
	public String toString() {
		return Arrays.toString(window.toArray());
	}
}
