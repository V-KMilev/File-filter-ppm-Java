package wProject;

public class Pixel {

	private int red;
	private int green;
	private int blue;

	public Pixel(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	// get red value
	public int getRed() {
		return this.red;
	}

	// get green value
	public int getGreen() {
		return this.green;
	}

	// get blue value
	public int getBlue() {
		return this.blue;
	}

	// add all the pixel's values in the scale
	public void add(Pixel other) {
		red += other.red;
		green += other.green;
		blue += other.blue;
	}

	// subtract and get the average pixel
	public Pixel sub(int number) {
		return new Pixel(red /= number, green /= number, blue /= number);
	}

	// easier write into the file
	@Override
	public String toString() {
		return this.red + " " + this.green + " " + this.blue;
	}
}