package com.cvanbattum.seamtest;

import java.awt.image.BufferedImage;
import java.util.logging.Logger;

public class OffsetChecker {
	
	private static final Logger LOGGER = Logger.getLogger("seamtest");
	
	private int steps;  //Total amount of steps
	private int step;   //Current step
	
	private boolean running;
	
	private final BufferedImage img;
	
	public OffsetChecker(BufferedImage img) {
		this.img = img;
	}
	
	public boolean checkOffset(int seamOffset, int tileSize, int stepSize, Orientation orientation) {
		//Start the program by setting it to run
		setRunning(true);
		//Calculate the number of steps this test will take
		calculateSteps(tileSize, stepSize, orientation);
		
		while (next()) {
			
		}
		
		return false;
	}
	
	private void calculateSteps(int tileSize, int stepSize, Orientation orientation) {
		int it_pixelLength; //Iterator pixel length
		if (orientation == Orientation.VERTICAL) {
			it_pixelLength = img.getHeight();
		}
		else if (orientation == Orientation.HORIZONTAL) {
			it_pixelLength = img.getWidth();
		}
		else throw new IllegalStateException("calculateSteps() ran into an error");
		
		//Check if the arguments are valid
		if (stepSize > it_pixelLength) {
			throw new IllegalArgumentException("Step size must be smaller than the image");
		}
		else if (tileSize > it_pixelLength) {
			throw new IllegalArgumentException("Tile size must be smaller than the image");
		}
		
		//Calculate the size
		if (stepSize == 0) {
			steps = it_pixelLength / tileSize; 
		}
		else if (stepSize > 0) {
			steps = it_pixelLength / stepSize;
		}
		else throw new IllegalArgumentException("Step size must be greater than 0");
		
		LOGGER.info("Calculated " + steps + " steps");
	}
	
	private boolean next() {
		if (running) {
			if (step < steps) {
				steps++;
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
			
			LOGGER.info("offset checking now running!");
			return true;
		}
		else {
			return false;
		}
	}
	
}
