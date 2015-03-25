/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Victory.java
 *	Created On:	Mar 20, 2015
 */
package util.songs;

/**
 * victory
 * 
 * @author Auguste, Ray
 */
public class Victory extends Song {
	
	private String[] tones = {"D5", "D5", "D5", "D5", "A#5", "C5", "D5", "C5", "D5"};
	
	//{duration, pause}
	private int[][] timing = {{148, 150}, {148, 150}, {148, 150}, {148, 400}, {444, 400}, 
			{444, 400}, {148, 300}, {148, 150}, {889, 400}};
	
	@Override
	protected int[][] getSheetMusic() {
		return toSheetMusic(tones, timing);
	}
}