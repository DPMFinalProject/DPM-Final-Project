/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	Art.java
 *	Created On:	Apr 7, 2015
 */
package util;

import lejos.nxt.LCD;

/**
 * 	so artsy
 * 
 * @author Auguste
 */
public class Art {
	public static void drawInvader() {
		LCD.clear();
		int artHeight = 10;
		int artWidth = 13;
		
		int[][] art = {		
		{3, 1}, {9, 1}, 
		{4, 2}, {8, 2}, 
		{3, 3}, {4, 3}, {5, 3}, {6, 3}, {7, 3}, {8, 3}, {9, 3},
		{2, 4}, {3, 4}, {5, 4}, {6, 4}, {7, 4}, {9, 4}, {10, 4},
		{1, 5}, {2, 5}, {3, 5}, {4, 5}, {5, 5}, {6, 5}, {7, 5}, {8, 5}, {9, 5}, {10, 5}, {11, 5},
		{1, 6}, {3, 6}, {4, 6}, {5, 6}, {6, 6}, {7, 6}, {8, 6}, {9, 6}, {11, 6},
		{1, 7}, {3, 7}, {9, 7}, {11, 7},
		{4, 8}, {5, 8}, {7, 8}, {8, 8}
		};
		
		for(int[] pixel : art) {
			drawPixel(pixel[0], pixel[1], artHeight, artWidth);
		}
	}

	private static void drawPixel(int x, int y, int height, int width) {
		int pixelWidth = LCD.SCREEN_WIDTH/width;
		int pixelHeight = LCD.SCREEN_HEIGHT/height;
		int pixelWidthOffset = Math.round((LCD.SCREEN_WIDTH - pixelWidth * width)/2);
		int pixelHeightOffset = Math.round((LCD.SCREEN_HEIGHT - pixelHeight * height)/2);
		int xStart = pixelWidthOffset + (x * pixelWidth);
		int yStart = pixelHeightOffset + (y * pixelHeight);
		
		for (int i = xStart; i < xStart + pixelWidth; i++) {
			for (int j = yStart; j < yStart + pixelHeight; j++) {
				LCD.setPixel(i, j, 1);
			}
		}
	}
}
