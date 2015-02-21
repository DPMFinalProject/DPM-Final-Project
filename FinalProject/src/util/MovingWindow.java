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
 * @author Oleg
 *	Represents a moving window of doubles with methods allowing statistical
 *	calculations
 */
public class MovingWindow {
	LinkedList<Double> window = new LinkedList<Double>();
	int maxWindowSize;
	
	public MovingWindow(int size) {
		maxWindowSize = size;
	}
	
	public void add(double entry) {
		if (window.size() == maxWindowSize) {
			window.remove(0);
			window.add(entry);
		} else {
			window.add(entry);
		}
	}
	
	public double median() {
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
	
	public double mean() {
		double result = 0;
		
		for (Double d : window) {
			result += d;
		}
		
		return result/window.size();
	}
	
	public double stdDev() {
		double mean = mean();
		
		double variance = 0;
		for (Double d : window) {
			variance += (d - mean) * (d - mean);
		}
		
		return Math.sqrt(variance / (window.size() - 1));
	}
	
	public boolean isSizeEven() {
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
