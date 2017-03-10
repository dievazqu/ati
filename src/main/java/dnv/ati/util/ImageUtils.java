package dnv.ati.util;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import dnv.ati.model.Image;

public class ImageUtils {

	public static Image grayScale(){
		int size = 512;
		Image img = new Image(size, size);
		for(int i=0; i<size; i++){
			for(int j=0; j<size; j++){
				img.setGrayColor(i, j, j/2);
			}
		}
		return img;
	}
	
	public static Image colorScale(int colorCase){
		int size = 512;
		Image img = new Image(size, size);
		for(int i=0; i<size; i++){
			for(int j=0; j<size; j++){
				switch (colorCase) {
				case 0:
					img.setOnlyRColor(i, j, 192);
					img.setOnlyGColor(i, j, Math.abs(i-size/2));
					img.setOnlyBColor(i, j, Math.abs(j-size/2));
					break;
				case 1:
					img.setOnlyRColor(i, j, Math.abs(i-size/2));
					img.setOnlyGColor(i, j, 192);
					img.setOnlyBColor(i, j, Math.abs(j-size/2));
					break;
				case 2:
					img.setOnlyRColor(i, j, Math.abs(i-size/2));
					img.setOnlyGColor(i, j, Math.abs(j-size/2));
					img.setOnlyBColor(i, j, 192);
				default:
					break;
				}
				
			}
		}
		return img;
	}
	
	public static Image readFromRAW(File file, int width, int height) {
		try{
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			Image img = new Image(width, height);
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					int gray = dis.readByte();
					img.setGrayColor(i, j, gray);
				}
			}
			return img;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static Image readFromPGM(File file) {
		try {
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			String firstLine = "" + (char) dis.readByte()
					+ (char) dis.readByte();
			if (!firstLine.equals("P5")) {
				return null;
			}
			int width, height, maxValue;
			try {
				width = readNextStringInt(dis);
				height = readNextStringInt(dis);
				maxValue = readNextStringInt(dis);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			Image img = new Image(width, height);
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					if (maxValue > 255) {
						// TODO: Here we need to read two bytes to get the value
						return null;
					} else {
						int gray = dis.readByte();
						double percentage = gray * 256.0 / maxValue;
						img.setGrayColor(i, j, percentage);
					}
				}
			}
			return img;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Image readFromPPM(File file) {
		try {
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			String firstLine = "" + (char) dis.readByte()
					+ (char) dis.readByte();
			if (!firstLine.equals("P6")) {
				return null;
			}
			int width, height, maxValue;
			try {
				width = readNextStringInt(dis);
				height = readNextStringInt(dis);
				maxValue = readNextStringInt(dis);
			} catch (Exception e) {
				return null;
			}
			Image img = new Image(width, height);
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					if (maxValue > 255) {
						// TODO: Here we need to read two bytes to get the value
						return null;
					} else {
						img.setOnlyRColor(i, j, dis.readByte() * 256.0 / maxValue);
						img.setOnlyGColor(i, j, dis.readByte() * 256.0 / maxValue);
						img.setOnlyBColor(i, j, dis.readByte() * 256.0 / maxValue);
					}
				}
			}
			return img;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Image readFromBPM(File file) {
		try {
			return convert(ImageIO.read(file));
		} catch (IOException ee) {
			System.out.println("Exception");
		}
		return null;
	}
	
	public static int readNextStringInt(DataInputStream dis) throws Exception {
		String str = "";
		char ch = (char) dis.readByte();
		while (ch < '0' || ch > '9')
			ch = (char) dis.readByte();
		while (ch >= '0' && ch <= '9') {
			str += ch;
			ch = (char) dis.readByte();
		}
		System.out.println(str);

		if (str == "")
			return 0;
		return Integer.parseInt(str);
	}

	private static Image convert(BufferedImage bi) {
		Image img = new Image(bi.getWidth(), bi.getHeight());
		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				int rgb = bi.getRGB(j, i);
				img.setRGBColor(i, j, rgb);
			}
		}
		return img;
	}
}
