package com.cvanbattum.seamtest;

import com.cvanbattum.seamtest.tester.VisualisationDrawer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Logger;

public class OffsetChecker {
	
	private static final Logger LOGGER = Logger.getLogger("seamtest");
	private static final float DIFFERENCE_TRESHOLD = 0.1f;
	
	private int steps;  //Total amount of steps
	private int step = 0;   //Current step
	
	private boolean running;
	
	private final BufferedImage img;
	
	public OffsetChecker(BufferedImage img) {
		this.img = img;
	}
	
	public boolean checkOffset(int seamOffset, int tileSize, int stepSize, Orientation orientation) {
		if (running) {
			throw new IllegalStateException("Can't invoke this method when program is already running");
		}
		
		// Test if the parameters are valid
		testValues(seamOffset, tileSize, stepSize);
		
		//TODO: test values
		int signifBrights = 0;
		int signifColors = 0;
		
		//Start the program by setting it to run
		setRunning(true);
		//Calculate the number of steps this test will take
		calculateSteps(tileSize, stepSize, orientation);
		
		//Initiate iterator lines; the checker follows these lines
		int baseLine  = seamOffset - tileSize;
		int compareLine = seamOffset;
		
		//Initiate visualisers
		VisualisationDrawer vd_color = new VisualisationDrawer(tileSize, steps * tileSize, orientation);
		vd_color.start();
		VisualisationDrawer vd_bright = new VisualisationDrawer(tileSize, steps * tileSize, orientation);
		vd_bright.start();
		
		//Loop through image
		while (next()) {
			//This code happens every tile
			int location = (step - 1) * tileSize;

			int tilePixelCount = tileSize * tileSize;
			int pixelCounter = 0;
			
			// Initialize pixel color arrays
			Color[] baseColors = new Color[tilePixelCount];
			Color[] compareColors = new Color[tilePixelCount];
			
			//Loop through tile (outer loop controls x, inner loop controls y; see labels)
			x:for (int i = 0; i < tileSize; i++) {
				y:for (int j = 0; j < tileSize; j++) {
					try {
						if (orientation == Orientation.VERTICAL) {
							baseColors[pixelCounter] = getColorAt(baseLine + i, location + j);
							
							compareColors[pixelCounter] = getColorAt(compareLine + i, location + j);
						}
						else {
							baseColors[pixelCounter] = getColorAt(location + i, baseLine + j);
							
							compareColors[pixelCounter] = getColorAt(location + i, compareLine + j);
						}
						
					}
					catch (ArrayIndexOutOfBoundsException e) {
						//Not supposed to happen, as it should be filtered out by the getCoordinatesInBounds method.
						e.printStackTrace();
					}
					
					pixelCounter++;
				}
			}
			
			Color avgBaseColor = ColorMath.getAverageColor(false, baseColors);
			Color avgCompareColor = ColorMath.getAverageColor(false, compareColors);
			
			float colorDiff = ColorMath.getColorDifference(avgBaseColor, avgCompareColor);
			float brightDiff = ColorMath.getBrightnessDifference(avgBaseColor, avgCompareColor);
			
			LOGGER.info("Color difference at tile " + step + " is " + (colorDiff * 100) + "%");
			if (colorDiff > DIFFERENCE_TRESHOLD) {
				signifColors++;
				
				LOGGER.info("Color difference is significant");
			}
			
			vd_color.step(avgBaseColor, avgCompareColor, colorDiff > DIFFERENCE_TRESHOLD);

			LOGGER.info("Brightness difference at tile " + step + " is " + (brightDiff * 100) + "%");
			if (brightDiff > DIFFERENCE_TRESHOLD) {
				signifBrights++;
				
				LOGGER.info("Brightness difference is significant");
			}
			
			vd_bright.step(avgBaseColor, avgCompareColor, brightDiff > DIFFERENCE_TRESHOLD);
		}
		
		vd_bright.stop();
		vd_color.stop();
		
		try {
			vd_bright.save("brightDiffVisual_tex_test2.jpg");
			vd_color.save("colorDiffVisual_tex_test2.jpg");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		LOGGER.info("Finished. Found " + signifColors + " significant colors and " + signifBrights + " significant brights out of " + steps + " tiles.");
		
		return false;
	}

	private void testValues(int seamOffset, int tileSize, int stepSize) {
		// Test seamOffset. 0 is allowed, as it indicates the seam is located at the at the edges of the image. This 
		// test is performed in the main method.
		if (seamOffset < 0) {
			throw new IllegalArgumentException("seamOffset argument must be zero or larger. Value given: " + seamOffset);
		}
		
		// Tiles must be at least one pixel large, therefore the minimum tile size is 1.
		if (tileSize <= 0) {
			throw new IllegalArgumentException("tileSize must be larger than zero. Value given: " + tileSize);
		}
		
		// Step size indicates the amount of pixels that is advanced (not skipped) directly after a tile. 1 means the 
		// next tile is placed 1 pixel further than the previous tile. Therefore, 1 is the minumum value.
		if (stepSize <= 0) {
			throw new IllegalArgumentException("stepSize must be larger than zero. Value given: " + stepSize);
		}
	}

	private void calculateSteps(int tileSize, int stepSize, Orientation orientation) {
		int it_pixelLength = getLineLength(orientation);
		
		//Check if the arguments are valid
		if (stepSize > it_pixelLength) {
			throw new IllegalArgumentException("Step size must be smaller than the image");
		}
		else if (tileSize > it_pixelLength) {
			throw new IllegalArgumentException("Tile size must be smaller than the image");
		}
		
		//Calculate the steps
		if (stepSize == 0) {
			steps = Math.floorDiv(it_pixelLength, tileSize);
		}
		else if (stepSize > 0) {
			steps = Math.floorDiv(it_pixelLength, stepSize);
		}
		else throw new IllegalArgumentException("Step size must be greater than 0");
		
		LOGGER.info("Calculated " + steps + " steps");
	}
	
	private boolean next() {
		if (running) {
			if (step < steps) {
				step++;
				return true;
			}
			else {
				//Iterator has ended
				return false;
			}
		}
		else {
			System.out.println("Program was stopped.");
			return false;
		}
	}
	
	private boolean setRunning(boolean running) {
		if (running != this.running) {
			//Set steps to 0 whenever running is set to true
			step = 0;
			
			//Set running to true;
			this.running = running;
			
			LOGGER.info("Offset checking now running!");
			return true;
		}
		else {
			return false;
		}
	}
	
	private int getLineLength(Orientation o) {
		int it_pixelLength; //Iterator pixel length
		if (o == Orientation.VERTICAL) {
			it_pixelLength = img.getHeight();
		}
		else if (o == Orientation.HORIZONTAL) {
			it_pixelLength = img.getWidth();
		}
		else throw new IllegalStateException("getLineLength() ran into an error (orientation was neither horizontal or vertical)");
		
		return it_pixelLength;
	}
	
	private Color getColorAt(int x, int y) {
		if (getCoordinateInBounds(x, y)) {
			return new Color(img.getRGB(x, y));
		}
		else return null;
	}
	
	private boolean getCoordinateInBounds(int x, int y) {
		return !(x >= img.getWidth() || y >= img.getHeight());
	}
	
}
