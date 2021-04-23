package wProject;

import java.io.File;
import java.io.IOException;

import java.util.Scanner;

public class run {
	public static void main(String[] args) throws IOException {

		boolean containableFile = true;

		try (Scanner scanner = new Scanner(System.in)) {

			String fileName = "resourses\\input.ppm";

			File file = new File(fileName);

			if (!file.exists()) {
				System.out.println("[CraftCN run] This file does not exist!");
				containableFile = false;
			}

			if (!file.getName().contains(".ppm")) {
				System.out.println("[CraftCN format] The file format is not .ppm!");
				containableFile = false;
			}

			if (!file.canRead()) {
				System.out.println("[CraftCN readable] The file is not readable!");
				containableFile = false;
			}

			if (containableFile == true) {

				fileInput server = new fileInput(file);
				server.getMatrix();
			}
		}
	}
}
