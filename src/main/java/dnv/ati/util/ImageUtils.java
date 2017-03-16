package dnv.ati.util;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
					img.setOnlyR(i, j, 192);
					img.setOnlyG(i, j, Math.abs(i-size/2));
					img.setOnlyB(i, j, Math.abs(j-size/2));
					break;
				case 1:
					img.setOnlyR(i, j, Math.abs(i-size/2));
					img.setOnlyG(i, j, 192);
					img.setOnlyB(i, j, Math.abs(j-size/2));
					break;
				case 2:
					img.setOnlyR(i, j, Math.abs(i-size/2));
					img.setOnlyG(i, j, Math.abs(j-size/2));
					img.setOnlyB(i, j, 192);
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
						img.setOnlyR(i, j, dis.readByte() * 256.0 / maxValue);
						img.setOnlyG(i, j, dis.readByte() * 256.0 / maxValue);
						img.setOnlyB(i, j, dis.readByte() * 256.0 / maxValue);
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
				img.setRGB(i, j, rgb);
			}
		}
		return img;
	}

	public static void saveInRAW(File file, Image image) {
		try{
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
			int width = image.getWidth();
			int height = image.getHeight();
			for(int i=0; i<height; i++){
				for(int j=0; j<width; j++){
					dos.write((int)image.getGray(j, i));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void saveInPGM(File file, Image image) {
		try{
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
			int width = image.getWidth();
			int height = image.getHeight();
			dos.writeBytes("P5\n");
			dos.writeBytes(String.valueOf(width)+" "+String.valueOf(height)+"\n255\n");
			for(int i=0; i<height; i++){
				for(int j=0; j<width; j++){
					dos.write((int)image.getGray(j, i));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void saveInPPM(File file, Image image) {
		try{
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
			int width = image.getWidth();
			int height = image.getHeight();
			dos.writeBytes("P6\n");
			dos.writeBytes(String.valueOf(width)+" "+String.valueOf(height)+"\n255\n");
			for(int i=0; i<height; i++){
				for(int j=0; j<width; j++){
					int rgb = image.getRGB(j, i);
					dos.write( (rgb & 0x0FF0000) >> 16);
					dos.write( (rgb & 0x000FF00) >> 8);
					dos.write( (rgb & 0x00000FF));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void saveInBMP(File file, Image image) {
		try{
			ImageIO.write(image.toBufferedImage(), "bmp", file);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static Image redFilter(Image image){
		Image img = new Image(image.getWidth(), image.getHeight());
		for(int i=0; i<image.getHeight(); i++){
			for(int j=0; j<image.getWidth(); j++){
				img.setOnlyR(i, j, image.getOnlyR(i, j));
			}
		}
		return img;
	}
	
	public static Image greenFilter(Image image){
		Image img = new Image(image.getWidth(), image.getHeight());
		for(int i=0; i<image.getHeight(); i++){
			for(int j=0; j<image.getWidth(); j++){
				img.setOnlyG(i, j, image.getOnlyG(i, j));
			}
		}
		return img;
	}
	
	public static Image blueFilter(Image image){
		Image img = new Image(image.getWidth(), image.getHeight());
		for(int i=0; i<image.getHeight(); i++){
			for(int j=0; j<image.getWidth(); j++){
				img.setOnlyB(i, j, image.getOnlyB(i, j));
			}
		}
		return img;
	}
	
	public static Image grayFilter(Image image){
		Image img = new Image(image.getWidth(), image.getHeight());
		for(int i=0; i<image.getHeight(); i++){
			for(int j=0; j<image.getWidth(); j++){
				img.setGrayColor(i, j, image.getGray(i, j));
			}
		}
		return img;
	}
}
