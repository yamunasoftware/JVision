package org.yamunasoftware.jvision;

import java.awt.image.BufferedImage;

@SuppressWarnings("unused")
public class ImageProcessing extends Analyze {
	/* CONSTRUCTOR */
	public ImageProcessing() {
		super();
	}

	/* FILTER METHODS */

	// Applies the Specific Blurs for Image (PARAMS: Collected RGB Values Matrix,
	// and 3x3 Matrix of Kernels):
	public static int[][] applyFilter(int rgbValues[][], int kernel[][], CONV_TYPE type) throws Exception {
		// Main RGB Array:
		int filterRGB[][] = new int[rgbValues.length - (kernel.length - 1)][rgbValues[0].length - (kernel[0].length - 1)];

		// Checks the Case:
		if (rgbValues.length > kernel.length && rgbValues[0].length > kernel[0].length && kernel.length == 3
				&& kernel[0].length == 3) {
			// Loop Variable:
			int turnsWidth = 0;

			// Loops through Array:
			mainLoop: while (turnsWidth < filterRGB.length) {
				// Loop Variable:
				int turnsHeight = 0;
				secondLoop: while (turnsHeight < filterRGB[0].length) {
					// Sets the Filtered RGB Values:
					int TL = (Capture.getGrayscale(convertRGB(rgbValues[turnsWidth][turnsHeight])) * kernel[0][0]);
					int T = (Capture.getGrayscale(convertRGB(rgbValues[turnsWidth + 1][turnsHeight])) * kernel[1][0]);
					int TR = (Capture.getGrayscale(convertRGB(rgbValues[turnsWidth + 2][turnsHeight])) * kernel[2][0]);
					int L = (Capture.getGrayscale(convertRGB(rgbValues[turnsWidth][turnsHeight + 1])) * kernel[0][1]);
					int M = (Capture.getGrayscale(convertRGB(rgbValues[turnsWidth + 1][turnsHeight + 1])) * kernel[1][1]);
					int R = (Capture.getGrayscale(convertRGB(rgbValues[turnsWidth + 2][turnsHeight + 1])) * kernel[2][1]);
					int BL = (Capture.getGrayscale(convertRGB(rgbValues[turnsWidth][turnsHeight + 2])) * kernel[0][2]);
					int B = (Capture.getGrayscale(convertRGB(rgbValues[turnsWidth + 1][turnsHeight + 2])) * kernel[1][2]);
					int BR = (Capture.getGrayscale(convertRGB(rgbValues[turnsWidth + 2][turnsHeight + 2])) * kernel[2][2]);

					// Checks the Case:
					if (type == CONV_TYPE.AVG) {
						// Finds the Average:
						filterRGB[turnsWidth][turnsHeight] = ((TL + T + TR + L + M + R + BL + B + BR) / 9);
					}

					else if (type == CONV_TYPE.SUM) {
						// Takes the Summation of the Values:
						filterRGB[turnsWidth][turnsHeight] = (TL + T + TR + L + M + R + BL + B + BR);
					}

					turnsHeight++;
				}

				turnsWidth++;
			}
		}

		else {
			// Error Debugs:
			System.err.println("Image and Kernel Enterance Error!");
		}

		// Returns the RGB Array:
		return filterRGB;
	}

	// Visualization of the GrayScale Filter (GrayScale Only, Assumes Usage of
	// Previous Method):
	public static BufferedImage visualizeFilter(int grayscale[][]) throws Exception {
		// Main Buffered Image:
		BufferedImage newImage = new BufferedImage(grayscale.length, grayscale[0].length, BufferedImage.TYPE_BYTE_GRAY);

		// Loop Variable:
		int turnsWidth = 0;

		// Loops through Array:
		mainLoop: while (turnsWidth < grayscale.length) {
			// Loop Variable:
			int turnsHeight = 0;
			secondLoop: while (turnsHeight < grayscale[0].length) {
				// Sets the Visualization:
				int normalizedRGB = Capture.normalizeValue(grayscale[turnsWidth][turnsHeight]);

				// Sets the BufferedImage RGB:
				newImage.setRGB(turnsWidth, turnsHeight, normalizedRGB);

				turnsHeight++;
			}

			turnsWidth++;
		}

		// Returns the Visualization:
		return newImage;
	}

	/* CONVOLUTION TYPES */

	// Types: AVG and SUM
	public static enum CONV_TYPE {
		AVG,
		SUM
	}
}