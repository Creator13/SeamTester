package com.cvanbattum.seamtest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class SeamTester {
	
	private static final Logger LOGGER = Logger.getLogger("seamtest");
	private static final String LOG_FILE_LOCATION = System.getProperty("user.home") + "\\Seamless\\seamTester.log";

	//Set up logger
	static {
		Formatter f = new Formatter() {
			@Override
			public String format(LogRecord record) {
				String s = "";

				s += "[" + formatMillis(record.getMillis()) + "] [" + record.getLevel() + "] ";
				s += record.getMessage();
				s += "\n";

				return s;
			}

			@Override
			public String getHead(Handler h) {
				return "Program started at " + formatMillis(System.currentTimeMillis()) + ".\n";
			}

			private String formatMillis(long millis) {
				Date date = new Date(millis);
				DateFormat formatter = new SimpleDateFormat("dd-MM-YYYY @ HH:mm:ss:SSS");
				String dateFormatted = formatter.format(date);

				return dateFormatted;
			}
		};

		try {
			LOGGER.setUseParentHandlers(false);

			Handler fh = new FileHandler(LOG_FILE_LOCATION, true);
			fh.setFormatter(f);
			
			Handler ch = new ConsoleHandler();
			ch.setFormatter(f);

			LOGGER.addHandler(fh);
			LOGGER.addHandler(ch);
		}
		catch (IOException e) {
			System.err.println("An error occurred while opening log file.");
		}

	}
	
	public static void main(String[] args) {
		//Just an argument check
		if (args.length == 1) {
			File f = new File(args[0]);
			
			if (f.exists()) {
				try {
					BufferedImage img = ImageIO.read(f);
					
					if (img != null) {
						testImage(img, 363);
					}
					else {
						printInvalidImage();
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			else {
				LOGGER.severe("File " + args[0] + " not found!");
			}
		}
		else {
			LOGGER.severe("Only one argument is allowed! Please remove any pending arguments.");
		}
		
	}
	
	private static void testImage(BufferedImage img, int seamOffset) {
		OffsetChecker it = new OffsetChecker(img);
		
		it.checkOffset(seamOffset, 1, 1, Orientation.HORIZONTAL);
	}
	
	private static void printInvalidImage() {
		LOGGER.severe("This file is not an image!");
	}
	
}
