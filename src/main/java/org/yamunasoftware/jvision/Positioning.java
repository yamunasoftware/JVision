package org.yamunasoftware.jvision;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class Positioning extends Analyze {
  /* VISION POSITIONING VARIABLES */

  // Positioning Data (w/ Defaults):
  private static int centerPixel[] = new int[2];
  private static int frameWidth = 0, frameHeight = 0;
  private static double cameraOffsetHorizontal = 0, cameraOffsetVertical = 0;
  private static double yRatio = 0, xRatio = 0;

  // Vision Positioning Output Variables (w/ Defaults):
  private static ArrayList<Double> offset = new ArrayList<Double>();
  private static ArrayList<Double> distance = new ArrayList<Double>();

  /* VISION POSITIONING SETUP METHODS */

  // Constructor:
  public Positioning() {
    super();
  }

  // Initialize Vision Positioning:
  public static void initVisionPosition(int width, int height, double distanceRatio,
      double offsetRatio, double camOffsetX, double camOffsetY) throws Exception {
    // Sets Ratios:
    yRatio = distanceRatio;
    xRatio = offsetRatio;

    // Sets Offsets:
    cameraOffsetHorizontal = camOffsetX;
    cameraOffsetVertical = camOffsetY;

    // Sets Frame Data:
    frameWidth = width;
    frameHeight = height;

    // Pixel Settings:
    centerPixel[0] = (frameWidth / 2);
    centerPixel[1] = frameHeight;
  }

  /* VISION POSITIONING METHODS */

  // Vision Positioning Method:
  public static void getVisionPosition(int x[], int y[], int lastX[], int lastY[]) throws Exception {
    // Checks the Case:
    if (x.length == y.length) {
      // Loop Variables:
      int turns = 0;

      // Loops through Arrays:
      mainLoop: while (turns < x.length) {
        // Gets and Sets Values:
        double pointPosition[] = getPointPosition(x[turns], y[turns], lastX[turns], lastY[turns]);
        distance.add(pointPosition[0]);
        offset.add(pointPosition[1]);

        turns++;
      }
    }

    else {
      // Error Debugs:
      System.err.println("Invalid Array Lengths!");
    }
  }

  // Point Positioning Method:
  public static double[] getPointPosition(int x, int y, int lastX, int lastY) throws Exception {
    // Distance Calculation and Return Array:
    double returnArray[] = new double[2];
    double distance = getDistance(x, y, lastX, lastY);

    // Gets the Midpoint Calculation:
    int mid[] = getMidpoint(x, y, lastX, lastY);
    double midDist = getDistance(mid[0], mid[1], centerPixel[0], centerPixel[1]);

    // Gets the Output Values:
    double realDistance = ((distance * yRatio) + cameraOffsetVertical);
    double realOffset = (midDist * xRatio);

    // Checks the Case:
    if (x > centerPixel[0]) {
      // Sets the Offset:
      realOffset -= cameraOffsetHorizontal;
    }

    else {
      // Sets the Offset:
      realOffset += cameraOffsetHorizontal;
    }

    // Sets Array:
    returnArray[0] = realDistance;
    returnArray[1] = realOffset;

    // Returns Array:
    return returnArray;
  }

  /* VISION POSITION CALCULATION METHODS */

  // Vision Offset Correction Method:
  public static double getVisionOffsetCorrection(double offset) throws Exception {
    // Checks the Case:
    if (offset > cameraOffsetHorizontal) {
      // Returns the New Value:
      return -offset;
    }

    else {
      // Returns the New Value:
      return offset;
    }
  }

  // Distance Method:
  public static double getDistance(int x, int y, int endX, int endY) throws Exception {
    // Gets the Distance:
    double xDiff = ((endX - x) * (endX - x));
    double yDiff = ((endY - y) * (endY - y));
    double sum = (xDiff + yDiff);
    double distance = Math.sqrt(sum);

    // Returns the Distance:
    return distance;
  }

  // Midpoint Method:
  public static int[] getMidpoint(int x, int y, int endX, int endY) throws Exception {
    // Calculates the Coordinates:
    int xCoordinate = ((x + endX) / 2);
    int yCoordinate = ((y + endY) / 2);
    int coordinates[] = { xCoordinate, yCoordinate };

    // Returns the Coordinate:
    return coordinates;
  }

  /* VISION POSITION UTILITY METHODS */

  // Get Alignment X Distance Method:
  public static ArrayList<Double> getOffset() throws Exception {
    // Returns the X Alignments:
    return offset;
  }

  // Get Distance Method:
  public static ArrayList<Double> getDistance() throws Exception {
    // Returns the Distances:
    return distance;
  }
}