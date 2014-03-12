package net.shiftinpower.utilities;

/**
 * 
 * Returns a random number between the min and the max values specified
 * 
 * @author Kaloyan Roussev
 */
public class Randomizer {

	public static int returnRandomNumberBetween(int min, int max) {
		int d = (max - min) + 1;
		int c = min + (int) (Math.random() * ((d)));
		return c;
	}
} // End of Class
