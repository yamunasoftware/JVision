package lib;

import java.util.ArrayList;
import java.awt.image.BufferedImage;

@SuppressWarnings("unused")
public class Tests {
  /* TEST METHODS */

  // Vision Positioning Test Method:
  public static ArrayList<ArrayList<Double>> positioningTest(int width, int height, 
    double distanceRatio, double offsetRatio, double camOffsetX, double camOffsetY, int x[], 
    int y[], int lastX[], int lastY[]) throws Exception {
    // Gets the Vision Positioning:
    Positioning.initVisionPosition(width, height, distanceRatio, offsetRatio, camOffsetX, camOffsetY);
    Positioning.getVisionPosition(x, y, lastX, lastY);

    // Gets the Test Values:
    ArrayList<Double> offsets = Positioning.getOffset();
    ArrayList<Double> distances = Positioning.getDistance();
    
    // Sets the Test Values:
    ArrayList<ArrayList<Double>> returnList = new ArrayList<ArrayList<Double>>();
    returnList.add(offsets);
    returnList.add(distances);

    // Returns the Test Values:
    return returnList;
  }

  // Lighting Test Method:
  public static ArrayList<boolean[]> lightingTest(String filePaths[], String type, double upper, double lower) 
    throws Exception {
    // Returns the Test Value:
    return Lighting.getLightingThresholds(filePaths, type, upper, lower);
  }

  // Image Correction Test Method:
  public static int[][] correctionTest(int rgbValues[][], int redDiff, int greenDiff, int blueDiff) 
    throws Exception {
    // Tests the Correction:
    ImageCorrection.setCorrection(redDiff, greenDiff, blueDiff);
    return ImageCorrection.correctNoise(rgbValues);
  }

  // Image Processing Test Method:
  public static BufferedImage procesingTest(int rgbValues[][], int kernel[][], 
    ImageProcessing.CONV_TYPE type) throws Exception {
    // Tests the Processing:
    int grayscale[][] = ImageProcessing.applyFilter(rgbValues, kernel, type);
    return ImageProcessing.visualizeFilter(grayscale);
  } 
}