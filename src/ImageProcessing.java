import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * Purpose of this project is to create an application which is able to modify
 * images
 * as well as create new images using 2D Arrays.
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
        // load image using URL.
        int[][] imageData = imgToTwoD(
                "https://content.codecademy.com/projects/project_thumbnails/phaser/bug-dodger.png");

        // viewImageData(imageData);

        assert imageData != null;
        int[][] trimmed = trimBorders(imageData, 60);
        int[][] negative = negativeColor(imageData);
        int[][] stretchedHImg = stretchHorizontally(imageData);
        int[][] shrankVImg = shrinkVertically(imageData);
        int[][] invertedImg = invertImage(imageData);
        int[][] coloredImg = colorFilter(imageData, -75, 30, -30);

        twoDToImage(trimmed, "./trimmed_apple.jpg");
        twoDToImage(negative, "./negative_apple.jpg");
        twoDToImage(stretchedHImg, "./stretched_apple.jpg");
        twoDToImage(shrankVImg, "./shrank_apple.jpg");
        twoDToImage(invertedImg, "./inverted_apple.jpg");
        twoDToImage(coloredImg, "./colored_apple.jpg");

        // int[][] allFilters =
        // stretchHorizontally(shrinkVertically(colorFilter(negativeColor(trimBorders(invertImage(imageData),
        // 50)), 200, 20, 40)));

        // Painting with pixels

        int[][] blankImg = new int[500][500];
        int[][] randomImg = paintRandomImage(blankImg);
        int[] rgba = { 255, 255, 0, 255 };
        int[][] rectangleImg = paintRectangle(randomImg, 200, 200, 100, 100, getColorIntValFromRGBA(rgba));
        int[][] generatedRectangles = generateRectangles(randomImg, 1000);

        twoDToImage(randomImg, "./random_img.jpg");
        twoDToImage(rectangleImg, "./rectangle.jpg");
        twoDToImage(generatedRectangles, "./generated_rect.jpg");
    }

    // Image Processing Methods

    public static int[][] trimBorders(int[][] imageTwoD, int pixelCount) {
        // Example Method
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

    public static int[][] negativeColor(int[][] imageTwoD) {
        // TODO: Fill in the code for this method
        int[][] manipulatedImg = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
                rgba[0] = 255 - rgba[0];
                rgba[1] = 255 - rgba[1];
                rgba[2] = 255 - rgba[2];
                manipulatedImg[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return manipulatedImg;
    }

    public static int[][] stretchHorizontally(int[][] imageTwoD) {
        // TODO: Fill in the code for this method
        int[][] manipulatedImg = new int[imageTwoD.length][imageTwoD[0].length * 2];
        int it = 0;
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                it = j * 2;
                manipulatedImg[i][it] = imageTwoD[i][j];
                manipulatedImg[i][it + 1] = imageTwoD[i][j];
            }
        }
        return manipulatedImg;
    }

    public static int[][] shrinkVertically(int[][] imageTwoD) {
        // TODO: Fill in the code for this method
        int[][] manipulatedImg = new int[imageTwoD.length / 2][imageTwoD[0].length];
        int it = 0;
        for (int i = 0; i < imageTwoD[0].length; i++) {
            for (int j = 0; j < imageTwoD.length - 1; j += 2) {
                manipulatedImg[j / 2][i] = imageTwoD[j][i];
            }
        }
        return manipulatedImg;
    }

    public static int[][] invertImage(int[][] imageTwoD) {
        // TODO: Fill in the code for this method
        int[][] invertedImg = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                invertedImg[i][j] = imageTwoD[(imageTwoD.length - 1) - i][(imageTwoD[i].length - 1) - j];
            }
        }
        return invertedImg;
    }

    public static int[][] colorFilter(int[][] imageTwoD, int redChangeValue, int greenChangeValue,
            int blueChangeValue) {
        // TODO: Fill in the code for this method
        int[][] manipulatedImg = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);

                int newRed = rgba[0] + redChangeValue;
                int newGreen = rgba[1] + greenChangeValue;
                int newBlue = rgba[2] + blueChangeValue;

                if (newRed > 255) {
                    newRed = 255;
                } else if (newRed < 0) {
                    newRed = 0;
                }

                if (newGreen > 255) {
                    newGreen = 255;
                } else if (newGreen < 0) {
                    newGreen = 0;
                }

                if (newBlue > 255) {
                    newBlue = 255;
                } else if (newBlue < 0) {
                    newBlue = 0;
                }

                rgba[0] = newRed;
                rgba[1] = newGreen;
                rgba[2] = newBlue;

                manipulatedImg[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return manipulatedImg;
    }

    // Painting Methods

    public static int[][] paintRandomImage(int[][] canvas) {
        // TODO: Fill in the code for this method
        Random rand = new Random();
        for (int i = 0; i < canvas.length; i++) {
            for (int j = 0; j < canvas[i].length; j++) {
                int[] rgba = { rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), 255 };
                canvas[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return canvas;
    }

    public static int[][] paintRectangle(int[][] canvas, int width, int height, int rowPosition, int colPosition,
            int color) {
        // TODO: Fill in the code for this method
        for (int i = 0; i < canvas.length; i++) {
            for (int j = 0; j < canvas[i].length; j++) {
                if (i >= rowPosition && i <= rowPosition + width) {
                    if (j >= colPosition && j <= colPosition + height) {
                        canvas[i][j] = color;
                    }
                }
            }
        }
        return canvas;
    }

    public static int[][] generateRectangles(int[][] canvas, int numRectangles) {
        // TODO: Fill in the code for this method
        Random rand = new Random();
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
     * This method accepts a 2D array of integers and a String for the file name.
     * It converts the 2D array of int pixel data into an image and saves it.
     * 
     * @param input the input being a file path or an image URL.
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
            return null;
        }
    }

    /**
     * This method
     * 
     * @param imgData
     * @param fileName
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
     * @param imageTwoD 2D array of integers extracting a 3x3 section from bottom to
     *                  top left of the image.
     */

    public static void viewImageData(int[][] imageTwoD) {
        if (imageTwoD.length > 3 && imageTwoD[0].length > 3) {
            int[][] rawPixels = new int[3][3];
            for (int i = 0; i < 3; i++) {
                System.arraycopy(imageTwoD[i], 0, rawPixels[i], 0, 3);
            }
            System.out.println("Raw pixel data from the top left corner.");
            System.out.print(Arrays.deepToString(rawPixels).replace("],", "],\n") + "\n");

            int[][][] rgbPixels = new int[3][3][4];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    rgbPixels[i][j] = getRGBAFromPixel(imageTwoD[i][j]);
                }
            }
            System.out.println();
            System.out.println("Extracted RGBA pixel data from top the left corner.");

            for (int[][] row : rgbPixels) {
                System.out.print(Arrays.deepToString(row) + System.lineSeparator());
            }
        } else {
            System.out.println("The image is not large enough to extract 9 pixels from the top left corner");
        }
    }
}
