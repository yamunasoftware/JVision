package org.yamunasoftware.jvision;

@SuppressWarnings("unused")
public class ImageCorrection extends ImageProcessing {
  /* CORRECTION VARIABLES */

  // Main Correction Variables:
  private static int redDifferenceThreshold = 0;
  private static int blueDifferenceThreshold = 0;
  private static int greenDifferenceThreshold = 0;

  /* CORRECTION SETUP METHODS */

  // Constructor:
  public ImageCorrection() {
    super();
  }

  // Correction Setup:
  public static void setCorrection(int redDiff, int greenDiff, int blueDiff) throws Exception {
    // Sets the Difference Threshold:
    redDifferenceThreshold = redDiff;
    greenDifferenceThreshold = greenDiff;
    blueDifferenceThreshold = blueDiff;
  }

  /* CORRECTION NOISE METHODS */

  // Noise Correction Method:
  public static int[][] correctNoise(int rgbValues[][]) throws Exception {
    // Loop Variables:
    int turnsWidth = 0;
    int rgb[][] = rgbValues;

    // Loops through Array:
    mainLoop: while (turnsWidth < rgb.length) {
      // Loop Variable:
      int turnsHeight = 0;

      // Loop through Array:
      secondLoop: while (turnsHeight < rgb[0].length) {
        // Local Average Variables:
        double redLocalAverage = 0;
        double greenLocalAverage = 0;
        double blueLocalAverage = 0;

        // Local Usage Variables:
        double count = 0;
        int localRGBValue[] = convertRGB(rgb[turnsWidth][turnsHeight]);

        // Gets the Usable Pixel Value:
        int coordinate[] = { turnsWidth, turnsHeight };
        int usableValues[] = checkSurroundings(rgb, coordinate);

        // Checks the Case:
        if (usableValues[0] == 1) {
          // Adds to the Average:
          int localRGB[] = convertRGB(rgb[turnsWidth - 1][turnsHeight - 1]);
          redLocalAverage += localRGB[0];
          greenLocalAverage += localRGB[1];
          blueLocalAverage += localRGB[2];
          count++;
        }

        // Checks the Case:
        if (usableValues[1] == 1) {
          // Adds to the Average:
          int localRGB[] = convertRGB(rgb[turnsWidth][turnsHeight - 1]);
          redLocalAverage += localRGB[0];
          greenLocalAverage += localRGB[1];
          blueLocalAverage += localRGB[2];
          count++;
        }

        // Checks the Case:
        if (usableValues[2] == 1) {
          // Adds to the Average:
          int localRGB[] = convertRGB(rgb[turnsWidth + 1][turnsHeight - 1]);
          redLocalAverage += localRGB[0];
          greenLocalAverage += localRGB[1];
          blueLocalAverage += localRGB[2];
          count++;
        }

        // Checks the Case:
        if (usableValues[3] == 1) {
          // Adds to the Average:
          int localRGB[] = convertRGB(rgb[turnsWidth - 1][turnsHeight]);
          redLocalAverage += localRGB[0];
          greenLocalAverage += localRGB[1];
          blueLocalAverage += localRGB[2];
          count++;
        }

        // Checks the Case:
        if (usableValues[4] == 1) {
          // Adds to the Average:
          int localRGB[] = convertRGB(rgb[turnsWidth + 1][turnsHeight]);
          redLocalAverage += localRGB[0];
          greenLocalAverage += localRGB[1];
          blueLocalAverage += localRGB[2];
          count++;
        }

        // Checks the Case:
        if (usableValues[5] == 1) {
          // Adds to the Average:
          int localRGB[] = convertRGB(rgb[turnsWidth - 1][turnsHeight + 1]);
          redLocalAverage += localRGB[0];
          greenLocalAverage += localRGB[1];
          blueLocalAverage += localRGB[2];
          count++;
        }

        // Checks the Case:
        if (usableValues[6] == 1) {
          // Adds to the Average:
          int localRGB[] = convertRGB(rgb[turnsWidth][turnsHeight + 1]);
          redLocalAverage += localRGB[0];
          greenLocalAverage += localRGB[1];
          blueLocalAverage += localRGB[2];
          count++;
        }

        // Checks the Case:
        if (usableValues[7] == 1) {
          // Adds to the Average:
          int localRGB[] = convertRGB(rgb[turnsWidth + 1][turnsHeight + 1]);
          redLocalAverage += localRGB[0];
          greenLocalAverage += localRGB[1];
          blueLocalAverage += localRGB[2];
          count++;
        }

        // Computes the Averages:
        redLocalAverage /= count;
        greenLocalAverage /= count;
        blueLocalAverage /= count;

        // Computes the Comparisons:
        double redDifference = Math.abs(redLocalAverage - localRGBValue[0]);
        double greenDifference = Math.abs(greenLocalAverage - localRGBValue[1]);
        double blueDifference = Math.abs(blueLocalAverage - localRGBValue[2]);

        // Checks the Case:
        if (redDifference > redDifferenceThreshold || greenDifference > greenDifferenceThreshold
            || blueDifference > blueDifferenceThreshold) {
          // Sets the Current Pixel:
          int setRGB[] = { (int) (redLocalAverage), (int) (greenLocalAverage), (int) (blueLocalAverage) };
          rgb[turnsWidth][turnsHeight] = convertSingularRGB(setRGB);
        }

        turnsHeight++;
      }

      turnsWidth++;
    }

    // Returns the RGB Values:
    return rgb;
  }

  // Checks Surrounding Pixel Ranges:
  public static int[] checkSurroundings(int rgbValues[][], int coordinate[]) throws Exception {
    // Main Information Array:
    int information[] = new int[8];

    // Gets the Pixel Coordinates:
    int TL[] = { coordinate[0] - 1, coordinate[1] - 1 };
    int T[] = { coordinate[0], coordinate[1] - 1 };
    int TR[] = { coordinate[0] + 1, coordinate[1] - 1 };
    int L[] = { coordinate[0] - 1, coordinate[1] };
    int R[] = { coordinate[0] + 1, coordinate[1] };
    int BL[] = { coordinate[0] - 1, coordinate[1] + 1 };
    int B[] = { coordinate[0], coordinate[1] + 1 };
    int BR[] = { coordinate[0] + 1, coordinate[1] + 1 };

    // Sets the Information:
    information[0] = checkCoordinate(rgbValues, TL);
    information[1] = checkCoordinate(rgbValues, T);
    information[2] = checkCoordinate(rgbValues, TR);
    information[3] = checkCoordinate(rgbValues, L);
    information[4] = checkCoordinate(rgbValues, R);
    information[5] = checkCoordinate(rgbValues, BL);
    information[6] = checkCoordinate(rgbValues, B);
    information[7] = checkCoordinate(rgbValues, BR);

    // Returns the Information:
    return information;
  }

  // Checks the Coordinates Individually:
  public static int checkCoordinate(int rgbValues[][], int coordinate[]) throws Exception {
    // Checks the Case:
    if (isValueRange(coordinate[0], 0, rgbValues.length - 1) &&
        isValueRange(coordinate[1], 0, rgbValues[0].length)) {
      // Returns the Value:
      return 1;
    }

    else {
      // Returns the Value:
      return 0;
    }
  }
}