package org.yamunasoftware.jvision;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class Lighting extends Analyze {
	/* MAIN LIGHT VARIABLES */

	// Lighting Image Variables:
	private static ArrayList<Double> avgValues = new ArrayList<Double>();
	private static ArrayList<Double> sortedValues = new ArrayList<Double>();
	private static double ratio = 0.5;
	private static int flips = 0;

	/* MAIN LIGHT METHODS */

	// Constructor:
	public Lighting() {
		super();
	}

	// Multiple Lighting Thresholds Method:
	public static ArrayList<boolean[]> getLightingThresholds(String filePaths[], String type, double upper, double lower)
		throws Exception {
		// Loop Variables:
		ArrayList<boolean[]> thresholds = new ArrayList<boolean[]>();
		int turns = 0;

		// Loops through Array:
		mainLoop: while (turns < filePaths.length) {
			// Gets the Boolean:
			boolean imageThresholds[] = getLightingThreshold(filePaths[turns], type, upper, lower);
			thresholds.add(imageThresholds);

			turns++;
		}

		// Returns the ArrayList:
		return thresholds;
	}

	// Lighting Threshold Method:
	public static boolean[] getLightingThreshold(String filePath, String type, double upper, double lower)
		throws Exception {
		// Gets the Lighting:
		BufferedImage image = openImage(filePath, type);
		BufferedImage localImage = resizeBufferedImageRatio(image, ratio);
		int rgbValues[][] = findSelectedBufferedRGB(localImage, 0, 0, localImage.getWidth(), localImage.getHeight());
		int rgb[] = averageRGBValues(rgbValues);
		double avg = (rgb[0] + rgb[1] + rgb[2]) / 3;
		boolean thresholds[] = new boolean[2];

		// Checks the Case:
		if (avg > upper) {
			// Sets the Boolean:
			thresholds[0] = true;
		}

		else {
			// Sets the Boolean:
			thresholds[0] = false;
		}

		// Checks the Case:
		if (avg < lower) {
			// Sets the Boolean:
			thresholds[1] = true;
		}

		else {
			// Sets the Boolean:
			thresholds[1] = false;
		}

		// Returns the Boolean:
		return thresholds;
	}

	/* MAIN LIGHT INFO METHODS */

	// Get Lightest Image Method:
	public static double[] getLightInfo(String filePaths[], String type) throws Exception {
		// Main Light Variables:
		int lightest = 0, darkest = 0;
		double lightestValue = 0, darkestValue = 0;
		ArrayList<BufferedImage> images = getImages(filePaths, type);

		// Array Variables:
		double avgRGB[] = new double[images.size()];
		int turns = 0;

		// Loops through Array:
		mainLoop: while (turns < avgRGB.length) {
			// Gets the Average Value:
			BufferedImage localImage = images.get(turns);
			int rgbValues[][] = findSelectedBufferedRGB(localImage, 0, 0, localImage.getWidth(), localImage.getHeight());
			int rgb[] = averageRGBValues(rgbValues);
			avgRGB[turns] = (rgb[0] + rgb[1] + rgb[2]) / 3;

			turns++;
		}

		// Gets the Arrays:
		double best[] = getBests(avgRGB, true);
		double worst[] = getBests(avgRGB, false);

		// Sets the Light Indexes:
		lightest = (int) (best[0]);
		darkest = (int) (worst[0]);

		// Sets the Light Values:
		lightestValue = best[1];
		darkestValue = worst[1];

		// Final Save Operations:
		avgValues = toArrayList(avgRGB);
		double sorted[] = sortArray(avgRGB);
		sortedValues = toArrayList(sorted);

		// Gets Array and Returns:
		double returnArray[] = { lightest, lightestValue, darkest, darkestValue };
		return returnArray;
	}

	// Get All Images Method:
	public static ArrayList<BufferedImage> getImages(String filePaths[], String type) throws Exception {
		// Loop Variables:
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		int turns = 0;

		// Loops through Array:
		mainLoop: while (turns < filePaths.length) {
			// Gets Image and Ratio:
			BufferedImage localImage = openImage(filePaths[turns], type);
			BufferedImage resized = resizeBufferedImageRatio(localImage, ratio);
			images.add(resized);

			turns++;
		}

		// Returns ArrayList:
		return images;
	}

	/* GETTER METHODS */

	// Get Average Values Method:
	public static ArrayList<Double> getAverageValues() throws Exception {
		// Returns the Values:
		return avgValues;
	}

	// Get Sorted Values Method:
	public static ArrayList<Double> getSortedValues() throws Exception {
		// Returns the Values:
		return sortedValues;
	}

	// Get Ratio Method:
	public static double getRatio() throws Exception {
		// Returns the Ratio:
		return ratio;
	}

	/* UTILITIES METHODS */

	// To ArrayList Method:
	public static ArrayList<Double> toArrayList(double values[]) throws Exception {
		// Loop Variables:
		ArrayList<Double> list = new ArrayList<Double>();
		int turns = 0;

		// Loops through Array:
		mainLoop: while (turns < values.length) {
			// Adds to the ArrayList:
			list.add(values[turns]);

			turns++;
		}

		// Returns the ArrayList:
		return list;
	}

	// Get Index Method:
	public static double[] getBests(double values[], boolean highest) throws Exception {
		// Loop Variables:
		int turns = 0;
		int findIndex = 0;
		double findValue = 0;

		// Loops through Array:
		mainLoop: while (turns < values.length) {
			// Checks the Case:
			if (turns == 0) {
				// Sets the Values:
				findIndex = turns;
				findValue = values[turns];
			}

			else {
				// Checks the Case:
				if (highest) {
					// Checks the Case:
					if (values[turns] >= findValue) {
						// Sets the Values:
						findIndex = turns;
						findValue = values[turns];
					}
				}

				else {
					// Checks the Case:
					if (values[turns] <= findValue) {
						// Sets the Values:
						findIndex = turns;
						findValue = values[turns];
					}
				}
			}

			turns++;
		}

		// Returns the Index:
		double bests[] = { findIndex, findValue };
		return bests;
	}

	// Sort Array Method (Double):
	public static double[] sortArray(double values[]) throws Exception {
		// Loop Variable:
		double array[] = values;
		int turns = 0;

		// Loops through Array:
		mainLoop: while (turns < array.length) {
			// Checks the Case:
			if (turns < array.length - 1) {
				// Checks the Case:
				if (array[turns] >= array[turns + 1]) {
					// Flips the Values:
					double value = array[turns];
					array[turns] = array[turns + 1];
					array[turns + 1] = value;
					flips++;
				}
			}

			turns++;
		}

		// Checks the Case:
		if (flips != 0) {
			// Runs Again:
			flips = 0;
			sortArray(array);
		}

		// Returns the Array:
		return array;
	}
}