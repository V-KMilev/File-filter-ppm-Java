package wProject;

import java.io.File;
import java.io.IOException;

public class Run {

	public static void main(String[] args) throws IOException {

		String fileName = "resourses\\input.ppm";

		File file = new File(fileName);

		boolean containableFile = true;

		if (!file.exists()) {
			System.out.println("[CraftCN run] This file does not exist!");
			containableFile = false;
		}

		if (!file.getName().endsWith(".ppm")) {
			System.out.println("[CraftCN format] The file format is not .ppm!");
			containableFile = false;
		}

		if (!file.canRead()) {
			System.out.println("[CraftCN readable] The file is not readable!");
			containableFile = false;
		}

		if (containableFile == true) {

			FilterWizard filter = new FilterWizard(file);

			System.out.println("[CraftCN filter] The filter is running...");

			filter.saveFile(filter.filter(0, 0, "3x3S"), "test1.ppm");

			System.out.println("[CraftCN filter] The filter finished!");
		}
	}
}
