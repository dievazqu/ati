package dnv.ati.util;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import javax.imageio.ImageIO;

import dnv.ati.model.Image;

public class ImageUtils {

	public static int[] grayHistogram(Image image) {
		int[] histogram = new int[256];
		for(int i=0; i<image.getWidth(); i++){
			for(int j=0; j<image.getHeight(); j++){
				histogram[(int) image.getGray(i, j)]++;
			}
		}
		return histogram;
	}

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
					if(gray<0) gray+=256;
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
						if(gray<0) gray+=256;
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
						int color = dis.readByte();
						color = color < 0 ? color + 256 : color;
						img.setOnlyR(i, j, color * 256.0 / maxValue);
						color = dis.readByte();
						color = color < 0 ? color + 256 : color;
						img.setOnlyG(i, j, color * 256.0 / maxValue);
						color = dis.readByte();
						color = color < 0 ? color + 256 : color;
						img.setOnlyB(i, j, color * 256.0 / maxValue);
					}
				}
			}
			return img;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Image readFromBMP(File file) {
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
					dos.write((int)image.getGray(i, j));
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
					dos.write((int)image.getGray(i, j));
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
					int rgb = image.getRGB(i, j);
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
	
	public static Image zoomX2(Image image){
		Image img = new Image(image.getWidth()/2, image.getHeight()/2);
		for(int i=0; i<image.getHeight()/2; i++){
			for(int j=0; j<image.getWidth()/2; j++){
				double red = (image.getOnlyR(i*2, j*2)+image.getOnlyR(i*2, j*2+1)+image.getOnlyR(i*2+1, j*2)+image.getOnlyR(i*2+1, j*2+1))/4;
				img.setOnlyR(i, j, red);
				double green = (image.getOnlyG(i*2, j*2)+image.getOnlyG(i*2, j*2+1)+image.getOnlyG(i*2+1, j*2)+image.getOnlyG(i*2+1, j*2+1))/4;
				img.setOnlyG(i, j, green);
				double blue = (image.getOnlyB(i*2, j*2)+image.getOnlyB(i*2, j*2+1)+image.getOnlyB(i*2+1, j*2)+image.getOnlyB(i*2+1, j*2+1))/4;
				img.setOnlyB(i, j, blue);				
			}
		}
		return img;
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
	
	public static Image sumImage(Image img1, Image img2){
		int maxWidth = Math.max(img1.getWidth(), img2.getWidth());
		int maxHeight = Math.max(img1.getHeight(), img2.getHeight());
		Image ansImage = new Image(maxWidth, maxHeight);
		for(int k=0; k<3; k++){
			for(int i=0; i<maxHeight; i++){
				for(int j=0; j<maxWidth; j++){
					double pixelSum = img1.getDataValue(i, j, k) + img2.getDataValue(i, j, k);
					ansImage.setDataValue(i, j, k, pixelSum);
				}
			}
		}
		ansImage.normalize();
		return ansImage;
	}
	
	public static Image diffImage(Image img1, Image img2){
		int maxWidth = Math.max(img1.getWidth(), img2.getWidth());
		int maxHeight = Math.max(img1.getHeight(), img2.getHeight());
		Image ansImage = new Image(maxWidth, maxHeight);
		for(int k=0; k<3; k++){
			for(int i=0; i<maxHeight; i++){
				for(int j=0; j<maxWidth; j++){
					double pixelSum = img1.getDataValue(i, j, k) - img2.getDataValue(i, j, k);
					ansImage.setDataValue(i, j, k, pixelSum);
				}
			}
		}
		ansImage.normalize();
		return ansImage;
	}
	
	public static Image prodImage(Image img1, Image img2){
		int maxWidth = Math.max(img1.getWidth(), img2.getWidth());
		int maxHeight = Math.max(img1.getHeight(), img2.getHeight());
		Image ansImage = new Image(maxWidth, maxHeight);
		for(int k=0; k<3; k++){
			for(int i=0; i<maxHeight; i++){
				for(int j=0; j<maxWidth; j++){
					double pixelSum = img1.getDataValue(i, j, k) * img2.getDataValue(i, j, k);
					ansImage.setDataValue(i, j, k, pixelSum);
				}
			}
		}
		ansImage.normalize();
		return ansImage;
	}
	
	public static void saltAndPepperNoiseImage(Image img, double percentage, double p0, double p1){
		if(p0<0 || 1<p1 || p1<=p0)
			throw new IllegalArgumentException();
		List<Point> points = new ArrayList<Point>();
		for(int i=0; i<img.getWidth(); i++){
			for(int j=0; j<img.getHeight(); j++){
				points.add(new Point(i,j));
			}
		}
		int total = img.getHeight()*img.getWidth();
		Collections.shuffle(points);
		for(int k=0; k<total*percentage; k++){
			Point point = points.get(k);
			double randomValue = RandomGenerator.generateUniformNumber(0, 1);
			if(randomValue<=p0)
				img.setGrayColor(point.y, point.x, 0);
			if(randomValue>=p1)
				img.setGrayColor(point.y, point.x, 255);
		}
	}
	
	public static Image exponencialNoiseImage(int width, int height, double percentage, double lambda){
		return noisyImages(width, height, 1.0, percentage, () -> RandomGenerator.generateExponencialNumber(lambda));
	}
	
	public static Image rayleighNoiseImage(int width, int height, double percentage, double phi){
		return noisyImages(width, height, 1.0, percentage, () -> RandomGenerator.generateRayleighNumber(phi));
	}
	
	public static Image gaussianNoiseImage(int width, int height, double percentage, double mean, double std){
		return noisyImages(width, height, 0.0, percentage, () -> RandomGenerator.generateGaussNumber(mean, std));
	}
	
	public static Image noisyImages(int width, int height, double initialValue, double percentage, Supplier<Double> supplier){
		Image img = new Image(width, height);
		List<Point> points = new ArrayList<Point>();
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				points.add(new Point(i,j));
				img.setGrayColor(i, j, initialValue);
			}
		}
		int total = width*height;
		Collections.shuffle(points);
		for(int k=0; k<total*percentage; k++){
			Point point = points.get(k);
			img.setGrayColor(point.y, point.x, supplier.get());
		}
		return img;
	}
	
}
