package com.cvanbattum.seamtest;

import java.awt.Color;

/**
 * @version 1.0
 */
public class ColorMath {

	/**
	 * The color difference is the mathematical average of the differences of each of the colors' channels. This means 
	 * that the difference (as a percentage of a channel's maximum value, 255) is calculated for each channel.
	 * 
	 * @param color1 A color
	 * @param color2 A color
	 * @return The difference between the two given colors
	 */
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

	/**
	 * <p>The brightness difference is the average of the differences in perceptual brightness off each channel from two 
	 * colors. The returned value is a float between 1 and 0.</p>
	 * <p>The formula for the perceptual brightness of a color is: </p>
	 * <pre>red * 0.2126 + green * 0.7152 + blue * 0.0722</pre>
	 * 
	 * @param color1 A color
	 * @param color2 A color
	 * @return The difference in perceptual brightness from two colors.
	 */
	public static float getBrightnessDifference(Color color1, Color color2) {
		double percBright1 = color1.getRed() * 0.2126 + color1.getGreen() * 0.7152 + color1.getBlue() * 0.0722;
		double percBright2 = color2.getRed() * 0.2126 + color2.getGreen() * 0.7152 + color2.getBlue() * 0.0722;

		return (float) (Math.abs(percBright1 - percBright2) / 255.0f);
	}
	
	/**
	 * <p>Simple average color calculation method. The average of the colors is calculated by taking the average of each 
	 * channel (R, G and B), and then creating the new color with these averages.</p>
	 * <p>This method supports alpha. When <code>useAlpha</code> is <code>true</code>, the method will also calculate 
	 * the average of the alpha channels of both colors</p>
	 * 
 	 * @param color1 a color
	 * @param color2 a color
	 * @param useAlpha When <code>true</code> is passed, the method will also take into account the alpha channel when 
	 *                 calculating the average.
	 * @return The mathematical average of two colors. 
	 */
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

	/**
	 * Calculates the average of a set of numbers.
	 * 
	 * @param nums The numbers to calculate the average from
	 * @return The average of the numbers in the <code>nums</code> array
	 */
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
