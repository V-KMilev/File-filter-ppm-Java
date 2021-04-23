package wProject;

import java.awt.Color;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class fileInput {

	private final File file;

	private List<String> lines;

	private int[][] intMatrix;

	private int[] intLineContent;
	private int[] intLines;

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

		if (lines.get(2) != null) {

			lineContent = lines.get(1).split(" ");

			x = Integer.parseInt(lineContent[0]);
			y = Integer.parseInt(lineContent[1]);

		} else
			System.out.println("[CraftCN resolutionIndex] The file formath is not .ppm!");

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

	// get the pixels lines into RGB int
	private int[] getStringIntoRGBInt() {

		lines = getPixelsLines();

		intLineContent = new int[lines.size()];

		for (int i = 0; i < lines.size(); i++)
			lineContent = lines.get(i).split(" ");

		// create pixels and fill the array with the RGB int
		for (int i = 0; i < lineContent.length; i++) {

			if ((i * 3 + 2) <= lineContent.length) {
				int red = Integer.parseInt(lineContent[i * 3]);
				int green = Integer.parseInt(lineContent[i * 3 + 1]);
				int blue = Integer.parseInt(lineContent[i * 3 + 2]);

				intLineContent[i] = new pixel(red, green, blue).getIntFromColor();
			}
		}

		return intLineContent;
	}

	// create and get the image matrix
	public int[][] getMatrix() {

		int x = getResolutionIndex('x');
		int y = getResolutionIndex('y');

		intMatrix = new int[x][y];

		intLines = getStringIntoRGBInt();

		for (int row = 0; row < y; row++) {
			for (int column = 0; column < x; column++) {

				intMatrix[row][column] = intLines[row * x + column];
				System.out.println(intMatrix[row][column]);
			}
		}

		return intMatrix;
	}

	// filter to a specific scale
	private int[][] filter(int scaleOne, int scaleTwo) {

		intMatrix = getMatrix();

		return null;
	}

	// Filter Sharpen (3x3)
	private Color[][] sharpen3x3() {

		filter(3, 3);

		return null;
	}

	// Filter Gaussian Blur (3x3)
	private Color[][] gaussianBlur3x3() {

		filter(3, 3);

		return null;
	}

	// Filter Gaussian Blur (5x5)
	private Color[][] gaussianBlur5x5() {

		filter(5, 5);

		return null;
	}
}
