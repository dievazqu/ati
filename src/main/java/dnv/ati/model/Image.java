package dnv.ati.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import dnv.ati.util.ConversionUtils;

public class Image {

	private int width, height;
	private double[][][] data;

	public Image(int width, int height) {
		this.width = width;
		this.height = height;
		this.data = new double[height][width][3];
	}

	public void setGrayColor(int i, int j, double value) {
		data[i][j][0] = value;
		data[i][j][1] = value;
		data[i][j][2] = value;
	}

	public void setRGBColor(int i, int j, int rgb) {
		data[i][j][0] = (rgb & 0x0FF0000) >> 16;
		data[i][j][1] = (rgb & 0x000FF00) >> 8;
		data[i][j][2] = (rgb & 0x00000FF);
	}

	public void setOnlyRColor(int i, int j, double value) {
		data[i][j][0] = value;
	}

	public void setOnlyGColor(int i, int j, double value) {
		data[i][j][1] = value;
	}

	public void setOnlyBColor(int i, int j, double value) {
		data[i][j][2] = value;
	}

	public BufferedImage toBufferedImage() {
		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				bi.setRGB(i, j, getRGB(j, i));
			}
		}
		return bi;
	}

	public void draw(Graphics g, int x, int y) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				g.setColor(new Color(getRGB(j, i)));
				g.drawRect(x + i, y + j, 1, 1);
			}
		}
	}

	public int getRGB(int i, int j) {
		return ConversionUtils.doubleToRGBInt(
				data[i][j][0],
				data[i][j][1],
				data[i][j][2]);
	}

	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
}
