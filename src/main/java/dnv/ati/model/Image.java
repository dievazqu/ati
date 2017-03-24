package dnv.ati.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.function.BiFunction;
import java.util.function.Function;

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
	
	public double getGray(int i, int j){
		double sum = 0;
		int k;
		for(k=0; k<data[0][0].length; k++){
			sum+=data[i][j][k];
		}
		return (sum/k); 
	}
	
	public int bands(){
		return data[0][0].length;
	}
	
	public double getDataValue(int i, int j, int k){
		if(i<0 || i>=data.length || j<0 || j>=data[0].length
				|| k<0 || k>=data[0][0].length){
			return 0;
		}
		return data[i][j][k];
	}
	
	public void setDataValue(int i, int j, int k, double value){
		data[i][j][k] = value;
	}

	public void setRGB(int i, int j, int rgb) {
		data[i][j][0] = (rgb & 0x0FF0000) >> 16;
		data[i][j][1] = (rgb & 0x000FF00) >> 8;
		data[i][j][2] = (rgb & 0x00000FF);
	}

	public double getOnlyR(int i, int j) {
		return data[i][j][0];
	}
	
	public double getOnlyG(int i, int j) {
		return data[i][j][1];
	}
	
	public double getOnlyB(int i, int j) {
		return data[i][j][2];
	}
	
	public void setOnlyR(int i, int j, double value) {
		data[i][j][0] = value;
	}

	public void setOnlyG(int i, int j, double value) {
		data[i][j][1] = value;
	}

	public void setOnlyB(int i, int j, double value) {
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

	public Image copy(int x1, int y1, int x2, int y2) {
		if(x2>=width || y2>=height || x1<0 || y1<0 || x2<x1 || y2<y1){
			throw new IllegalArgumentException();
		}
		Image img = new Image(x2-x1+1, y2-y1+1);
		for(int x=x1; x<=x2; x++){
			for(int y=y1; y<=y2; y++){
				img.setRGB(y-y1, x-x1, getRGB(y, x));
			}
		}
		return img;
	}
	
	private double average(BiFunction<Integer, Integer, Double> f, int x1, int y1, int x2, int y2){
		if(x2>=width || y2>=height || x1<0 || y1<0 || x2<x1 || y2<y1){
			throw new IllegalArgumentException();
		}
		double sum = 0;
		for(int x=x1; x<=x2; x++){
			for(int y=y1; y<=y2; y++){
				sum+=f.apply(y, x);
			}
		}
		return sum / ((x2-x1+1)*(y2-y1+1));
	}

	public double averageGray(int x1, int y1, int x2, int y2) {
		return average(this::getGray, x1, y1, x2, y2);
	}
	
	public double averageR(int x1, int y1, int x2, int y2) {
		return average(this::getOnlyR, x1, y1, x2, y2);
	}
	
	public double averageG(int x1, int y1, int x2, int y2) {
		return average(this::getOnlyG, x1, y1, x2, y2);
	}
	
	public double averageB(int x1, int y1, int x2, int y2) {
		return average(this::getOnlyB, x1, y1, x2, y2);
	}
	
	private void map(Function<Double, Double> fc){
		for(int k=0; k<data[0][0].length; k++){
			for(int i=0; i<data.length; i++){
				for(int j=0; j<data[0].length; j++){
					data[i][j][k] = fc.apply(data[i][j][k]);
				}
			}
		}
	}
	
	public void negative(){
		map(x -> 255 - x);
	}
	
	public void gammaPower(double gamma){
		double c = Math.pow(255.0, 1-gamma);
		map(x -> c * Math.pow(x, gamma));
	}
	
	public void prodByScalar(double scalar){
		map(x -> x*scalar);
		dynamicRange();
	}
	
	public void normalize(){
		for(int k=0; k<data[0][0].length; k++){
			double min = data[0][0][k];
			double max = data[0][0][k];
			for(int i=0; i<data.length; i++){
				for(int j=0; j<data[0].length; j++){
					min = Math.min(min, data[i][j][k]);
					max = Math.max(max, data[i][j][k]);
				}
			}
			for(int i=0; i<data.length; i++){
				for(int j=0; j<data[0].length; j++){
					data[i][j][k] = (data[i][j][k] - min) * 255.0 / (double)(max-min) ;
				}
			}
		}
	}
	
	public void dynamicRange(){
		for(int k=0; k<data[0][0].length; k++){
			double max = data[0][0][k];
			for(int i=0; i<data.length; i++){
				for(int j=0; j<data[0].length; j++){
					max = Math.max(max, data[i][j][k]);
				}
			}
			double c = 255.0 / Math.log(1+max);
			for(int i=0; i<data.length; i++){
				for(int j=0; j<data[0].length; j++){
					data[i][j][k] = c * Math.log(1+data[i][j][k]);
				}
			}
		}
	}
	
	
}
