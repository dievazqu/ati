package dnv.ati.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import dnv.ati.util.ConversionUtils;
import dnv.ati.util.ImageUtils;

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

	public double getGray(int i, int j) {
		double sum = 0;
		int k;
		for (k = 0; k < data[0][0].length; k++) {
			sum += data[i][j][k];
		}
		return (sum / k);
	}

	public int bands() {
		return data[0][0].length;
	}

	public double getDataValue(int i, int j, int k) {
		if(i<0){
			i=0;
		}
		if(i>=data.length){
			i=data.length-1;
		}
		if(j<0){
			j=0;
		}
		if(j>= data[0].length){
			j= data[0].length-1;
		}
		if(k<0 || k >= data[0][0].length){
			return 0;
		}
		return data[i][j][k];
	}

	public void setDataValue(int i, int j, int k, double value) {
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
		return ConversionUtils.doubleToRGBInt(data[i][j][0], data[i][j][1],
				data[i][j][2]);
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public Image copy(int x1, int y1, int x2, int y2) {
		if (x2 >= width || y2 >= height || x1 < 0 || y1 < 0 || x2 < x1
				|| y2 < y1) {
			throw new IllegalArgumentException();
		}
		Image img = new Image(x2 - x1 + 1, y2 - y1 + 1);
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				img.setRGB(y - y1, x - x1, getRGB(y, x));
			}
		}
		return img;
	}

	private double average(BiFunction<Integer, Integer, Double> f, int x1,
			int y1, int x2, int y2) {
		if (x2 >= width || y2 >= height || x1 < 0 || y1 < 0 || x2 < x1
				|| y2 < y1) {
			throw new IllegalArgumentException();
		}
		double sum = 0;
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				sum += f.apply(y, x);
			}
		}
		return sum / ((x2 - x1 + 1) * (y2 - y1 + 1));
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

	private void map(Function<Double, Double> fc, int k) {
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				data[i][j][k] = fc.apply(data[i][j][k]);
			}
		}
	}
	
	
	private void map(Function<Double, Double> fc) {
		for (int k = 0; k < data[0][0].length; k++) {
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[0].length; j++) {
					data[i][j][k] = fc.apply(data[i][j][k]);
				}
			}
		}
	}

	public void negative() {
		map(x -> 255 - x);
	}

	public void gammaPower(double gamma) {
		double c = Math.pow(255.0, 1 - gamma);
		map(x -> c * Math.pow(x, gamma));
	}

	public void prodByScalar(double scalar) {
		map(x -> x * scalar);
		dynamicRange();
	}

	public void normalize() {
		for (int k = 0; k < data[0][0].length; k++) {
			double min = data[0][0][k];
			double max = data[0][0][k];
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[0].length; j++) {
					min = Math.min(min, data[i][j][k]);
					max = Math.max(max, data[i][j][k]);
				}
			}
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[0].length; j++) {
					data[i][j][k] = (data[i][j][k] - min) * 255.0
							/ (double) (max - min);
				}
			}
		}
	}

	public void dynamicRange() {
		for (int k = 0; k < data[0][0].length; k++) {
			double max = data[0][0][k];
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[0].length; j++) {
					max = Math.max(max, data[i][j][k]);
				}
			}
			double c = 255.0 / Math.log(1 + max);
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[0].length; j++) {
					data[i][j][k] = c * Math.log(1 + data[i][j][k]);
				}
			}
		}
	}

	public void contrast(double r1, double s1, double r2, double s2) {
		map(x -> {
			if (x < r1) {
				return x * s1 / r1;
			} else if (x < r2) {
				return (x - r1) * (s2 - s1) / (r2 - r1) + s1;
			} else {
				return (x - r2) * (255 - s2) / (255 - r2) + s2;
			}
		});
	}

	public void umbralize(double umbral, int k) {
		map(x -> {
			if (x < umbral) {
				return 0.0;
			}
			return 255.0;
		}, k);
	}
	
	public void umbralize(double umbral) {
		map(x -> {
			if (x < umbral) {
				return 0.0;
			}
			return 255.0;
		});
	}

	public void equalize() {
		int[] histogram = ImageUtils.grayHistogram(this);
		double[] data = new double[256];
		int total = width * height;
		int acum = 0;
		for (int i = 0; i < histogram.length; i++) {
			acum += histogram[i];
			data[i] = acum * 255.0 / (double) total;
		}
		map(x -> data[(int) Math.round(x)]);
	}
	
	private interface FilterFunction {
		public double apply(int i, int j, int k);
	}

	public double[][][] newImageDataFromFilter(FilterFunction filter){
		double[][][] newImageData = new double[height][width][3];
		for (int k = 0; k < 3; k++) {
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					newImageData[i][j][k] = filter.apply(i, j, k);
				}
			}
		}
		return newImageData;
	}
	
	public void genericFilter(FilterFunction filter) {
		data = newImageDataFromFilter(filter);
	}

	public void medianFilter(int windowSize) {
		genericFilter(this.medianFilterFunction(windowSize));
	}

	public void meanFilter(int windowSize) {
		genericFilter(this.meanFilerFunction(windowSize));
	}

	public void gaussianFilter(int windowSize, double sigma) {
		genericFilter(this.gaussianFilterFunction(windowSize, sigma));
		normalize();
	}

	public void weightedMedianFilter() {
		genericFilter(this.weightedMedianFunction(3));
	}

	public void borderFilter() {
		genericFilter(this.borderFilterFunction(3));
		normalize();
	}
	
	public void maskFilter(double[][] mask){
		genericFilter(this.maskFilterFunction(mask));
		normalize();
	}

	private void gradientFilters(double[][] xMask, double[][] yMask){
		double[][][] x = newImageDataFromFilter(maskFilterFunction(
				xMask));
		double[][][] y = newImageDataFromFilter(maskFilterFunction(
				yMask));
		double[][][] ans = new double[x.length][x[0].length][x[0][0].length];
		for(int i=0; i<x.length; i++){
			for(int j=0; j<x[0].length; j++){
				for(int k=0; k<x[0][0].length; k++){
					double xx = x[i][j][k];
					double yy = y[i][j][k];
					ans[i][j][k] = Math.sqrt(xx*xx+yy*yy);
				}
			}
		}
		data = ans;
		normalize();
	}
	
	public void prewitFilter() {
		gradientFilters(new double[][]{{-1,0,1},{-1,0,1},{-1,0,1}}, new double[][]{{-1,-1,-1},{0,0,0},{1,1,1}});
	}
	
	public void sobelFilter(){
		gradientFilters(new double[][]{{-1,0,1},{-2,0,2},{-1,0,1}}, new double[][]{{-1,-2,-1},{0,0,0},{1,2,1}});
	}
	
	private FilterFunction maskFilterFunction(double[][] mask){
		return new FilterFunctionWrapper(new FilterFunction() {
			public double apply(int x, int y, int k) {
				int offset = (mask.length-1)/2;
				double ans = 0;
				for(int i=0; i<mask.length; i++){
					for(int j=0; j<mask.length; j++){
						ans+=mask[i][j] * data[x+i-offset][y+j-offset][k];
					}
				}
				
				return ans;
			}
		}, mask.length);
	}
	
	private FilterFunction weightedMedianFunction(int windowSize) {
		return new FilterFunctionWrapper(new FilterFunction() {
			@Override
			public double apply(int x, int y, int k) {
				int offset = (windowSize-1)/2;
				List<Double> values = new ArrayList<Double>();
				for (int i = x - offset; i <= x + offset; i++) {
					for (int j = y - offset; j <= y + offset; j++) {
						int dist = Math.abs(i - x) + Math.abs(j - y);
						int repetitions;
						switch (dist) {
						case 0:
							repetitions = 4;
							break;
						case 1:
							repetitions = 2;
							break;
						case 2:
							repetitions = 1;
							break;
						default:
							repetitions = 0;
						}
						for (int l = 0; l < repetitions; l++) {
							values.add(data[i][j][k]);
						}
					}
				}
				Collections.sort(values);
				return (values.get(values.size() / 2) + values
						.get(values.size() / 2 + 1)) / 2;
			}
		}, windowSize);
		
	}

	private FilterFunction medianFilterFunction(int windowSize) {
		return new FilterFunctionWrapper(new FilterFunction() {
			@Override
			public double apply(int x, int y, int k) {
				int offset = (windowSize-1)/2;
				List<Double> values = new ArrayList<Double>();
				for (int i = x - offset; i <= x + offset; i++) {
					for (int j = y - offset; j <= y + offset; j++) {
						values.add(data[i][j][k]);
					}
				}
				Collections.sort(values);
				return values.get(values.size() / 2);
			}
		}, windowSize);
	}

	private FilterFunction meanFilerFunction(int windowSize) {
		return new FilterFunctionWrapper(new FilterFunction() {
			@Override
			public double apply(int x, int y, int k) {
				int offset = (windowSize-1)/2;
				double sum = 0.0;
				for (int i = x - offset; i <= x + offset; i++) {
					for (int j = y - offset; j <= y + offset; j++) {
						sum += data[i][j][k];
					}
				}
				int size = offset * 2 + 1;
				return sum / (size * size);
			}
		}, windowSize);
	}

	private FilterFunction gaussianFilterFunction(int windowSize, double sigma){
		return new FilterFunctionWrapper(new FilterFunction() {
			public double apply(int x, int y, int k) {
				double sum = 0.0;
				int offset = (windowSize-1)/2;
				for (int i = x - offset; i <= x + offset; i++) {
					for (int j = y - offset; j <= y + offset; j++) {
						int dx = i - x;
						int dy = j - y;
						double sigma2 = sigma * sigma;
						double c = Math.pow(Math.E, -(dx * dx + dy * dy) / sigma2)
								/ (2 * Math.PI * sigma2);
						sum += c * data[i][j][k];
					}
				}
				return sum;
			}
		}, windowSize);
	}

	private FilterFunction borderFilterFunction(int windowSize) {
		return new FilterFunctionWrapper(new FilterFunction() {
			@Override
			public double apply(int x, int y, int k) {
				int offset = (windowSize-1)/2;
				double sum = 0.0;
				int size = offset * 2 + 1;
				for (int i = x - offset; i <= x + offset; i++) {
					for (int j = y - offset; j <= y + offset; j++) {
						int dist = Math.abs(i - x) + Math.abs(j - y);
						if (dist == 0) {
							sum += (size * size - 1) * data[i][j][k];
						} else {
							sum -= 1.0 * data[i][j][k];
						}
					}
				}
				return sum / (size*size);
			}
		}, windowSize);
		
	}
	
	public Image clone(){
		return copy(0, 0, width-1, height-1);
	}
	
	public class FilterFunctionWrapper implements FilterFunction{
		
		private FilterFunction ff;
		private int windowSize;
		
		public FilterFunctionWrapper(FilterFunction ff, int windowSize){
			this.ff = ff;
			this.windowSize = windowSize;
		}

		@Override
		public double apply(int i, int j, int k) {
			int halfWindow = (windowSize - 1) / 2; 
			if (i < halfWindow || i >= height - halfWindow
						|| j < halfWindow || j >= width - halfWindow) {
					return data[i][j][k];
			}
			return ff.apply(i, j, k);
		}
	}

	public void sobelDFilter() {
		directionalFilter(new double[][]{
				{1,2,1},
				{0,0,0},
				{-1,-2,-1},
		});
	}

	public void prewitDFilter() {
		directionalFilter(new double[][]{
				{1,1,1},
				{0,0,0},
				{-1,-1,-1},
		});
	}

	public void kirshDFilter() {
		directionalFilter(new double[][]{
				{5,5,5},
				{-3,0,-3},
				{-3,-3,-3},
		});
	}

	public void aDFilter() {
		directionalFilter(new double[][]{
				{1,1,1},
				{1,-2,1},
				{-1,-1,-1},
		});
	}
	
		
	private void directionalFilter(double[][] mask){
		if(mask.length != mask[0].length || mask.length != 3){
			throw new IllegalArgumentException("Invalid mask dimension");
		}
		int n = mask.length;
		double[][] yMask = new double[n][n];
		double[][] d1Mask = new double[n][n];
		double[][] d2Mask = new double[n][n];
		// Rotate border pixels
		int n2 = n*n-1;
		Point[] border = new Point[n2];
		fillBorder(border);
		d1Mask[1][1] = mask[1][1];
		d2Mask[1][1] = mask[1][1];
		yMask[1][1] = mask[1][1];
		for(int i=0; i<n2; i++){
			Point curr = border[i];
			Point next = border[(i+1)%n2];
			Point prev = border[(i-1+n2)%n2];
			Point nextNext = border[(i+2)%n2];
			yMask[nextNext.x][nextNext.y] = mask[curr.x][curr.y]; 
			d1Mask[next.x][next.y] = mask[curr.x][curr.y];
			d2Mask[prev.x][prev.y] = mask[curr.x][curr.y];
		}
		directionalFilter(mask, yMask, d1Mask, d2Mask);
	}
	
	private void directionalFilter(double[][] mask, double[][] yMask, double[][] d1Mask, double[][] d2Mask){

		double[][][] x = newImageDataFromFilter(maskFilterFunction(
				mask));
		double[][][] y = newImageDataFromFilter(maskFilterFunction(
				yMask));
		double[][][] d1 = newImageDataFromFilter(maskFilterFunction(
				d1Mask));
		double[][][] d2 = newImageDataFromFilter(maskFilterFunction(
				d2Mask));
		double[][][] ans = new double[x.length][x[0].length][x[0][0].length];
		for(int i=0; i<x.length; i++){
			for(int j=0; j<x[0].length; j++){
				for(int k=0; k<x[0][0].length; k++){
					double xx = Math.abs(x[i][j][k]);
					double yy = Math.abs(y[i][j][k]);
					double dd1 = Math.abs(d1[i][j][k]);
					double dd2 = Math.abs(d2[i][j][k]);
					ans[i][j][k] = Math.max(Math.max(xx, yy), Math.max(dd1, dd2));
				}
			}
		}
		data = ans;
		normalize();
	}
	
	private void fillBorder(Point[] border){
		int k=0;
		border[k++] = new Point(0,0);
		border[k++] = new Point(0,1);
		border[k++] = new Point(0,2);
		border[k++] = new Point(1,2);
		border[k++] = new Point(2,2);
		border[k++] = new Point(2,1);
		border[k++] = new Point(2,0);
		border[k++] = new Point(1,0);
	}

	public void globalUmbral() {
		for(int k=0; k<data[0][0].length; k++){
			double T = 0;
			for(int i=0; i<data.length; i++){
				for(int j=0; j<data[0].length; j++){
					T += data[i][j][k];
				}
			}
			T /= data.length * data[0].length;
			double umbral = iterateGlobalUmbral(T, k);
			umbralize(umbral, k);
		}
	}
	
	private double iterateGlobalUmbral(double T, int k){
		double s1=0, s2=0;
		int n1=0, n2=0;
		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				double color = data[i][j][k];
				if(color <= T){
					s1 += color;
					n1++;
				}else{
					s2 += color;
					n2++;
				}
			}
		}
		double nextT = ((s1/n1)+(s2/n2))*0.5;
		if(Math.abs(nextT-T)<0.5){
			System.out.println("Umbral: "+nextT);
			return nextT;
		}else{
			return iterateGlobalUmbral(nextT, k);
		}
	}

	private final double EPS = 1e-9;
	
	/* Trata a la imagen como gris, deberiamos cambiarlo tal vez*/
	public void otsuUmbral() {
		normalize();
		int[] histogram = ImageUtils.grayHistogram(this);
		double size = width*height;
		double[] acumSum = new double[256];
		double[] m = new double[256];
		acumSum[0] = histogram[0] / size;
		m[0] = 0;
		for(int i=1; i<256; i++){
			acumSum[i] = acumSum[i-1] + (histogram[i] / size);
			m[i] = m[i-1] + (i*histogram[i] / size);
		}
		double maxx = -1;
		List<Integer> maxs = new LinkedList<Integer>();
		for(int i=0; i<256; i++){
			double var = Math.pow(m[255]*acumSum[i] - m[i], 2) / (acumSum[i]*(1-acumSum[i]));
			if(Math.abs(maxx-var)<EPS){
				maxs.add(i);
			}else{
				if(maxx < var){
					maxx = var;
					maxs.clear();
					maxs.add(i);
				}
			}
		}
		double sum = 0;
		for(Integer i : maxs){
			sum+=i;
		}
		sum/=maxs.size();
		System.out.println("Umbral: "+sum);
		umbralize(sum);
	}
	
	

	public void isotropicDiffusion(int t) {
		while(t-->0){
			genericFilter((i,j,k) -> {
				double sum = 0;
				int[] dx = new int[]{0,0,1,-1};
				int[] dy = new int[]{1,-1,0,0};
				for(int a=0; a<4; a++){
					sum+=getDataValue(dx[a]+i, dy[a]+j, k);
				}
				return sum/4.0;
			});
		}
	}
	
	public void anisotropicDiffusion(int t, Function<Double, Double> f) {
		while(t-->0){
			genericFilter((i,j,k) -> {
				double sum = 0;
				int[] dx = new int[]{0,0,1,-1};
				int[] dy = new int[]{1,-1,0,0};
				for(int a=0; a<4; a++){
					double diff = getDataValue(dx[a]+i, dy[a]+j, k)-getDataValue(i, j, k);
					sum+=f.apply(diff)*diff;
				}
				return data[i][j][k]+(sum/4.0);
			});
		}
	}
	
}
