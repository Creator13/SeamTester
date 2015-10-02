package com.cvanbattum.seamtest;

/**
 * @version 1.0
 */
public enum Orientation {

	//#### ENUM CONSTANTS ####//
	/**
	 * Horizontal orientation
	 */
	HORIZONTAL("Horizontal", "Width"),
	
	/**
	 * Vertical orientation
	 */
	VERTICAL("Vertical", "Height");
	
	//### NAMES and METHODS ###//
	private String name;                    //String representation of the orientation
	private String correspondingDimension;  //String representation of the image dimension name for this orientation
	
	Orientation(String name, String correspondingDimension) {
		this.name = name;
		this.correspondingDimension = correspondingDimension;
	}

	/**
	 * <p>The <code>toString()</code> method of the <code>Orientation</code> class returns a formatted string of this 
	 * orientation. The first letter is capitalized.</p>
	 * <p>By passing <code>true</code> for the <code>lowerCase</code> parameter, the method will return a fully 
	 * lower case word, for the use in a sentence.</p>
	 * 
	 * @param lowerCase Whether the returned string should be lower case or not.
	 * @return A formatted string for this orientation
	 */
	public String toString(boolean lowerCase) {
		if (lowerCase) {
			return this.name.toLowerCase();
		}
		else return this.name;
	}

	/**
	 * <p>The <code>toString()</code> method of the <code>Orientation</code> class returns a formatted string describing 
	 * the corresponding dimension name for this orientation. This means width for horizontal and height for vertical. 
	 * The first letter is capitalized.</p>
	 * <p>By passing <code>true</code> for the <code>lowerCase</code> parameter, the method will return a fully lower 
	 * case word, for the use in a sentence.</p>
	 *
	 * @param lowerCase Whether the returned string should be lower case or not.
	 * @return A formatted string of the corresponding dimension name.
	 */
	public String getDimensionName(boolean lowerCase) {
		if (lowerCase) {
			return this.name.toLowerCase();
		}
		else return this.correspondingDimension;
	}
	
}
