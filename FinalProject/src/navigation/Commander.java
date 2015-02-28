/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Commander.java
 *	Created On:	Feb 26, 2015
 */
package navigation;

import util.ResourceManager;

/**
 *  This class is in charge of decision making and goal evaluation.
 * 	@author Oleg
 */
public class Commander {
	private Navigation nav;
	
	// This is the main method you will be using.
	private static void execute() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ResourceManager.init();
			
			(new Thread() {
				public void run() {
					execute();
				}
			}).start();
			
		} finally {
			ResourceManager.init();
		}
	}

}
