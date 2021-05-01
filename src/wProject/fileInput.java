package wProject;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class fileInput {

	private final File file;

	private Scanner scanner;

	public fileInput(File file) {
		this.file = file;
	}

	// get the file
	private File getFile(File file) {

		return file;
	}

	// get the file content
	private List<String> getFileLines() {

		getFile(file);

		List<String> lines = new ArrayList<>();

		try {
			scanner = new Scanner(file);

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		}

		while (scanner.hasNextLine()) {

			String currentLine = scanner.nextLine();

			lines.add(currentLine);
		}

		return lines;
	}

	// get the resolution indexes
	private int getResolutionIndex(char index) {

		List<String> lines = getFileLines();

		int x = 0;
		int y = 0;

		if (lines.get(2) != null || lines.get(2) != "") {

			String[] lineContent = lines.get(1).split(" ");

			x = Integer.parseInt(lineContent[0]);
			y = Integer.parseInt(lineContent[1]);

		} else
			System.out.println("[CraftCN resolutionIndex] resolution out of index");

		if (index == 'x')
			return x;

		else if (index == 'y')
			return y;

		else
			return 0;
	}

	// get the Pixels lines only
	private List<String> getPixelsLines() {

		List<String> lines = getFileLines();

		lines.remove(0);
		lines.remove(0);
		lines.remove(0);

		return lines;
	}

	// get the Pixels lines into Pixel objects
	private List<Pixel> getLinesIntoPixels() {

		List<String> lines = getPixelsLines();

		List<Pixel> pixelLineContent = new ArrayList<Pixel>();

		// create and fill the pixels
		for (int i = 0; i < lines.size(); i++) {

			String[] rgb = lines.get(i).split(" ");

			// set the values of red, green and blue
			int red = Integer.parseInt(rgb[0]);
			int green = Integer.parseInt(rgb[1]);
			int blue = Integer.parseInt(rgb[2]);

			pixelLineContent.add(new Pixel(red, green, blue));
		}

		return pixelLineContent;
	}

	// create and get the image in Pixel matrix
	public Pixel[][] getPixelMatrix() {

		int x = getResolutionIndex('x');
		int y = getResolutionIndex('y');

		Pixel[][] pixelMatrix = new Pixel[y][x];

		List<Pixel> pixelLines = getLinesIntoPixels();

		for (int row = 0; row < y; row++) {
			for (int column = 0; column < x; column++) {

				pixelMatrix[row][column] = pixelLines.get(row * x + column);
				System.out.print(pixelMatrix[row][column].getRed() + " ");
			}
			System.out.println();
		}

		return pixelMatrix;
	}

	// filter to a specific scale
	private Pixel[][] gaussianBlurFilter(int scaleOne, int scaleTwo) {

		Pixel[][] pixelMatrix = getPixelMatrix();

		int x = getResolutionIndex('x');
		int y = getResolutionIndex('y');

		int newX = x;
		int rowCounter = 0;

		for (int row = 0; row < y; row++) {
			for (int column = 0; column < x; column++) {

				newX -= scaleOne;

				if (newX == x / scaleOne) {
					rowCounter++;

//					a

					if (rowCounter == 2) {

						rowCounter = 0;
						newX = x;
					}
				}

				int blurR = pixelMatrix[row][column * scaleOne].getRed();
				int blurG = pixelMatrix[row][column * scaleOne + 1].getGreen();
				int blurB = pixelMatrix[row][column * scaleOne + 2].getBlue();
			}
		}

		for (int row = 0; row < scaleOne; row++) {
			for (int column = 0; column < scaleTwo; column++) { 

			}
		}

		return null;
	}

	// Filter Sharpen (3x3)
	private Pixel[][] sharpen3x3() {

		gaussianBlurFilter(3, 3);

		return null;
	}

	// Filter Gaussian Blur (3x3)
	private Pixel[][] gaussianBlur3x3() {

		gaussianBlurFilter(3, 3);

		return null;
	}

	// Filter Gaussian Blur (5x5)
	private Pixel[][] gaussianBlur5x5() {

		gaussianBlurFilter(5, 5);

		return null;
	}
}
