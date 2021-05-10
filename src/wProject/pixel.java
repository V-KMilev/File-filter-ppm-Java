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

	// color fix for sharp filter
	private int colorFix(int color, int number) {

		if (color * number < 0)
			return color = 0;

		// 255 needs to be .getColorMaxValue();
		else if (color * number > 255)
			return color = 255;

		else
			return color *= number;
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

	// multiplies and get the sharped pixel
	public Pixel mult(int number) {
		return new Pixel(colorFix(red, number), colorFix(green, number), colorFix(blue, number));
	}

	// add all the pixel's values in the scale
	public void add(Pixel other) {
		red += other.red;
		green += other.green;
		blue += other.blue;
	}

	// divides and get the average pixel
	public Pixel div(int number) {
		return new Pixel(red /= number, green /= number, blue /= number);
	}

	// easier write into the file
	@Override
	public String toString() {
		return this.red + " " + this.green + " " + this.blue;
	}
}