package wProject;

import java.io.File;
import java.io.Writer;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class FilterWizard {

	private final File file;

	private Scanner scanner;

	public static int colorMaxValue;

	public FilterWizard(File file) {
		this.file = file;
	}

	// get the input file
	private File getFile(File file) {

		return file;
	}

	// get the file content into String List
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

		colorMaxValue = Integer.parseInt(lines.get(2));

		return lines;
	}

	// get the resolution indexes
	public int getResolutionIndex(char index) {

		String[] lineContent = getFileLines().get(1).split(" ");

		int x = Integer.parseInt(lineContent[0]);
		int y = Integer.parseInt(lineContent[1]);

		if (index == 'x')
			return x;
		else
			return y;
	}

	// get the pixel's lines only
	private List<String> getPixelsLines() {

		List<String> lines = getFileLines();

		lines.remove(0);
		lines.remove(0);
		lines.remove(0);

		return lines;
	}

	// get the pixel's lines List into Pixel List
	private List<Pixel> getLinesIntoPixels() {

		List<String> lines = getPixelsLines();

		List<Pixel> pixelLineContent = new ArrayList<Pixel>();

		for (int i = 0; i < lines.size(); i++) {

			String[] rgb = lines.get(i).split(" ");

			int red = Integer.parseInt(rgb[0]);
			int green = Integer.parseInt(rgb[1]);
			int blue = Integer.parseInt(rgb[2]);

			pixelLineContent.add(new Pixel(red, green, blue));
		}

		return pixelLineContent;
	}

	// get the Pixel List into Pixel matrix
	private Pixel[][] getPixelMatrix() {

		int x = getResolutionIndex('x');
		int y = getResolutionIndex('y');

		Pixel[][] pixelMatrix = new Pixel[y][x];

		List<Pixel> pixelLines = getLinesIntoPixels();

		for (int row = 0; row < y; row++) {
			for (int column = 0; column < x; column++)

				pixelMatrix[row][column] = pixelLines.get(row * x + column);
		}

		return pixelMatrix;

	}

	// get the multiplied Pixel and the division number in 3 by 3
	private int pixelMult3x3(Pixel newPixel, Pixel[][] pixelMatrix, int column, int row, int startX, int startY,
			int endX, int endY, int center, int corner, int side) {

		int div = 0;

		int centerX = (startX + endX) / 2;
		int centerY = (startY + endY) / 2;

		if (row == centerY && column == centerX) {

			newPixel.add(pixelMatrix[row][column].mult(center));
			div += center;

		} else if ((column == centerX - 1 && row == centerY) || (column == centerX + 1 && row == centerY)
				|| (column == centerX && row == centerY - 1) || (column == centerX && row == centerY + 1)) {

			newPixel.add(pixelMatrix[row][column].mult(side));
			div += side;

		} else {
			newPixel.add(pixelMatrix[row][column].mult(corner));
			div += corner;

		}

		return div;
	}

	// get filtered Pixel from specific scale
	private Pixel getFilteredPixel(int pixelX, int pixelY, Pixel[][] pixelMatrix, int scaleX, int scaleY,
			String filter) {

		int div = 0;

		Pixel newPixel = new Pixel(0, 0, 0);

		int startX = (pixelX - scaleX / 2) >= 0 ? (pixelX - scaleX / 2) : 0;
		int startY = (pixelY - scaleY / 2) >= 0 ? (pixelY - scaleY / 2) : 0;

		int endX = (pixelX + scaleX / 2) < pixelMatrix[0].length ? (pixelX + scaleX / 2) : pixelMatrix[0].length - 1;
		int endY = (pixelY + scaleY / 2) < pixelMatrix.length ? (pixelY + scaleY / 2) : pixelMatrix.length - 1;

		for (int column = startX; column <= endX; column++) {
			for (int row = startY; row <= endY; row++) {

				if (filter == "3x3B")
					div += pixelMult3x3(newPixel, pixelMatrix, column, row, startX, startY, endX, endY, 4, 1, 2);

				else if (filter == "3x3S")
					pixelMult3x3(newPixel, pixelMatrix, column, row, startX, startY, endX, endY, 5, 0, -1);

				else if (filter == "5x5B") {
				}

				else if (filter == "Box")
					newPixel.add(pixelMatrix[row][column]);

				else
					System.out.println("[CraftCN filter] Filter unidentified");
			}
		}

		if (filter == "3x3B")
			return newPixel.div(div);

		else if (filter == "3x3S")
			return newPixel.sharpFix();

		else if (filter == "5x5B")
			return newPixel.div((endX - startX + 1) * (endY - startY + 1));

		else if (filter == "Box")
			return newPixel.div((endX - startX + 1) * (endY - startY + 1));

		else
			return null;
	}

	// filter the pixel matrix with a selected filter
	public Pixel[][] filter(int scaleX, int scaleY, String filter) {

		Pixel[][] pixelMatrix = getPixelMatrix();

		Pixel[][] filteredMatrix = new Pixel[pixelMatrix.length][pixelMatrix[0].length];

		for (int row = 0; row < pixelMatrix.length; row++) {
			for (int column = 0; column < pixelMatrix[0].length; column++)

				filteredMatrix[row][column] = getFilteredPixel(column, row, pixelMatrix, scaleX, scaleY, filter);
		}

		return filteredMatrix;
	}

	// create new PPM file
	public void saveFile(Pixel[][] matrix, String name) {

		File filteredFile = new File(name);

		StringBuilder builder = new StringBuilder();

		List<String> lines = getFileLines();

		builder.append(lines.get(0) + "\n");
		builder.append(lines.get(1) + "\n");
		builder.append(lines.get(2) + "\n");

		for (int row = 0; row < matrix.length; row++) {
			for (int column = 0; column < matrix[0].length; column++)

				builder.append(matrix[row][column].toString() + "\n");
		}

		try {

			System.out.println("[CraftCN saveFile] " + filteredFile.getName() + " is running...");

			Writer writer = new FileWriter(filteredFile);

			writer.write(builder.toString());
			writer.close();

			System.out.println("[CraftCN saveFile] " + filteredFile.getName() + " finished!");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}