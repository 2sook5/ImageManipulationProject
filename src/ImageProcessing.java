import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * Purpose of this project is to create an application which is able to modify
 * images as well as create new images using 2D Arrays.
 * 
 * @author Issac Lopez
 */

public class ImageProcessing {

    /**
     * Main method can load image data into a 2D array of ints using the imgToTwoD()
     * method.
     * 
     * @param args
     */

    public static void main(String[] args) {
        // Load image using URL.
        int[][] imageData = imgToTwoD(
                "https://content.codecademy.com/projects/project_thumbnails/phaser/bug-dodger.png");

        // viewImageData(imageData);

        assert imageData != null;
        // Call methods by passing in the loaded image data.
        int[][] trimmed = trimBorders(imageData, 60);
        int[][] negative = negativeColor(imageData);
        int[][] stretchedHImg = stretchHorizontally(imageData);
        int[][] shrankVImg = shrinkVertically(imageData);
        int[][] invertedImg = invertImage(imageData);
        int[][] coloredImg = colorFilter(imageData, -75, 30, -30);

        // Storing the returned modified image data into a new 2D array of ints.
        twoDToImage(trimmed, "./trimmed_apple.jpg");
        twoDToImage(negative, "./negative_apple.jpg");
        twoDToImage(stretchedHImg, "./stretched_apple.jpg");
        twoDToImage(shrankVImg, "./shrank_apple.jpg");
        twoDToImage(invertedImg, "./inverted_apple.jpg");
        twoDToImage(coloredImg, "./colored_apple.jpg");

        // int[][] allFilters =
        // stretchHorizontally(shrinkVertically(colorFilter(negativeColor(trimBorders(invertImage(imageData),
        // 50)), 200, 20, 40)));

        // Painting with pixels.
        int[] rgba = { 255, 255, 0, 255 };
        int[][] blankImg = new int[500][500];
        int[][] randomImg = paintRandomImage(blankImg);
        int[][] rectangleImg = paintRectangle(randomImg, 200, 200, 100, 100, getColorIntValFromRGBA(rgba));
        int[][] generatedRectangles = generateRectangles(randomImg, 1000);

        twoDToImage(randomImg, "./random_img.jpg");
        twoDToImage(rectangleImg, "./rectangle.jpg");
        twoDToImage(generatedRectangles, "./generated_rect.jpg");
    }

    // ***************************************************************************************************

    // Image Processing Methods

    /**
     * This method accepts a 2D array of int pixel data and the number of
     * pixels to trim off of the borders of the image.
     * 
     * @param imageTwoD  given 2D array of integers.
     * @param pixelCount integer counter.
     * @return modified image of 2D array.
     */

    public static int[][] trimBorders(int[][] imageTwoD, int pixelCount) {
        if (imageTwoD.length > pixelCount * 2 && imageTwoD[0].length > pixelCount * 2) {
            int[][] trimmedImg = new int[imageTwoD.length - pixelCount * 2][imageTwoD[0].length - pixelCount * 2];
            for (int i = 0; i < trimmedImg.length; i++) {
                System.arraycopy(imageTwoD[i + pixelCount], pixelCount, trimmedImg[i], 0, trimmedImg[i].length);
            }
            return trimmedImg;
        } else {
            System.out.println("Cannot trim that many pixels from the given image.");
            return imageTwoD;
        }
    }

    /**
     * This method will replace the color of each pixel in the image with the
     * negative version of the pixel.
     * 
     * @param imageTwoD given 2D array of integers.
     * @return manipulated image.
     */

