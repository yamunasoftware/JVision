package org.yamunasoftware.jvision;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("unused")
public class Analyze extends Capture {
	// Detector Settings (w/ Defaults):
	private static int detectorRed = 0, detectorGreen = 0, detectorBlue = 0;
	private static int detectorArray[] = new int[3];
	private static String detectorName = "Detector";
	private static int booleanPixelCount = 0;

	// Blob Information ArrayLists (w/ Defaults):
	private static ArrayList<Integer> pixelCounts = new ArrayList<Integer>();
	private static ArrayList<Integer> x = new ArrayList<Integer>();
	private static ArrayList<Integer> y = new ArrayList<Integer>();
	private static ArrayList<Integer> lastX = new ArrayList<Integer>();
	private static ArrayList<Integer> lastY = new ArrayList<Integer>();

	/* CONSTRUCTOR AND SET METHODS */

	// Constructor:
	public Analyze() {
		super();
	}

	// Initializes Detector:
	public static void initDetector(String localDetectorName, int red, int green, int blue) throws Exception {
		// Sets the Detector Name:
		detectorName = localDetectorName;

		// Checks Case:
		if (red <= 255 && red >= 0 && green <= 255 && green >= 0 && blue <= 255 && blue >= 0) {
			// Setting the Detector Values:
			detectorRed = red;
			detectorGreen = green;
			detectorBlue = blue;
		}

		else {
			// Setting the Detector Values (Default):
			detectorRed = 0;
			detectorGreen = 0;
			detectorBlue = 0;
		}

		// Sets the Detection Array:
		detectorArray[0] = detectorRed;
		detectorArray[1] = detectorGreen;
		detectorArray[2] = detectorBlue;
	}

	/* IMAGE PRE-PROCESSING METHODS (SPECIFICALLY BUFFERED IMAGE ONLY) */

	// Resizes the Buffered Image Using the Aspect Ratio:
	public static BufferedImage resizeBufferedImageRatio(BufferedImage userImage, double resizeRatio) throws Exception {
		if (resizeRatio != 0.0) {
			// Gets the Image Width and Height using the Methods:
			double width = userImage.getWidth();
			double height = userImage.getHeight();

			// Calculates the new Width and Height:
			int newWidth = Math.abs((int) (width * resizeRatio));
			int newHeight = Math.abs((int) (height * resizeRatio));

			// Resizes the Buffered Image:
			BufferedImage newImage = resizeBufferedImage(userImage, newWidth, newHeight);

			// Returns the Image:
			return newImage;
		}

		else {
			// Error Statement:
			System.out.println("Invalid Resize Ratio!");
			return null;
		}
	}

	// Finds all RGB Values for Selected Area of User Buffered Image:
	public static int[][] findSelectedBufferedRGB(BufferedImage userImage, int startX, int startY, int width, int height)
			throws Exception {
		// Main Arrays:
		int zeroArray[][] = { { 0 } }; // Incorrect Enterance Array

		// Checks Case:
		if (userImage != null) {
			// Array Values:
			int imageWidth = userImage.getWidth();
			int imageHeight = userImage.getHeight();
			int allValueList[][] = new int[width][height];

			// Loop Values:
			int turnsWidth = startX;

			// Checks the Case:
			if (startX >= 0 && startX + width <= imageWidth && startY >= 0 && startY + height <= imageHeight) {
				// Loops through and Finds All Values:
				mainLoop: while (turnsWidth < startX + width) {
					// Loop Variable:
					int turnsHeight = startY;
					secondLoop: while (turnsHeight < startY + height) {
						allValueList[turnsWidth - startX][turnsHeight - startY] = userImage.getRGB(turnsWidth, turnsHeight);

						turnsHeight++;
					}

					turnsWidth++;
				}

				// Returns Array:
				return allValueList;
			}

			else {
				// Error Debug:
				System.out.println("Selected Area is not Possible!");

				// Returns False Array:
				return zeroArray;
			}
		}

		else {
			// Error Message:
			System.out.println("Null Buffered Image Detected!");
			return zeroArray;
		}
	}

	/* IMAGE SCREENING METHODS */

