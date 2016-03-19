package com.cvanbattum.seamtest.tester;

import com.cvanbattum.seamtest.Orientation;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Casper van Battum on 26-10-2015.
 */
public class VisualisationDrawer {
	
	private int scale = 100;
	
	private BufferedImage img;
	private Graphics2D g2;
	
	private boolean running;
	private int step;
	
	private Color diffColor = Color.BLUE;
	private Color altDiffColor = new Color(212, 212, 255);
	private Color noDiffColor = Color.RED;
	private Color altNoDiffColor = new Color(255, 212, 212);
	
	private final int tileSize;
	private final int height;
	private final Orientation ortn;
	
	public VisualisationDrawer(int tileSize, int height, Orientation orientation) {
		this.tileSize = tileSize;
		this.height = height;
		
		this.ortn = orientation;
	}
	
	public void start() {
		if (running) {
			throw new IllegalStateException("Can't start the drawer when it is already running.");
		}
		
		img = new BufferedImage(2 * tileSize * scale + scale, height * scale, BufferedImage.TYPE_INT_ARGB);
		g2 = img.createGraphics();
		
		step = 0;
		running = true;
		
	}
	
	public void step(Color c1, Color c2, boolean signif) {
		if (! running) {
			throw new IllegalStateException("Drawer can't step when it is not running");
		}
		
//		if (ortn == Orientation.HORIZONTAL) {
//			//Draw first tile
//			g2.setColor(c1);
//			g2.drawRect(step * tileSize, 0, tileSize, tileSize);
//			
//			//Draw line
//			if (signif) {
//				g2.setColor(diffColor);
//			}
//			else {
//				g2.setColor(noDiffColor);
//			}
//			g2.drawRect(step * tileSize, tileSize, tileSize, 1);
//			
//			//Draw second tile
//			g2.setColor(c2);
//			g2.drawRect(step * tileSize, tileSize + 1, tileSize, tileSize);
//		}
//		else if (ortn == Orientation.VERTICAL) {
			//Draw first tile
			g2.setColor(c1);
			g2.fillRect(0, step * tileSize * scale, tileSize * scale, tileSize * scale);

			//Draw line
			if (signif) {
				g2.setColor(diffColor);
			}
			else {
				g2.setColor(noDiffColor);
			}
			g2.fillRect(tileSize * scale, step * tileSize * scale, scale, tileSize * scale);

			//Draw second tile
			g2.setColor(c2);
			g2.fillRect(tileSize * scale + scale, step * tileSize * scale, tileSize * scale, tileSize * scale);
//		}
		
		step++;
		
	}
	
	public void stop() {
		if (! running) {
			throw new IllegalStateException("Can't stop the drawer when it is not running.");
		}
		
		running = false;
		System.out.println(step);
		
	}

	/**
	 * Saves the generated visualisation to a PNG image of <code>testImageName</code> to the 
	 * <code>[usrHome]/Seamless</code> folder.
	 * <p>
	 *     <b>Note:</b><br/>
	 *     This method does not create the folder so it will give an error is it doesn't already exist on the computer
	 * </p>
	 * @param testImageName Name of the file to be saved.
	 * @throws IOException
	 */
	public void save(String testImageName) throws IOException {
		if (running) {
			throw new IllegalStateException("Can't save image when the drawer is running.");
		}
		
		ImageIO.write(img, "PNG", new File(System.getProperty("user.home") + "\\Seamless\\" + testImageName + (testImageName.endsWith(".png") ? null : ".png")));
		
	}
	
}
