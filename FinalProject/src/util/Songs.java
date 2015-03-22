/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Songs.java
 *	Created On:	Mar 20, 2015
 */
package util;

import lejos.nxt.Sound;

/**
 * 	If we're not having fun, then what's the point of doing this
 * 
 * @author Auguste
 */
public class Songs {
	
	protected static int[][] sheetMusic;
	
	public static void play() {
		//note is of form {frequency, duration, pause}
		for (int[] note : sheetMusic) {
			Sound.playTone(note[0], note[1]);
			Sound.pause(note[2]);
		}
	}
	
	private static int[][] toSheetMusic(String[] tones) {
		int i=0;
		int[][] result = new int[tones.length][3];
		
		for (String tone : tones) {
			result[i][0] = toneToFrequency(tone);
			result[i][1] = 100;
			result[i][2] = 100;
			
			i++;
		}
		
		return result;
	}
	
	public final static class superMario extends Songs {

		static int[][] sheetMusic = {
				{660, 100, 150}, {660, 100, 300}, {660, 100, 300}, {510, 100, 100}, {660, 100, 300}, {770, 100, 550}, 
				{380, 100, 575}, {510, 100, 450}, {380, 100, 400}, {320, 100, 500}, {440, 100, 300}, {480, 80, 330}, 
				{450, 100, 150}, {430, 100, 300}, {380, 100, 200}, {660, 80, 200}, {760, 50, 150}, {860, 100, 300}, 
				{700, 80, 150}, {760, 50, 350}, {660, 80, 300}, {520, 80, 150}, {580, 80, 150}, {480, 80, 500}, 
				{510, 100, 450}, {380, 100, 400}, {320, 100, 500}, {440, 100, 300}, {480, 80, 330}, {450, 100, 150}, 
				{430, 100, 300}, {380, 100, 200}, {660, 80, 200}, {760, 50, 150}, {860, 100, 300}, {700, 80, 150}, 
				{760, 50, 350}, {660, 80, 300}, {520, 80, 150}, {580, 80, 150}, {480, 80, 500}, {500, 100, 300}, 
				{760, 100, 100}, {720, 100, 150}, {680, 100, 150}, {620, 150, 300}, {650, 150, 300}, {380, 100, 150}, 
				{430, 100, 150}, {500, 100, 300}, {430, 100, 150}, {500, 100, 100}, {570, 100, 220}, {500, 100, 300}, 
				{760, 100, 100}, {720, 100, 150}, {680, 100, 150}, {620, 150, 300}, {650, 200, 300}, {102, 80, 300}, 
				{102, 80, 150}, {102, 80, 300}, {380, 100, 300}, {500, 100, 300}, {760, 100, 100}, {720, 100, 150}, 
				{680, 100, 150}, {620, 150, 300}, {650, 150, 300}, {380, 100, 150}, {430, 100, 150}, {500, 100, 300}, 
				{430, 100, 150}, {500, 100, 100}, {570, 100, 420}, {585, 100, 450}, {550, 100, 420}, {500, 100, 360}, 
				{380, 100, 300}, {500, 100, 300}, {500, 100, 150}, {500, 100, 300}, {500, 100, 300}, {760, 100, 100}, 
				{720, 100, 150}, {680, 100, 150}, {620, 150, 300}, {650, 150, 300}, {380, 100, 150}, {430, 100, 150}, 
				{500, 100, 300}, {430, 100, 150}, {500, 100, 100}, {570, 100, 220}, {500, 100, 300}, {760, 100, 100}, 
				{720, 100, 150}, {680, 100, 150}, {620, 150, 300}, {650, 200, 300}, {102, 80, 300}, {102, 80, 150}, 
				{102, 80, 300}, {380, 100, 300}, {500, 100, 300}, {760, 100, 100}, {720, 100, 150}, {680, 100, 150}, 
				{620, 150, 300}, {650, 150, 300}, {380, 100, 150}, {430, 100, 150}, {500, 100, 300}, {430, 100, 150}, 
				{500, 100, 100}, {570, 100, 420}, {585, 100, 450}, {550, 100, 420}, {500, 100, 360}, {380, 100, 300}, 
				{500, 100, 300}, {500, 100, 150}, {500, 100, 300}, {500, 60, 150}, {500, 80, 300}, {500, 60, 350}, 
				{500, 80, 150}, {580, 80, 350}, {660, 80, 150}, {500, 80, 300}, {430, 80, 150}, {380, 80, 600}, 
				{500, 60, 150}, {500, 80, 300}, {500, 60, 350}, {500, 80, 150}, {580, 80, 150}, {660, 80, 550}, 
				{870, 80, 325}, {760, 80, 600}, {500, 60, 150}, {500, 80, 300}, {500, 60, 350}, {500, 80, 150}, 
				{580, 80, 350}, {660, 80, 150}, {500, 80, 300}, {430, 80, 150}, {380, 80, 600}, {660, 100, 150}, 
				{660, 100, 300}, {660, 100, 300}, {510, 100, 100}, {660, 100, 300}, {770, 100, 550}, {380, 100, 575},		
		};
	}
	
	public final static class tetris extends Songs {
		
		static String[] tones = {"E6", "B5", "C6", "D6", "C6", "B5", "A5", "A5", "C6", "E6", "D6", "C6", "B5", "C6", "D6", 
			"E6", "C6", "A5", "A5", "D6", "F6", "A6", "G6", "F6", "E6", "C6", "E6", "D6", "C6", "B5", "B5", "C6", "D6", "E6", "C6", "A5", "A5"};
		
		static int[][] sheetMusic = toSheetMusic(tones);
	}
	
	private static int toneToFrequency(String tone) {
		switch(tone.substring(0, 1)){
		case "A":
			if(tone.substring(1,2) == "#") {
				return Asharp[Integer.parseInt(tone.substring(2,3))];
			}
			else {
				return A[Integer.parseInt(tone.substring(1,2))];
			}
		case "B":
			return B[Integer.parseInt(tone.substring(1,2))];
		case "C":
			if(tone.substring(1,2) == "#") {
				return Csharp[Integer.parseInt(tone.substring(2,3))];
			}
			else {
				return C[Integer.parseInt(tone.substring(1,2))];
			}	
		case "D":
			if(tone.substring(1,2) == "#") {
				return Dsharp[Integer.parseInt(tone.substring(2,3))];
			}
			else {
				return D[Integer.parseInt(tone.substring(1,2))];
			}
		case "E":
			return E[Integer.parseInt(tone.substring(1,2))];
		case "F":
			if(tone.substring(1,2) == "#") {
				return Fsharp[Integer.parseInt(tone.substring(2,3))];
			}
			else {
				return F[Integer.parseInt(tone.substring(1,2))];
			}
		case "G":
			if(tone.substring(1,2) == "#") {
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