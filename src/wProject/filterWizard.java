package wProject;

import java.io.File;
import java.io.Writer;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.List;
import java.util.Scanner;

import java.util.ArrayList;

public class FilterWizard {

	private final File file;

	private Scanner scanner;

	// max color value
	public static int colorMaxValue;

	// kernel matrix for 3 by 3 Gaussian blur
	private final int[][] kernalMatrixBlur3x3 =
		{
			{ 1, 2, 1 },
			{ 2, 4, 2 },
			{ 1, 2, 1 }
		};

	// kernel matrix for 3 by 3 Sharpen
	private final int[][] kernalMatrixSharpen3x3 =
		{
			{ 0, -1, 0 },
			{ -1, 5, -1 },
			{ 0, -1, 0 }
		};

	// kernel for 5 by 5 Gaussian blur
	private final int[][] kernalMatrixBlur5x5 =
		{
			{ 1, 4, 6, 4, 1 },
			{ 4, 16, 24, 16, 4 },
			{ 6, 24, 36, 24, 6 },
			{ 4, 16, 24, 16, 4 },
			{ 1, 4, 6, 4, 1 }
		};

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
	private int getResolutionIndex(char index) {

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

	// get filtered Pixel for specific kernel
	private Pixel kernelFilter(int kernelMatrix[][], Pixel pixelMatrix[][], int pixelX, int pixelY) {

		Pixel newPixel = new Pixel(0, 0, 0);

		int div = 0;

		int kernalLengthX = kernelMatrix.length;
		int kernalLengthY = kernelMatrix[0].length;

		int centerKernalX = kernalLengthX / 2;
		int centerKernalY = kernalLengthY / 2;

		for (int row = -(kernalLengthX / 2); row <= kernalLengthX / 2; row++) {
			for (int column = -(kernalLengthY / 2); column <= kernalLengthY / 2; column++) {

				if (pixelX + row >= pixelMatrix[0].length || pixelY + column >= pixelMatrix.length || pixelX + row < 0
						|| pixelY + column < 0)
					continue;

				newPixel.add(pixelMatrix[pixelY + column][pixelX + row]
						.mult(kernelMatrix[centerKernalY + column][centerKernalX + row]));

				div += kernelMatrix[centerKernalY + column][centerKernalX + row];
			}
		}

		if (kernelMatrix == kernalMatrixSharpen3x3)
			return newPixel.sharpenedPixelFix();

		else
			return newPixel.div(div);
	}

	// get filtered Pixel for Box blur
	private Pixel boxFilter(int scaleX, int scaleY, Pixel[][] pixelMatrix, int pixelX, int pixelY) {

		Pixel newPixel = new Pixel(0, 0, 0);

		int startX = (pixelX - scaleX / 2) >= 0 ? (pixelX - scaleX / 2) : 0;
		int startY = (pixelY - scaleY / 2) >= 0 ? (pixelY - scaleY / 2) : 0;

		int endX = (pixelX + scaleX / 2) < pixelMatrix[0].length ? (pixelX + scaleX / 2) : pixelMatrix[0].length - 1;
		int endY = (pixelY + scaleY / 2) < pixelMatrix.length ? (pixelY + scaleY / 2) : pixelMatrix.length - 1;

		for (int column = startX; column <= endX; column++) {
			for (int row = startY; row <= endY; row++)

				newPixel.add(pixelMatrix[row][column]);
		}

		return newPixel.div((endX - startX + 1) * (endY - startY + 1));
	}

	// filter the Pixel matrix with a selected filter
	public Pixel[][] filter(int scaleX, int scaleY, String filter) {

		Pixel[][] pixelMatrix = getPixelMatrix();

		Pixel[][] filteredMatrix = new Pixel[pixelMatrix.length][pixelMatrix[0].length];

		for (int row = 0; row < pixelMatrix.length; row++) {
			for (int column = 0; column < pixelMatrix[0].length; column++) {

				if (filter == "Box")
					filteredMatrix[row][column] = boxFilter(scaleX, scaleY, pixelMatrix, column, row);

				else if (filter == "3x3B")
					filteredMatrix[row][column] = kernelFilter(kernalMatrixBlur3x3, pixelMatrix, column, row);

				else if (filter == "3x3S")
					filteredMatrix[row][column] = kernelFilter(kernalMatrixSharpen3x3, pixelMatrix, column, row);

				else if (filter == "5x5B")
					filteredMatrix[row][column] = kernelFilter(kernalMatrixBlur5x5, pixelMatrix, column, row);

				else
					System.out.println("[CraftCN filter] Filter unidentified");
			}
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