    public static int[][] negativeColor(int[][] imageTwoD) {
        // Create new 2D array of ints to hold the modified image data.
        int[][] manipulatedImg = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
                // Sets first three elements to 255 minus itself.
                rgba[0] = 255 - rgba[0];
                rgba[1] = 255 - rgba[1];
                rgba[2] = 255 - rgba[2];
                // Gets hexidecimal pixel data from the RGBA array.
                // Stores value in the new image created.
                manipulatedImg[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return manipulatedImg;
    }

    /**
     * This method will double the width of the provided image data.
     * For every pixel in the original image, you will copy it and place two
     * duplicate pixels side-by-side into the new modified image.
     * 
     * @param imageTwoD given 2D array of integers.
     * @return modified image.
     */

    public static int[][] stretchHorizontally(int[][] imageTwoD) {
        // Create new 2D array of ints to hold the modified image data.
        int[][] manipulatedImg = new int[imageTwoD.length][imageTwoD[0].length * 2];
        int k = 0; // Keeps track of current position for modified image.
        // Iterate through every pixel in the input image using row-major order.
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                k = j * 2; // Column index iterator.
                manipulatedImg[i][k] = imageTwoD[i][j]; // Copy current pixel position.
                manipulatedImg[i][k + 1] = imageTwoD[i][j]; // Copy current pixel from the input image.
            }
        }
        return manipulatedImg;
    }

    /**
     * This method will be halfing the height of the image and selecting every other
     * pixel down each column to place in the modified image.
     * 
     * @param imageTwoD given 2D array of integers.
     * @return modified image.
     */

    public static int[][] shrinkVertically(int[][] imageTwoD) {
        // Create new 2D array of ints having half the number of rows of input image
        // and the same number of columns.
        int[][] manipulatedImg = new int[imageTwoD.length / 2][imageTwoD[0].length];
        // Iterate through every pixel in the input image using column-major order.
        for (int i = 0; i < imageTwoD[0].length; i++) {
            for (int j = 0; j < imageTwoD.length - 1; j += 2) {
                manipulatedImg[j / 2][i] = imageTwoD[j][i];
            }
        }
        return manipulatedImg;
    }

    /**
     * This method inverts the input image by flipping the image vertically and
     * horizontally.
     * 
     * @param imageTwoD given 2D array of integers.
     * @return modified image.
     */

    public static int[][] invertImage(int[][] imageTwoD) {
        // Create new 2D array with same size of input image.
        int[][] invertedImg = new int[imageTwoD.length][imageTwoD[0].length];
        // Iterate through each pixel in the input image.
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                // Similar logic to negativeColor() but take negative of pixel positions
                // instead of the color values.
                invertedImg[i][j] = imageTwoD[(imageTwoD.length - 1) - i][(imageTwoD[i].length - 1) - j];
            }
        }
        return invertedImg;
    }

    /**
     * This method modifies every pixel in the image by provided R, G, and B values
     * as input parameters. You must make sure that each color value does not leave
     * the range of 0-255.
     * 
     * @param imageTwoD        given 2D array of integers.
     * @param redChangeValue   how much to change the red value by.
     * @param greenChangeValue how much to change the green value by.
     * @param blueChangeValue  how much to change the blue value by.
     * @return modified image.
     */

    public static int[][] colorFilter(int[][] imageTwoD, int redChangeValue, int greenChangeValue,
            int blueChangeValue) {
        // Create new 2D array with same size of input image.
        int[][] manipulatedImg = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                // Extract RGBA color values.
                int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);

                // Store calues of each color plus the modifier value.
                int newRed = rgba[0] + redChangeValue;
                int newGreen = rgba[1] + greenChangeValue;
                int newBlue = rgba[2] + blueChangeValue;

                // Makes sure that new color values do not go out of range (0-255).
                if (newRed > 255 || newGreen > 255 || newBlue > 255) {
                    newRed = 255;
                    newGreen = 255;
                    newBlue = 255;
                } else if (newRed < 0 || newGreen < 0 || newBlue < 0) {
                    newRed = 0;
                    newGreen = 0;
                    newBlue = 0;

                }

                // Set values in RGBA array to new color values calculated.
                rgba[0] = newRed;
                rgba[1] = newGreen;
                rgba[2] = newBlue;

                // Convert RGBA array to sinle int containing hexadecimal pixel data
                // using provided method and store it in the new image.
                manipulatedImg[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return manipulatedImg;
    }

    // ***************************************************************************************************

    // Painting Methods

    /**
     * This method will modify the image passed in by replacing every pixel with a
     * randomly colored pixel.
     * 
     * @param canvas 2D array of integers.
     * @return modified image.
     */

    public static int[][] paintRandomImage(int[][] canvas) {
        Random rand = new Random();
        // Iterate through each pixel in provided image.
        for (int i = 0; i < canvas.length; i++) {
            for (int j = 0; j < canvas[i].length; j++) {
                // Generates a randomly colored pixel.
                int[] rgba = { rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), 255 };
                canvas[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return canvas;
    }

    /**
     * This method draws a rectangle on an image with the below parameters.
     *
     * @param canvas      2D array of integers.
     * @param w           width of the rectangle.
     * @param h           height of the rectangle.
     * @param rowPosition from rectangle to the row edge of image.
     * @param colPosition from rectangle to the column edge of image.
     * @param color       retangles color.
     * @return modified image.
     */

    public static int[][] paintRectangle(int[][] canvas, int w, int h, int rowPosition, int colPosition,
            int color) {
        // Iterate through each pixel in input image.
        for (int i = 0; i < canvas.length; i++) {
            for (int j = 0; j < canvas[i].length; j++) {
                if (i >= rowPosition && i <= rowPosition + w && j >= colPosition && j <= colPosition + h) {
                    canvas[i][j] = color;
                }
            }
        }
        return canvas;
    }

    /**
     * This method will generate ranomly positioned, sized, and colored rectangles
     * based on the provided random number.
     * 
     * @param canvas
     * @param numRectangles deteremines how many randomly generated rectangles will
     *                      be placed in the image.
     * @return modified image.
     */

    public static int[][] generateRectangles(int[][] canvas, int numRectangles) {
        Random rand = new Random();
        // Iterate for the number of rectangles provided.
        for (int i = 0; i < numRectangles; i++) {
            int randomWidth = rand.nextInt(canvas[0].length);
            int randomHeight = rand.nextInt(canvas.length);
            int randomRowPos = rand.nextInt(canvas.length - randomHeight);
            int randomColPos = rand.nextInt(canvas[0].length - randomWidth);
            int[] rgba = { rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), 255 };
            int randomColor = getColorIntValFromRGBA(rgba);
            paintRectangle(canvas, randomWidth, randomHeight, randomRowPos, randomColPos, randomColor);
        }
        return canvas;
    }

    // ***************************************************************************************************

    // Utility Methods

    /**
     * This method accepts a String which can be a file path or image URL. It
     * returns a 2D array of integers that contains every pixel from the image
     * stored as int hexadecimal values containing the RGBA values for the pixel.
     * 
     * @param input a file path or an image URL.
     * @return pixel data into an image and saves it.
     */

    public static int[][] imgToTwoD(String input) {
        try {
            BufferedImage image = null;
            if (input.substring(0, 4).toLowerCase().equals("http")) {
                URL imageUrl = new URL(input);
                image = ImageIO.read(imageUrl);
                if (image == null) {
                    System.out.println("Failed to get image from provided URL.");
                }
            } else {
                image = ImageIO.read(new File(input));
            }

            assert image != null;
            int imgRows = image.getHeight();
            int imgCols = image.getWidth();
            int[][] pixelData = new int[imgRows][imgCols];

            for (int i = 0; i < imgRows; i++) {
                for (int j = 0; j < imgCols; j++) {
                    pixelData[i][j] = image.getRGB(j, i);
                }
            }
            return pixelData;
        } catch (Exception e) {
            System.out.println("Failed to load image: " + e.getLocalizedMessage());
            return new int[0][0];
        }
    }

    /**
     * This method accepts a 2D array of integers and a String for the file name.
     * Converts the 2D array of int pixel data into an image and saves it.
     * 
     * @param imgData  2D array of integers for the image data.
     * @param fileName Name of the file that will be used.
     */

    public static void twoDToImage(int[][] imgData, String fileName) {
        try {
            int imgRows = imgData.length;
            int imgCols = imgData[0].length;
            BufferedImage result = new BufferedImage(imgCols, imgRows, BufferedImage.TYPE_INT_RGB);

            for (int i = 0; i < imgRows; i++) {
                for (int j = 0; j < imgCols; j++) {
                    result.setRGB(j, i, imgData[i][j]);
                }
            }

            File output = new File(fileName);
            ImageIO.write(result, "jpg", output);
        } catch (Exception e) {
            System.out.println("Failed to save image: " + e.getLocalizedMessage());
        }
    }

    /**
     * This method accepts an int value representing the pixel hexadecimal value.
     * Returns a four element int array consisiting of R, G, B, A values; being
     * between 0 and 255.
     * Used to extract the color components from the hexadecimal value for the
     * pixel.
     * 
     * @param pixelColorValue value of each color pixel.
     * @return a four element array consisiting of R, G, B, A values.
     */

    public static int[] getRGBAFromPixel(int pixelColorValue) {
        Color pixelColor = new Color(pixelColorValue);
        return new int[] {
                pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), pixelColor.getAlpha()
        };
    }

    /**
     * This method accepts an array of integers that represent the RGBA values and
     * converts it into a single int value representing the pixel hexidecimal value.
     * 
     * @param colorData array of integers which represent RGBA values.
     * @return incorrect number of integers.
     */

    public static int getColorIntValFromRGBA(int[] colorData) {
        if (colorData.length == 4) {
            Color color = new Color(colorData[0], colorData[1], colorData[2], colorData[3]);
            return color.getRGB();
        } else {
            System.out.println("Incorrect number of elements in RGBA array.");
            return -1;
        }
    }

    /**
     * This method is used to view the structure of the image data in both rax pixel
     * form and the extracted RGBA form.
     * 
     * Call to viewImageData(imageData); in main method below:
     * 
     * Raw pixel data from the top left corner:
     * [[-4592897, -4592897, -4592897],
     * [-4592897, -4592897, -4592897],
     * [-4592897, -4592897, -4592897]]
     * 
     * Extracted RGBA pixel data from top the left corner:
     * [[185, 234, 255, 255], [185, 234, 255, 255], [185, 234, 255, 255]]
     * [[185, 234, 255, 255], [185, 234, 255, 255], [185, 234, 255, 255]]
     * [[185, 234, 255, 255], [185, 234, 255, 255], [185, 234, 255, 255]]
     * 
     * @param imageTwoD 2D array of integers extracting a 3x3 section from bottom to
     *                  top left of the image.
     */

    public static void viewImageData(int[][] imageTwoD) {
        if (imageTwoD.length > 3 && imageTwoD[0].length > 3) {
            int[][] rawPixels = new int[3][3];
            for (int i = 0; i < 3; i++) {
                System.arraycopy(imageTwoD[i], 0, rawPixels[i], 0, 3);
            }
            System.out.println("Raw pixel data from the top left corner:");
            System.out.print(Arrays.deepToString(rawPixels).replace("],", "],\n") + "\n");

            int[][][] rgbPixels = new int[3][3][4];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    rgbPixels[i][j] = getRGBAFromPixel(imageTwoD[i][j]);
                }
            }
            System.out.println();
            System.out.println("Extracted RGBA pixel data from top the left corner:");

            for (int[][] row : rgbPixels) {
                System.out.print(Arrays.deepToString(row) + System.lineSeparator());
            }
        } else {
            System.out.println("The image is not large enough to extract 9 pixels from the top left corner");
        }
    }
}