	// Grid Creation Method (Compatible with Arrays and Loops):
	public static int[][] getGrid(int imageWidth, int imageHeight, int squareValue) throws Exception {
		// Main Array:
		int gridValues[][] = new int[2][(squareValue * squareValue)];

		// Checks the Case:
		if (squareValue != 0) {
			// Standard Variables:
			int width = imageWidth / squareValue;
			int height = imageHeight / squareValue;

			// Loop Variables:
			int turns = 0;
			int squareWidthCount = 0, squareHeightCount = 0;

			// Loops to Find Values:
			mainLoop: while (turns < gridValues[0].length) {
				// Sets the First Coordinates:
				if (turns == 0) {
					// Setting the First Coordinates:
					gridValues[0][turns] = 0;
					gridValues[1][turns] = 0;

					// Adds to the Count:
					squareWidthCount++;
					squareHeightCount++;
				}

				else {
					if (squareWidthCount < squareValue) {
						// Setting Coordinates:
						gridValues[0][turns] = gridValues[0][turns - 1] + width;
						gridValues[1][turns] = gridValues[1][turns - 1];

						// Adds to the Count:
						squareWidthCount++;
					}

					else if (squareWidthCount >= squareValue && squareHeightCount < squareValue) {
						// Setting Coordinates:
						gridValues[0][turns] = 0;
						gridValues[1][turns] = gridValues[1][turns - 1] + height;

						// Resets the Count:
						squareWidthCount = 1;
						squareHeightCount++;
					}
				}

				turns++;
			}
		}

		else {
			// Error Debugs:
			System.err.println("Invalid Square Enterance!");
		}

		// Returns the Grid Array:
		return gridValues;
	}

	// Gets a Section of an RGB Array Method:
	public static int[][] getSection(int rgbValues[][], int startX, int startY, int width, int height) throws Exception {
		// Main Array to Be Returned:
		int section[][] = new int[width][height];

		// Checks the Case:
		if (startX >= 0 && startY >= 0 && startX + width <= rgbValues.length && startY + height <= rgbValues[0].length) {
			// Loop Variable:
			int turnsWidth = startX;

			// Loops through Array:
			mainLoop: while (turnsWidth < startX + width) {
				// Loop Variable:
				int turnsHeight = startY;
				secondLoop: while (turnsHeight < startY + height) {
					// Adds to the Section Array:
					section[turnsWidth - startX][turnsHeight - startY] = rgbValues[turnsWidth][turnsHeight];

					turnsHeight++;
				}

				turnsWidth++;
			}
		}

		else {
			// Error Debugs:
			System.err.println("Input Error!");
		}

		// Returns the Array:
		return section;
	}

	/* COLOR ANALYSIS METHODS */

	// Get Average RGB Values (FOR COMPARISON):
	public static int[] averageRGBValues(int[][] rgbValues) throws Exception {
		// Main Array:
		int averageRGB[] = { 0, 0, 0 }; // Default Values

		// Checks the Case:
		if (rgbValues.length != 0 && rgbValues[0].length != 0) {
			// Loop Variables:
			int totalAmount = (rgbValues.length) * (rgbValues[0].length);
			int turnsWidth = 0;

			// Finds the Average RGB Values:
			mainLoop: while (turnsWidth < rgbValues.length) {
				// Loop Variable:
				int turnsHeight = 0;
				secondLoop: while (turnsHeight < rgbValues[0].length) {
					// Converts to Standard RGB Values:
					int localRGBValues[] = convertRGB(rgbValues[turnsWidth][turnsHeight]);

					// Adds to Current Average Values:
					averageRGB[0] += localRGBValues[0];
					averageRGB[1] += localRGBValues[1];
					averageRGB[2] += localRGBValues[2];

					turnsHeight++;
				}

				turnsWidth++;
			}

			// Finds the Average:
			averageRGB[0] /= totalAmount;
			averageRGB[1] /= totalAmount;
			averageRGB[2] /= totalAmount;
		}

		else {
			// Error Message:
			System.out.println("Incorrect RGB Values Length!");
		}

		// Returns the Value:
		return averageRGB;
	}

