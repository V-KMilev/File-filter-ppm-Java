package wProject;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class fileInput {

	private final File file;

	private pixel[][] pixelMatrix;

	private pixel[] pixelLineContent;
	private pixel[] pixelLines;

	private List<String> lines;

	private String[][] lineContentRGB;

	private String[] lineContent;

	private String currentLine = "";

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

		lines = new ArrayList<>();

		try {
			scanner = new Scanner(file);

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		}

		while (scanner.hasNextLine()) {

			currentLine = scanner.nextLine();

			lines.add(currentLine);
		}

		return lines;
	}

	// get the resolution indexes
	private int getResolutionIndex(char index) {

		lines = getFileLines();

		int x = 0;
		int y = 0;

		if (lines.get(2) != null || lines.get(2) != "") {

			lineContent = lines.get(1).split(" ");

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

	// get the image lines only
	private List<String> getPixelsLines() {

		lines = getFileLines();

		lines.remove(0);
		lines.remove(0);
		lines.remove(0);

		return lines;
	}

	// get the pixels lines into pixel objects
	private pixel[] getLinesIntoPixels() {

		lines = getPixelsLines();

		pixelLineContent = new pixel[lines.size()];

		lineContentRGB = new String[lines.size()][3];

		for (int row = 0; row < lines.size(); row++) {
			for (int column = 0; column < 3; column++) {

				lineContent = lines.get(row).split(" ");
				lineContentRGB[row][column] = lineContent[row * lines.size() + column];
			}
		}

		// create and fill the pixels
		for (int i = 0; i < lineContent.length; i++) {

			// set the values of red, green and blue
			while (i < i / 3) {
				int red = Integer.parseInt(lineContent[i * 3]);
				int green = Integer.parseInt(lineContent[i * 3 + 1]);
				int blue = Integer.parseInt(lineContent[i * 3 + 2]);

				pixelLineContent[i] = new pixel(red, green, blue);
			}
		}

		return pixelLineContent;
	}

	// create and get the image matrix
	public pixel[][] getMatrix() {

		int x = getResolutionIndex('x');
		int y = getResolutionIndex('y');

		pixelMatrix = new pixel[x][y];

		pixelLines = getLinesIntoPixels();

		for (int row = 0; row < y; row++) {
			for (int column = 0; column < x; column++) {

				pixelMatrix[row][column] = pixelLines[row * x + column];
				System.out.print(pixelMatrix[row][column] + " ");
			}
		}

		return pixelMatrix;
	}

	// filter to a specific scale
	private pixel[][] filter(int scaleOne, int scaleTwo) {

		pixelMatrix = getMatrix();

		return null;
	}

	// Filter Sharpen (3x3)
	private pixel[][] sharpen3x3() {

		filter(3, 3);

		return null;
	}

	// Filter Gaussian Blur (3x3)
	private pixel[][] gaussianBlur3x3() {

		filter(3, 3);

		return null;
	}

	// Filter Gaussian Blur (5x5)
	private pixel[][] gaussianBlur5x5() {

		filter(5, 5);

		return null;
	}
}
