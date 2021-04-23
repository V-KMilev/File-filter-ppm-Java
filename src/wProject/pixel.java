package wProject;

public class pixel {

	private int red;
	private int green;
	private int blue;

	public pixel(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public int getIntFromColor() {

		red = (red << 16) & 0x00FF0000;
		green = (green << 8) & 0x0000FF00;
		blue = blue & 0x000000FF;

		return 0xFF000000 | red | green | blue;
	}
}