	// Binary Detector (Converts to Zeros and Ones):
	public static int[][] binaryDetector(int[][] rgbValues, int[] lightingMargin) throws Exception {
		// Box Values Array:
		int boxValuesArray[][] = new int[rgbValues.length][rgbValues[0].length];
		int setRGBValues[] = new int[3];

		// Sets the RGB Value:
		setRGBValues[0] = detectorRed;
		setRGBValues[1] = detectorGreen;
		setRGBValues[2] = detectorBlue;

		// Checks the Case:
		if (rgbValues.length != 0 && rgbValues[0].length != 0) {
			// Loop Variables:
			int turnsWidth = 0;

			// Loops Through the Array (and checks case):
			mainLoop: while (turnsWidth < boxValuesArray.length) {
				// Loop Variable:
				int turnsHeight = 0;
				secondLoop: while (turnsHeight < boxValuesArray[turnsWidth].length) {
					// Converts to Regular RGB:
					int converted[] = convertRGB(rgbValues[turnsWidth][turnsHeight]);

					// Checks the Case:
					if (isWithinRange(detectorArray, converted, lightingMargin)) {
						// Sets the Values:
						boxValuesArray[turnsWidth][turnsHeight] = 1;
					}

					else {
						// Sets the Values:
						boxValuesArray[turnsWidth][turnsHeight] = 0;
					}

					turnsHeight++;
				}

				turnsWidth++;
			}
		}

		else {
			// Error Debugs:
			System.out.println("Incorrect RGB Values Length!");
		}

		// Returns the Accepted Values Array:
		return boxValuesArray;
	}

	/* DETECTION METHODS */

	// Get Nearest Pixel Specified (IF PRESENT):
	public static int[] getNearestPixel(int rgbValues[][], int lightingMargin[]) throws Exception {
		// Main Coordinates Array (w/ Default):
		int coordinates[] = new int[2];
		int lowestDistance = 0, distance = 0;

		// Checks the Case:
		if (rgbValues.length != 0) {
			// Loop Variables:
			int turnsWidth = 0;
			int detectionCount = 0;

			// Loops through Array:
			mainLoop: while (turnsWidth < rgbValues.length) {
				// Loop variable:
				int turnsHeight = 0;
				secondLoop: while (turnsHeight < rgbValues[0].length) {
					// Gets the RGB Array Values:
					int localRGB[] = convertRGB(rgbValues[turnsWidth][turnsHeight]);
					distance = (int) (getDistance(localRGB, detectorArray));

					// Checks the Case:
					if (isWithinRange(detectorArray, localRGB, lightingMargin)) {
						// Adds to the Detection Count:
						detectionCount++;

						// Checks the Case:
						if (detectionCount == 0) {
							// Sets the Coordinates and Distance:
							coordinates[0] = turnsWidth;
							coordinates[1] = turnsHeight;
							lowestDistance = distance;
						}

						else {
							// Checks the Case:
							if (distance <= lowestDistance) {
								// Sets the Coordinates and Distance:
								coordinates[0] = turnsWidth;
								coordinates[1] = turnsHeight;
								lowestDistance = distance;
							}
						}
					}

					turnsHeight++;
				}

				turnsWidth++;
			}
		}

		// Returns the Nearest Coordinate:
		return coordinates;
	}

	// Get Selected Area Boolean (IF PRESENT):
	public static boolean getBodyBoolean(int detectionArray[][], int pixelCount) throws Exception {
		// Main Value:
		boolean valuePresent = false; // Default Value

		// Loop Variables:
		int turnsWidth = 0;
		int trueCount = 0;

		// Count Value (Helps Mitigate Stray Pixels):
		int countValue = pixelCount; // Default Number of Pixels counted true before classifying (Customizable)

		// Loops Through Area for Confirmation:
		mainLoop: while (turnsWidth < detectionArray.length) {
			// Loop Variable:
			int turnsHeight = 0;
			secondLoop: while (turnsHeight < detectionArray[turnsWidth].length) {
				// Checks the Case:
				if (detectionArray[turnsWidth][turnsHeight] == 1) {
					// Adds to the TRUE pixels:
					trueCount++;
				}

				turnsHeight++;
			}

			turnsWidth++;
		}

		// Checks the Case:
		if (trueCount >= countValue) {
			// Sets the Value:
			valuePresent = true;
		}

		// Sets the Count Value:
		booleanPixelCount = trueCount;

		// Returns Value:
		return valuePresent;
	}

