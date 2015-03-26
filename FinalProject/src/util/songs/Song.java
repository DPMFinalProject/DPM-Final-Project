/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Songs.java
 *	Created On:	Mar 20, 2015
 */
package util.songs;

import lejos.nxt.Sound;

/**
 * 	for funsies
 * 
 * @author Auguste
 */
public abstract class Song {
	
	public void play() {
		//note is of form {frequency, duration, pause}
		for (int[] note : getSheetMusic()) {
			Sound.playTone(note[0], note[1], 200);
			Sound.pause(note[2]);
		}
	}
	
	protected abstract int[][] getSheetMusic();
	
	protected int[][] toSheetMusic(String[] tones) {
		int i=0;
		int[][] result = new int[tones.length][3];
		
		for (String tone : tones) {
			result[i][0] = toneToFrequency(tone);
			result[i][1] = 200;
			result[i][2] = 200;
			
			i++;
		}
		
		return result;
	}
	
	protected int[][] toSheetMusic(String[] tones, int[][] timing) {
		
		int[][] result = new int[tones.length][3];
		
		for (int i=0; i < tones.length; i++) {
			result[i][0] = toneToFrequency(tones[i]);
			result[i][1] = timing[i][0];
			result[i][2] = timing[i][1];
		}
		
		return result;
	}
	
	protected int toneToFrequency(String tone) {
		switch((int)tone.charAt(0)) {
		case (int)'A':
			if(tone.length() == 3) {
				return Asharp[Integer.parseInt(tone.substring(2,3))];
			}
			else {
				return A[Integer.parseInt(tone.substring(1,2))];
			}
		case (int)'B':
			return B[Integer.parseInt(tone.substring(1,2))];
		case (int)'C':
			if(tone.length() == 3) {
				return Csharp[Integer.parseInt(tone.substring(2,3))];
			}
			else {
				return C[Integer.parseInt(tone.substring(1,2))];
			}	
		case (int)'D':
			if(tone.length() == 3) {
				return Dsharp[Integer.parseInt(tone.substring(2,3))];
			}
			else {
				return D[Integer.parseInt(tone.substring(1,2))];
			}
		case (int)'E':
			return E[Integer.parseInt(tone.substring(1,2))];
		case (int)'F':
			if(tone.length() == 3) {
				return Fsharp[Integer.parseInt(tone.substring(2,3))];
			}
			else {
				return F[Integer.parseInt(tone.substring(1,2))];
			}
		case (int)'G':
			if(tone.length() == 3) {
				return Gsharp[Integer.parseInt(tone.substring(2,3))];
			}
			else {
				return G[Integer.parseInt(tone.substring(1,2))];
			}
		default:
			return 0;
		}
	}
	
	//tones
	static int[] A = {27, 55, 110, 220, 440, 880, 1760, 3520, 704, 14080};
	static int[] Asharp = {29, 58, 116, 233, 466, 932, 1964, 3729, 7458, 14917};
	static int[] B = {31, 62, 123, 247, 494, 988, 1976, 3951, 7902, 15804};
	static int[] C = {16, 33, 65, 131, 262, 523, 1047, 2093, 4186, 8372, 16744};
	static int[] Csharp = {17, 35, 69, 139, 277, 554, 1109, 2217, 4435, 8870};
	static int[] D = {18, 37, 73, 147, 294, 587, 1174, 2344, 4699, 9397};
	static int[] Dsharp = {19, 39, 78, 156, 311, 622, 1245, 2489, 4978, 9956};
	static int[] E = {21, 41, 82, 165, 330, 659, 1319, 2637, 5274, 10548};
	static int[] F = {22, 44, 87, 175, 349, 698, 1397, 2794, 5588, 11175};
	static int[] Fsharp = {23, 46, 92, 185, 370, 740, 1480, 2960, 5920, 11840};
	static int[] G = {24, 49, 98, 196, 392, 784, 1568, 3136, 6271, 12542};
	static int[] Gsharp = {26, 52, 104, 208, 415, 831, 1661, 3322, 6645, 13290};
}