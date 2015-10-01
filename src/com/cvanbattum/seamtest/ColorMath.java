package com.cvanbattum.seamtest;

import java.awt.Color;

public class ColorMath {

	public static float getColorDifference(Color color1, Color color2) {
		float max = 255.0f;

		float redDiff;
		int red1 = color1.getRed();
		int red2 = color2.getRed();
		redDiff = getDifference(red1, red2, max);

		float greenDiff;
		int green1 = color1.getGreen();
		int green2 = color2.getGreen();
		greenDiff = getDifference(green1, green2, max);

		float blueDiff;
		int blue1 = color1.getBlue();
		int blue2 = color2.getBlue();
		blueDiff = getDifference(blue1, blue2, max);

		//Return average difference
		return (redDiff + greenDiff + blueDiff) / 3;
	}

	public static float getBrightnessDifference(Color color1, Color color2) {
		//Formula: 0.2126*R + 0.7152*G + 0.0722*B (perceptual brightness)
		
		
	}

	public static Color getAverageColor(Color color1, Color color2, boolean useAlpha) {
		int redAvg = getAverage(color1.getRed(), color2.getRed());
		int greenAvg = getAverage(color1.getGreen(), color2.getGreen());
		int blueAvg = getAverage(color1.getBlue(), color2.getBlue());
		
		if (useAlpha) {
			int alphaAvg = getAverage(color1.getAlpha(), color2.getAlpha());
			return new Color(redAvg, greenAvg, blueAvg, alphaAvg);
		}
		
		return new Color(redAvg, greenAvg, blueAvg);
	}

	/**
	 * <p>Calculates the difference between two integers as a percentage of a maximum value. The range is defined by the
	 * <code>max</code> parameter, and 0 on the low side.</p>
	 * <p>The percentage is given as a float between 1 and 0.</p>
	 * 
	 * @param a Value 1
	 * @param b Value 2
	 * @param max The range
	 * @return the difference as a percentage of the maximum value.
	 */
	private static float getDifference(int a, int b, float max) {
		return Math.abs(a - b) / max;
	}
	
	private static int getAverage(int... nums) {
		if (nums.length == 1) {
			//Return only number in list
			return nums[0];
		}
		else if (nums.length == 2) {
			//Return simple average of two numbers (faster than loop)
			return (nums[0] + nums[1]) / 2;
		}
		else {
			//Slowest method for >= 2 entries, adds all numbers to a total and divides by n 
			int total = 0;

			for (int num : nums) {
				total += num;
			}

			return total / nums.length;
		}
		
	}
	
}
