package dnv.ati.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image {

	private int width, height;
	private int[][] data;

	private static Image convert(BufferedImage bi){
		Image img = new Image();
		img.height = bi.getHeight();
		img.width = bi.getWidth();
		img.data = new int[img.width][img.height];
		for(int i=0; i<img.width; i++){
			for(int j=0; j<img.height; j++){
				img.data[i][j] = bi.getRGB(i, j);
			}
		}
		return img;
	}
	
	public static Image readFromRAW(File file){
		//TODO: parse File in RAW to Image
		return null;
	}
	
	public static Image readFromPGM(File file){
		//TODO: parse File in PGM to Image
		return null;
	}
	
	public static Image readFromPPM(File file){
		//TODO: parse File in PPM to Image
		return null;
	}
	
	public static Image readFromBPM(File file){
		try{
			return convert(ImageIO.read(file));
		} catch (IOException ee) {
			System.out.println("Exception");
		}
		return null;
	}

	public void draw(Graphics g, int x, int y) {
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				g.setColor(new Color(data[i][j]));
				g.drawRect(x+i, y+j, 1, 1);
			}
		}
	}
}
