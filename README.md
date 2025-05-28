# JVision

Java Computer Vision Library for Base Vision Tasks

## Overview

JVision is a Java library for accomplishing simple but powerful computer vision tasks. When combined with another library, this library could be more powerful than it already is. Some of the possible tasks that this library can perform include: blob detection, color detection, lighting correction, noise correction, color separation, filters and blurs, convolutions, 3D mapping.

The main functionality of the library is located in the "Analyze.java" class, which is a child of the "Camera.java" class which is a collection of important image manipulation methods (using the BufferedImage data structure). The "Analyze" class contains all of the main image processing methods such as color detection, blob detection, and color separation. The 3D mapping or positioning methods are contained inside of the "Positioning.java" class. Furthermore, the lighting correction and detection methods are stored in the "Lighting.java" class.

The noise correction and filters are located in the "ImageCorrection.java" class, which is also connected to the convolutions methods. The blurring and convolutions methods are located in the "ImageProcessing.java" class, which is useful for Gaussian blurring, machine learning, edge detection, and color separation. This class is the pride of our library and this class allows for most of the powerful image processing technology that our library provides. Finally, we have a series of tests located in the "Tests.java" class, which provides a series of methods to test the validity and functionality of your build for this library.

## Installation

The installation for this library is listed below either using the Maven or Gradle build tools. Both of these tools are ubiquitous in the Jaa build environment world. The other option for using this library in your code is to use this as a module or use the included jar file found in the repository. This can be used locally, but updating to newer versions is much harder in build environments without using the Maven or Gradle implementation. Lastly, it is important to us that you pay attention to the version number when building your project with our library.

Maven:

```XML
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>com.github.yamunasoftware</groupId>
    <artifactId>JVision</artifactId>
    <version>LATEST-VERSION/version>
  </dependency>
</dependencies>
```

Gradle:

```Java
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}

dependencies {
  implementation 'com.github.yamunasoftware:JVision:LATEST-VERSION'
}
```
