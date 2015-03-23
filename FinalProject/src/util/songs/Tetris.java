/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Tetris.java
 *	Created On:	Mar 20, 2015
 */
package util.songs;

/**
 * because tetris
 * 
 * @author Auguste
 */
public class Tetris extends Song {
	
	private String[] tones = {"E6", "B5", "C6", "D6", "C6", "B5", "A5", "A5", "C6", "E6", "D6", "C6", "B5", "C6", "D6", 
		"E6", "C6", "A5", "A5", "D6", "F6", "A6", "G6", "F6", "E6", "C6", "E6", "D6", "C6", "B5", "B5", "C6", "D6", "E6", "C6", "A5", "A5"};
	
	@Override
	protected int[][] getSheetMusic() {
		return toSheetMusic(tones);
	}
}