	// Gets Selected Area Blobs (IF PRESENT):
	public static int getBodyBlobs(int detectionArray[][], int distance) throws Exception {
		// Main Blob Count:
		int blobCount = 0;

		// Loop Variables:
		int turnsWidth = 0;
		int currentPixelCount = 0;
		boolean firstPixelFound = false;

		// Blob Lists:
		ArrayList<Integer> blobX = new ArrayList<Integer>();
		ArrayList<Integer> blobY = new ArrayList<Integer>();
		ArrayList<Integer> blobPixels = new ArrayList<Integer>();
		ArrayList<Integer> blobLastX = new ArrayList<Integer>();
		ArrayList<Integer> blobLastY = new ArrayList<Integer>();

		// Last Pixels:
		int lastPixelX = 0, lastPixelY = 0;

		// Loops through Array:
		mainLoop: while (turnsWidth < detectionArray.length) {
			// Loop Variable:
			int turnsHeight = 0;
			secondLoop: while (turnsHeight < detectionArray[0].length) {
				// Checks the Case:
				if (detectionArray[turnsWidth][turnsHeight] == 1 && !firstPixelFound) {
					// Adds to the ArrayLists:
					blobX.add(turnsWidth);
					blobY.add(turnsHeight);
					currentPixelCount++;

					// Changes First Pixel:
					firstPixelFound = true;
				}

				else if (detectionArray[turnsWidth][turnsHeight] == 1 && firstPixelFound) {
					// Loop Variables:
					int turnsBlob = 0, blobGroup = -1, prevDistance = -1;

					// Loops through ArrayLists:
					blobLoop: while (turnsBlob < blobX.size()) {
						// Gets the Distance:
						int distanceValue = (int) (Math
								.sqrt((((turnsHeight - blobY.get(turnsBlob)) * (turnsHeight - blobY.get(turnsBlob)))
										+ ((turnsWidth - blobX.get(turnsBlob)) * (turnsWidth - blobX.get(turnsBlob))))));

						// Checks Prev Distance Case:
						if (prevDistance == -1) {
							// Sets Prev Distance:
							prevDistance = distanceValue;
						}

						// Checks Distance Case:
						if (distanceValue <= distance && distanceValue <= prevDistance) {
							// Sets the Blob Group:
							blobGroup = turnsBlob;
						}

						turnsBlob++;
					}

					// Checks the Case:
					if (blobGroup == -1) {
						// Adds to the Blob ArrayLists:
						blobX.add(turnsWidth);
						blobY.add(turnsHeight);
						blobPixels.add(currentPixelCount);
						blobLastX.add(lastPixelX);
						blobLastY.add(lastPixelY);
						currentPixelCount = 0;
					}

					else {
						// Adds to the Current Pixel Count:
						currentPixelCount++;
					}
				}

				// Checks the Case:
				if (detectionArray[turnsWidth][turnsHeight] == 1) {
					// Sets the Last Pixel:
					lastPixelX = turnsWidth;
					lastPixelY = turnsHeight;
				}

				turnsHeight++;
			}

			turnsWidth++;
		}

		// Sets the Blob Counts:
		blobCount = blobX.size();
		pixelCounts = blobPixels;
		x = blobX;
		y = blobY;

		// Returns the Count:
		return blobCount;
	}

	/* UTILITY METHODS */

	// Get Pixel Counts Method:
	public static ArrayList<Integer> getBlobPixelCounts() throws Exception {
		// Returns the Pixel Counts:
		return pixelCounts;
	}

	// Get Boolean Pixel Count Method:
	public static int getBooleanPixelCount() throws Exception {
		// Returns the Boolean Pixel Count:
		return booleanPixelCount;
	}

	// Get the Last Blob X Method:
	public static ArrayList<Integer> getLastBlobX() throws Exception {
		// Returns the Last Blob X:
		return lastX;
	}

	// Get the Last Blob Y Method:
	public static ArrayList<Integer> getLastBlobY() throws Exception {
		// Returns the Last Blob Y:
		return lastY;
	}

	// Get Blob X Method:
	public static ArrayList<Integer> getBlobX() throws Exception {
		// Returns the Blob X:
		return x;
	}

	// Get Blob Y Method:
	public static ArrayList<Integer> getBlobY() throws Exception {
		// Returns the Blob Y:
		return y;
	}
}