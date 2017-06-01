package dnv.ati.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import dnv.ati.model.State.LevelSetsInfo;
import dnv.ati.util.Auxiliar;
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

	public void setGrayColor(int i, int j, double value, double[][][] matrix) {
		matrix[i][j][0] = value;
		matrix[i][j][1] = value;
		matrix[i][j][2] = value;
	}
	
	public void setGrayColor(int i, int j, double value) {
		setGrayColor(i, j, value, data);
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
	
	public static int getDataValue(int[][] data, int i, int j){
		if (i < 0) {
			i = 0;
		}
		if (i >= data.length) {
			i = data.length - 1;
		}
		if (j < 0) {
			j = 0;
		}
		if (j >= data[0].length) {
			j = data[0].length - 1;
		}
		return data[i][j];
	}

	public static double getDataValue(double[][][] data, int i, int j, int k) {
		if (i < 0) {
			i = 0;
		}
		if (i >= data.length) {
			i = data.length - 1;
		}
		if (j < 0) {
			j = 0;
		}
		if (j >= data[0].length) {
			j = data[0].length - 1;
		}
		if (k < 0 || k >= data[0][0].length) {
			return 0;
		}
		return data[i][j][k];
	}
	
	public double getDataValue(int i, int j, int k) {
		return getDataValue(data, i, j, k);
	}

	public void setDataValue(int i, int j, int k, double value) {
		data[i][j][k] = value;
	}
	
	public static void setRGB(int i, int j, int rgb, double[][][] data) {
		data[i][j][0] = (rgb & 0x0FF0000) >> 16;
		data[i][j][1] = (rgb & 0x000FF00) >> 8;
		data[i][j][2] = (rgb & 0x00000FF);
	}

	public void setRGB(int i, int j, int rgb) {
		setRGB(i, j, rgb, data);
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

	public double[] getRGBArray(int i, int j){
		return new double[]{data[i][j][0], data[i][j][1],
				data[i][j][2]};
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
	
	private interface FilterFunction2 {
		public double apply(int[][] data, int i, int j);
	}
	
	private interface FilterFunction {
		public double apply(double[][][] data, int i, int j, int k);
	}

	public static double[][] newImageDataFromFilter(FilterFunction2 filter, int[][] data) {
		double[][] newImageData = new double[data.length][data[0].length];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				newImageData[i][j] = filter.apply(data, i, j);
			}
		}
		return newImageData;
	}
	
	public static double[][][] newImageDataFromFilter(FilterFunction filter, double[][][] data) {
		double[][][] newImageData = new double[data.length][data[0].length][data[0][0].length];
		for (int k = 0; k < data[0][0].length; k++) {
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[0].length; j++) {
					newImageData[i][j][k] = filter.apply(data, i, j, k);
				}
			}
		}
		return newImageData;
	}

	public void genericFilter(FilterFunction filter) {
		data = newImageDataFromFilter(filter, data);
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

	public void maskFilter(double[][] mask) {
		genericFilter(this.maskFilterFunction(mask));
		normalize();
	}

	private Derivates gradientFilters(double[][] xMask, double[][] yMask) {
		double[][][] x = newImageDataFromFilter(maskFilterFunction(xMask), data);
		double[][][] y = newImageDataFromFilter(maskFilterFunction(yMask), data);
		double[][][] ans = new double[x.length][x[0].length][x[0][0].length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				for (int k = 0; k < x[0][0].length; k++) {
					double xx = x[i][j][k];
					double yy = y[i][j][k];
					ans[i][j][k] = Math.sqrt(xx * xx + yy * yy);
				}
			}
		}
		data = ans;
		normalize();
		return new Derivates(x, y);
	}

	public void prewitFilter() {
		gradientFilters(new double[][] { { -1, 0, 1 }, { -1, 0, 1 },
				{ -1, 0, 1 } }, new double[][] { { -1, -1, -1 }, { 0, 0, 0 },
				{ 1, 1, 1 } });
	}

	public Derivates sobelFilter() {
		return gradientFilters(new double[][] { { -1, 0, 1 }, { -2, 0, 2 },
				{ -1, 0, 1 } }, new double[][] { { -1, -2, -1 }, { 0, 0, 0 },
				{ 1, 2, 1 } });
	}

	private static FilterFunction maskFilterFunction(double[][] mask) {
		return new FilterFunction() {
			public double apply(double[][][] data, int x, int y, int k) {
				int offset = (mask.length - 1) / 2;
				double ans = 0;
				for (int i = 0; i < mask.length; i++) {
					for (int j = 0; j < mask.length; j++) {
						ans += mask[i][j]
								* getDataValue(data, x + i - offset, y + j - offset,
										k);
					}
				}

				return ans;
			}
		};
	}

	private static FilterFunction weightedMedianFunction(int windowSize) {
		return new FilterFunction() {
			@Override
			public double apply(double[][][] data, int x, int y, int k) {
				int offset = (windowSize - 1) / 2;
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
							values.add(getDataValue(data, i, j, k));
						}
					}
				}
				Collections.sort(values);
				return (values.get(values.size() / 2) + values.get(values
						.size() / 2 + 1)) / 2;
			}
		};

	}

	private static FilterFunction medianFilterFunction(int windowSize) {
		return new FilterFunction() {
			@Override
			public double apply(double[][][] data, int x, int y, int k) {
				int offset = (windowSize - 1) / 2;
				List<Double> values = new ArrayList<Double>();
				for (int i = x - offset; i <= x + offset; i++) {
					for (int j = y - offset; j <= y + offset; j++) {
						values.add(getDataValue(data, i, j, k));
					}
				}
				Collections.sort(values);
				return values.get(values.size() / 2);
			}
		};
	}

	private static FilterFunction meanFilerFunction(int windowSize) {
		return new FilterFunction() {
			@Override
			public double apply(double[][][] data, int x, int y, int k) {
				int offset = (windowSize - 1) / 2;
				double sum = 0.0;
				for (int i = x - offset; i <= x + offset; i++) {
					for (int j = y - offset; j <= y + offset; j++) {
						sum += getDataValue(data, i, j, k);
					}
				}
				int size = offset * 2 + 1;
				return sum / (size * size);
			}
		};
	}
	
	private static FilterFunction2 gaussianFilterFunction2(int windowSize, double sigma) {
	
		double[][] values = new double[windowSize][windowSize];
		int offset = (windowSize - 1) / 2;
		for (int i = - offset; i <= offset; i++) {
			for (int j = - offset; j <= offset; j++) {
				int dx = i + offset;
				int dy = j + offset;
				double sigma2 = sigma * sigma;
				double c = Math.pow(Math.E, -(i * i + j * j)
						/ sigma2)
						/ (2 * Math.PI * sigma2);
				values[dx][dy] = c;
			}
		}
		
		return new FilterFunction2() {
			public double apply(int[][] data, int x, int y) {
				double sum = 0.0;
				for (int i = x - offset; i <= x + offset; i++) {
					for (int j = y - offset; j <= y + offset; j++) {
						int dx = i - x + offset;
						int dy = j - y + offset;
						sum += values[dx][dy] * getDataValue(data, i, j);
					}
				}
				return sum;
			}
		};
	}

	private static FilterFunction gaussianFilterFunction(int windowSize, double sigma) {
		return new FilterFunction() {
			public double apply(double[][][] data, int x, int y, int k) {
				double sum = 0.0;
				int offset = (windowSize - 1) / 2;
				for (int i = x - offset; i <= x + offset; i++) {
					for (int j = y - offset; j <= y + offset; j++) {
						int dx = i - x;
						int dy = j - y;
						double sigma2 = sigma * sigma;
						double c = Math.pow(Math.E, -(dx * dx + dy * dy)
								/ sigma2)
								/ (2 * Math.PI * sigma2);
						sum += c * getDataValue(data, i, j, k);
					}
				}
				return sum;
			}
		};
	}
	
	
	

	private static FilterFunction borderFilterFunction(int windowSize) {
		return new FilterFunction() {
			@Override
			public double apply(double[][][] data, int x, int y, int k) {
				int offset = (windowSize - 1) / 2;
				double sum = 0.0;
				int size = offset * 2 + 1;
				for (int i = x - offset; i <= x + offset; i++) {
					for (int j = y - offset; j <= y + offset; j++) {
						int dist = Math.abs(i - x) + Math.abs(j - y);
						if (dist == 0) {
							sum += (size * size - 1) * getDataValue(data, i, j, k);
						} else {
							sum -= 1.0 * getDataValue(data, i, j, k);
						}
					}
				}
				return sum / (size * size);
			}
		};

	}

	public Image clone() {
		return copy(0, 0, width - 1, height - 1);
	}

	public void sobelDFilter() {
		directionalFilter(new double[][] { { 1, 2, 1 }, { 0, 0, 0 },
				{ -1, -2, -1 }, });
	}

	public void prewitDFilter() {
		directionalFilter(new double[][] { { 1, 1, 1 }, { 0, 0, 0 },
				{ -1, -1, -1 }, });
	}

	public void kirshDFilter() {
		directionalFilter(new double[][] { { 5, 5, 5 }, { -3, 0, -3 },
				{ -3, -3, -3 }, });
	}

	public void aDFilter() {
		directionalFilter(new double[][] { { 1, 1, 1 }, { 1, -2, 1 },
				{ -1, -1, -1 }, });
	}

	public void laplacianFilter() {
		double[][] mask = new double[][] { { 0, 1, 0 }, { 1, -4, 1 },
				{ 0, 1, 0 }, };
		genericFilter(maskFilterFunction(mask));
		for (int k = 0; k < 3; k++) {
			zeroCross(0, k);
		}
	}

	public void laplacianWithGradientFilter() {
		double[][] mask = new double[][] { { 0, 1, 0 }, { 1, -4, 1 },
				{ 0, 1, 0 }, };
		genericFilter(maskFilterFunction(mask));
		for (int k = 0; k < 3; k++) {
			double epsilon = getGradient(k);
			zeroCross(epsilon * 0.25, k);
		}
	}

	public void logFilter(double sigma, int windowSize) {
		double[][] mask = getLoGMask(sigma, windowSize);
		genericFilter(maskFilterFunction(mask));
		for (int k = 0; k < 3; k++) {
			double epsilon = getGradient(k);
			zeroCross(epsilon * 0.25, k);
		}
		;
	}

	private double[][] getLoGMask(double sigma, int windowSize) {
		double m[][] = new double[windowSize][windowSize];
		int midline = (windowSize + 1) / 2;
		for (int i = 0; i < windowSize; i++) {
			for (int j = 0; j < windowSize; j++) {
				if (i < midline && j < midline) {
					m[i][j] = getDeltaG(sigma, midline - 1 - i, midline - 1 - j);
				} else if (i < midline) {
					m[i][j] = m[i][2 * midline - j - 2];
				} else {
					m[i][j] = m[2 * midline - i - 2][j];
				}
			}
		}
		return m;
	}

	private double getDeltaG(double sigma, int x, int y) {
		double xx = x * x;
		double yy = y * y;
		double sigmasigma = sigma * sigma;
		return ((xx + yy) / sigmasigma - 2)
				* Math.pow(Math.E, -(xx + yy) / (2 * sigmasigma))
				/ (Math.sqrt(2 * Math.PI) * sigma * sigmasigma);
	}

	public double getGradient(int k) {
		double lastNonZeroValue = 0;
		double max = Double.MIN_VALUE;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				double current = data[i][j][k];
				if (lastNonZeroValue * current <= 0) {
					double diff = Math.abs(lastNonZeroValue - current);
					max = max < diff ? diff : max;
				}
				lastNonZeroValue = current != 0 ? current : lastNonZeroValue;
			}
			lastNonZeroValue = 0;
		}
		for (int j = 0; j < width; j++) {
			for (int i = 0; i < height; i++) {
				double current = data[i][j][k];
				if (lastNonZeroValue * current <= 0) {
					double diff = Math.abs(lastNonZeroValue - current);
					max = max < diff ? diff : max;
				}
				lastNonZeroValue = current != 0 ? current : lastNonZeroValue;
			}
			lastNonZeroValue = 0;
		}
		return max;
	}

	public void zeroCross(double epsilon, int k) {
		double lastNonZeroValue = 0;
		double[][] m = new double[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				double current = data[i][j][k];
				if (lastNonZeroValue * current <= 0
						&& Math.abs(lastNonZeroValue - current) >= epsilon) {
					m[i][j] = 255;
				}
				lastNonZeroValue = current != 0 ? current : lastNonZeroValue;
			}
			lastNonZeroValue = 0;
		}
		for (int j = 0; j < width; j++) {
			for (int i = 0; i < height; i++) {
				double current = data[i][j][k];
				if (lastNonZeroValue * current <= 0
						&& Math.abs(lastNonZeroValue - current) >= epsilon) {
					m[i][j] = 255;
				}
				lastNonZeroValue = current != 0 ? current : lastNonZeroValue;
			}
			lastNonZeroValue = 0;
		}

		// assign
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				data[i][j][k] = m[i][j];
			}
		}
	}

	private void directionalFilter(double[][] mask) {
		if (mask.length != mask[0].length || mask.length != 3) {
			throw new IllegalArgumentException("Invalid mask dimension");
		}
		int n = mask.length;
		double[][] yMask = new double[n][n];
		double[][] d1Mask = new double[n][n];
		double[][] d2Mask = new double[n][n];
		// Rotate border pixels
		int n2 = n * n - 1;
		Point[] border = new Point[n2];
		fillBorder(border);
		d1Mask[1][1] = mask[1][1];
		d2Mask[1][1] = mask[1][1];
		yMask[1][1] = mask[1][1];
		for (int i = 0; i < n2; i++) {
			Point curr = border[i];
			Point next = border[(i + 1) % n2];
			Point prev = border[(i - 1 + n2) % n2];
			Point nextNext = border[(i + 2) % n2];
			yMask[nextNext.x][nextNext.y] = mask[curr.x][curr.y];
			d1Mask[next.x][next.y] = mask[curr.x][curr.y];
			d2Mask[prev.x][prev.y] = mask[curr.x][curr.y];
		}
		directionalFilter(mask, yMask, d1Mask, d2Mask);
	}

	private void directionalFilter(double[][] mask, double[][] yMask,
			double[][] d1Mask, double[][] d2Mask) {

		double[][][] x = newImageDataFromFilter(maskFilterFunction(mask), data);
		double[][][] y = newImageDataFromFilter(maskFilterFunction(yMask), data);
		double[][][] d1 = newImageDataFromFilter(maskFilterFunction(d1Mask), data);
		double[][][] d2 = newImageDataFromFilter(maskFilterFunction(d2Mask), data);
		double[][][] ans = new double[x.length][x[0].length][x[0][0].length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				for (int k = 0; k < x[0][0].length; k++) {
					double xx = Math.abs(x[i][j][k]);
					double yy = Math.abs(y[i][j][k]);
					double dd1 = Math.abs(d1[i][j][k]);
					double dd2 = Math.abs(d2[i][j][k]);
					ans[i][j][k] = Math.max(Math.max(xx, yy),
							Math.max(dd1, dd2));
				}
			}
		}
		data = ans;
		normalize();
	}

	private void fillBorder(Point[] border) {
		int k = 0;
		border[k++] = new Point(0, 0);
		border[k++] = new Point(0, 1);
		border[k++] = new Point(0, 2);
		border[k++] = new Point(1, 2);
		border[k++] = new Point(2, 2);
		border[k++] = new Point(2, 1);
		border[k++] = new Point(2, 0);
		border[k++] = new Point(1, 0);
	}

	public void globalUmbral() {
		for (int k = 0; k < data[0][0].length; k++) {
			double T = 0;
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[0].length; j++) {
					T += data[i][j][k];
				}
			}
			T /= data.length * data[0].length;
			double umbral = iterateGlobalUmbral(T, k);
			umbralize(umbral, k);
		}
	}

	private double iterateGlobalUmbral(double T, int k) {
		double s1 = 0, s2 = 0;
		int n1 = 0, n2 = 0;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				double color = data[i][j][k];
				if (color <= T) {
					s1 += color;
					n1++;
				} else {
					s2 += color;
					n2++;
				}
			}
		}
		double nextT = ((s1 / n1) + (s2 / n2)) * 0.5;
		if (Math.abs(nextT - T) < 0.5) {
			System.out.println("Umbral: " + nextT);
			return nextT;
		} else {
			return iterateGlobalUmbral(nextT, k);
		}
	}

	private final double EPS = 1e-9;

	/* Trata a la imagen como gris, deberiamos cambiarlo tal vez */
	public void otsuUmbral() {
		int[] histogram = ImageUtils.grayHistogram(this);
		double size = width * height;
		double[] acumSum = new double[256];
		double[] m = new double[256];
		acumSum[0] = histogram[0] / size;
		m[0] = 0;
		for (int i = 1; i < 256; i++) {
			acumSum[i] = acumSum[i - 1] + (histogram[i] / size);
			m[i] = m[i - 1] + (i * histogram[i] / size);
		}
		double maxx = -1;
		List<Integer> maxs = new LinkedList<Integer>();
		for (int i = 1; i < 255; i++) {
			double var = Math.pow(m[255] * acumSum[i] - m[i], 2)
					/ (acumSum[i] * (1 - acumSum[i]));
			if (Math.abs(maxx - var) < EPS) {
				maxs.add(i);
			} else {
				if (maxx < var) {
					maxx = var;
					maxs.clear();
					maxs.add(i);
				}
			}
		}
		double sum = 0;
		for (Integer i : maxs) {
			sum += i;
		}
		sum /= maxs.size();
		System.out.println("Umbral: " + sum);
		umbralize(sum);
	}

	public void isotropicDiffusion(int t) {
		while (t-- > 0) {
			genericFilter((data, i, j, k) -> {
				double sum = 0;
				int[] dx = new int[] { 0, 0, 1, -1 };
				int[] dy = new int[] { 1, -1, 0, 0 };
				for (int a = 0; a < 4; a++) {
					sum += getDataValue(data, dx[a] + i, dy[a] + j, k);
				}
				return sum / 4.0;
			});
		}
	}

	public void anisotropicDiffusion(int t, Function<Double, Double> f) {
		while (t-- > 0) {
			genericFilter((data, i, j, k) -> {
				double sum = 0;
				int[] dx = new int[] { 0, 0, 1, -1 };
				int[] dy = new int[] { 1, -1, 0, 0 };
				for (int a = 0; a < 4; a++) {
					double diff = getDataValue(data, dx[a] + i, dy[a] + j, k)
							- getDataValue(data, i, j, k);
					sum += f.apply(diff) * diff;
				}
				return data[i][j][k] + (sum / 4.0);
			});
		}
	}

	public void circularHoughTransformation(int radiusSteps, double epsilon) {
		double[][][] clone = clone().data;
		List<Point> whitePoints = whitePoints(0);

		int[] acum = new int[radiusSteps];

		double radius2 = Math.max(height, width) * Math.sqrt(2);
		double radius1 = 0;
		double radiusStep = radius2 / (radiusSteps - 1);

		Point center = new Point((width - 1) / 2, (height - 1) / 2);

		double currentRadius = radius1;
		int currentRadiusStep = 0;
		while (currentRadiusStep < radiusSteps) {
			for (Point p: whitePoints) {
				if (satisfiesCircularNormalEquation(p.x, p.y, currentRadius, epsilon, center)) {
					acum[currentRadiusStep]++;
				}
			}
			currentRadiusStep++;
			currentRadius += radiusStep;
		}

		int max = Auxiliar.max(acum);
		double threshold = ((double) max) * 0.75;

		currentRadius = radius1;
		currentRadiusStep = 0;
		while (currentRadiusStep < radiusSteps) {
			if (acum[currentRadiusStep] > threshold) {
				// we are using a bigger epsilon for drawing
				drawCircle(currentRadius, 100, center, clone);
//				drawCircle(currentRadius, epsilon, center, clone);
			}
			currentRadiusStep++;
			currentRadius += radiusStep;
		}

		data = clone;
	}

	private void drawCircle(double radius, double epsilon, Point center, double[][][] matrix) {
		for (int x = 0; x < matrix.length; x++) {
			for (int y = 0; y < matrix[0].length; y++) {
				if (satisfiesCircularNormalEquation(x, y, radius, epsilon, center)) {
			 		setGrayColor(x, y, 128.0, matrix);
			 	}
			}
		}
	}

	private boolean satisfiesCircularNormalEquation(int x, int y, double radius, double epsilon, Point center) {
		 return Math.abs(Math.pow(x - center.x, 2) + Math.pow(y - center.y, 2) - Math.pow(radius, 2)) < epsilon;
	}

	public void linearHoughTransformation(int titaSteps, int roSteps, double epsilon) {
		double[][][] clone = clone().data;
		List<Point> whitePoints = whitePoints(0);

		int[][] acum = new int[titaSteps][roSteps];

		double tita2 = Math.PI/2;
		double tita1 = (-1) * tita2;
		double titaStep = (tita2 - tita1) / (titaSteps - 1);

		double ro2 = Math.max(height, width) * Math.sqrt(2);
		double ro1 = (-1) * ro2;
		double roStep = (ro2 - ro1) / (roSteps - 1);

		double currentTita = tita1;
		int currentTitaStep = 0;
		while (currentTitaStep < titaSteps) {
			double currentRo = ro1;
			int currentRoStep = 0;
			while (currentRoStep < roSteps) {
				for (Point p: whitePoints) {
					if (satisfiesLineNormalEquation(p.x, p.y, currentTita, currentRo, epsilon)) {
						acum[currentTitaStep][currentRoStep]++;
					}
				}
				currentRoStep++;
				currentRo += roStep;
			}
			currentTitaStep++;
			currentTita += titaStep;
		}

		
		int max = Auxiliar.max(acum);
		double threshold = ((double) max) * 0.75;

		currentTita = tita1;
		currentTitaStep = 0;
		while (currentTitaStep < titaSteps) {
			double currentRo = ro1;
			int currentRoStep = 0;
			while (currentRoStep < roSteps) {
				if (acum[currentTitaStep][currentRoStep] > threshold) {
					drawLine(currentTita, currentRo, epsilon, clone);
				}
				currentRoStep++;
				currentRo += roStep;
			}
			currentTitaStep++;
			currentTita += titaStep;
		}

		data = clone;
	}

	private void drawLine(double tita, double ro, double epsilon, double[][][] matrix) {
		for (int x = 0; x < matrix.length; x++) {
			for (int y = 0; y < matrix[0].length; y++) {
			 	if (satisfiesLineNormalEquation(x, y, tita, ro, epsilon)) {
			 		setGrayColor(x, y, 128.0, matrix);
			 	}
			}
		}
	}

	private boolean satisfiesLineNormalEquation(int x, int y, double tita, double ro, double epsilon) {
		return Math.abs(ro - x * Math.cos(tita) - y * Math.sin(tita)) < epsilon;
	}

	private List<Point> whitePoints(int k) {
		List<Point> whitePoints = new ArrayList<>();
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				if (data[i][j][k] == 255.0) {
					whitePoints.add(new Point(i, j));
				}
			}
		}
		return whitePoints;
	}

	public void susanBorderDetector() {
		susanDetector(true);
	}
	
	public void susanCornerDetector() {
		susanDetector(false);
	}
	
	public void susanDetector(boolean border) {
		double[][][] clone = clone().data;
		double[][] mask = new double[][]{
			{0, 0, 1.0, 1.0, 1.0, 0, 0},
			{0, 1.0, 1.0, 1.0, 1.0, 1.0, 0},
			{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
			{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
			{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
			{0, 1.0, 1.0, 1.0, 1.0, 1.0, 0},
			{0, 0, 1.0, 1.0, 1.0, 0, 0}
		};
		int halfMaskSize = (int) Math.floor(mask.length / 2);
		int N = 37;
		int t = 27;
		double epsilon = 0.15;
		double sBorderValue = 0.5;
		double sCornerValue = 0.75;
		int pixelSameGrayAmountInMask;
		double s;
		
		for (int i = halfMaskSize; i < data.length - halfMaskSize; i++) {
			for (int j = halfMaskSize; j < data[0].length - halfMaskSize; j++) {
				pixelSameGrayAmountInMask = pixelSameGrayAmountInMask(i, j, t, mask);
				s = 1 - ((double) pixelSameGrayAmountInMask) / N;
				if ((border && (Math.abs(s - sBorderValue) < epsilon)) ||
					(!border && (Math.abs(s - sCornerValue) < epsilon))) {
					setGrayColor(i, j, 0, clone);
					clone[i][j][0] = 255.0;
				} else {
					setRGB(i, j, getRGB(i, j), clone);
				}
			}
		}
		data = clone;
	}
	
	private int pixelSameGrayAmountInMask(int x, int y, int t, double[][] mask) {
		int count = 0;
		int halfMaskSize = (int) Math.floor(mask.length / 2);
		
		for (int i = -halfMaskSize; i < halfMaskSize + 1; i++) {
			for (int j = -halfMaskSize; j < halfMaskSize + 1; j++) {
				if (Math.abs(getGray(x, y)
						- mask[i + halfMaskSize][j + halfMaskSize] * getGray(x + i, y + j)) < t) {
					count++;
				}
			}
		}
		return count;
	}
	
	public void cannyBorderDetector(int windowSize, double sigma, double t1, double t2) {
		gaussianFilter(windowSize, sigma);
		Derivates d = sobelFilter();
		double[][][] copy = clone().data;
		for (int k = 0; k < copy[0][0].length; k++) {
			// aca va desde 1 y hasta length - 1 para que no pase un IndexOutOfBounds
			// se re puede mejorar eso
			for (int i = 1; i < copy.length - 1; i++) {
				for (int j = 1; j < copy[0].length - 1; j++) {
					Point direction = cannyBorderDirection(d.dy[i][j][k], d.dx[i][j][k]);
					
					if (copy[i][j][k] < copy[i + direction.x][j + direction.y][k]) {
						data[i][j][k] = 0;
					} else if (copy[i][j][k] <= copy[i - direction.x][j - direction.y][k]) {
						data[i][j][k] = 0;
					}
				}
			}
		}
		
		List<Point> lateCheckPoints;
		for (int k = 0; k < data[0][0].length; k++) {
			lateCheckPoints = new LinkedList<>();
			// aca va desde 1 y hasta length - 1 para que no pase un IndexOutOfBounds
			// se re puede mejorar eso
			for (int i = 1; i < data.length - 1; i++) {
				for (int j = 1; j < data[0].length - 1; j++) {
					if (data[i][j][k] > t2) {
						data[i][j][k] = 255.0;
						lateCheckPoints.add(new Point(i, j));
					} else if (data[i][j][k] < t1) {
						data[i][j][k] = 0.0;
					} else {
					}
				}
			}
			
			int[] dx = { 0, 0, 1, 1, 1,-1,-1,-1};
			int[] dy = { 1,-1, 0, 1,-1, 0, 1,-1};
			
			// Analizo si algun vecino de algun borde no está en 0,
			// entonces ahora ese punto tambien es borde.
			while(!lateCheckPoints.isEmpty()){
				Point p = lateCheckPoints.remove(0);
				for(int i = 0; i<dx.length; i++){
					int di = p.x+dx[i];
					int dj = p.y+dy[i];
					if( 0<=di && di<data.length &&
						0<=dj && dj<data[0].length){
						double value = getDataValue(di,dj,k);
						if(0<value && value<255){
							data[di][dj][k] = 255;
							lateCheckPoints.add(new Point(di,dj));
						}
					}
				}
			} 
			map(v -> (v<255.0?0:255.0),k);
		}
	}

	private Point cannyBorderDirection(double dxVal,double dyVal) {
		if (dxVal == 0) {
			return new Point(0, 1);
		}
		double angle = Math.atan2(dyVal, dxVal) * 180.0 / Math.PI;
		angle += angle < 0 ? 180 : 0;

		if (angle < 22.5 || angle > 157.5) {
			return new Point(1, 0);
		} else if (angle < 67.5){
			return new Point(1, 1);
		} else if (angle < 112.5){
			return new Point(0, 1);
		} else {
			return new Point(1, -1);
		}
	}

	private class Derivates {
		double[][][] dx;
		double[][][] dy;

		public Derivates(double[][][] dx, double[][][] dy) {
			this.dx = dx;
			this.dy = dy;
		}
	}
	
	private boolean shouldBeIn(Point p, double[] t1){
		double[] rgb = getRGBArray(p.x, p.y);
		double num = Auxiliar.norm(rgb, t1);
		double den = 256;
		double pr = 1 - num/den;
		
		return pr > 0.75;
	}
	
	public State.LevelSetsInfo levelSets(int x1, int x2, int y1, int y2){
		
		List<Point> insideBorder, outsideBorder;
		int[][] theta = new int[height][width];
		insideBorder = new LinkedList<Point>();
		outsideBorder = new LinkedList<Point>();
		
		addBorders(insideBorder, x1, x2, y1, y2);
		addBorders(outsideBorder, x1-1, x2+1, y1-1, y2+1);
		fillValues(theta, 0, width-1, 0, height-1, 3);
		fillValues(theta, x1, x2, y1, y2, -3);
		fillValues(theta, insideBorder, -1);
		fillValues(theta, outsideBorder, 1);
		double[] t1 = avg(theta, -3, -1);
		
		return levelSets(new LevelSetsInfo(theta, t1),  true);
	}
	
	
	
	public State.LevelSetsInfo levelSets(LevelSetsInfo info, boolean print){

		long time = System.currentTimeMillis();
		int Na = Math.min(width, height);
		int Ns = 5;
		List<Point> lin = new LinkedList<Point>();
		Auxiliar.find(lin, info.theta, -1);
		List<Point> lout = new LinkedList<Point>();
		Auxiliar.find(lout, info.theta, 1);
		// Primer ciclo
		boolean changes = true;
		for(int t=0; t<Na && changes; t++){
			changes = false;
			changes|=stepCycle(lout,lin,
					p->shouldBeIn(p, info.t),
					3,1,-1,-3,info.theta);
			changes|=stepCycle(lin,lout,
					p->!shouldBeIn(p, info.t),
					-3,-1,1,3,info.theta);
		}
		FilterFunction2 ff = gaussianFilterFunction2(Ns, 1);
		for(int t=0; t<Ns; t++){
			System.out.println(t);
			double[][] gTheta = newImageDataFromFilter(ff, info.theta);
			changes|=stepCycle(lout,lin,
					p->gTheta[p.x][p.y]*info.theta[p.x][p.y]<0,
					3,1,-1,-3,info.theta);
			changes|=stepCycle(lin,lout,
					p->gTheta[p.x][p.y]*info.theta[p.x][p.y]<0,
					-3,-1,1,3,info.theta);
		}
		if(print){
			for(Point p : lin){
				int i = p.x;
				int j = p.y;
				setGrayColor(i, j, 0);
				setOnlyR(i, j, 255);
			}
			for(Point p : lout){
				int i = p.x;
				int j = p.y;
				setGrayColor(i, j, 0);
				setOnlyB(i, j, 255);
			}
		}

		System.out.println("time: "+(System.currentTimeMillis()-time));
		return info;
	}
	
	private static boolean stepCycle(List<Point> list1, List<Point> list2, Function<Point, Boolean> func,
			int ext1, int b1, int b2, int ext2, int[][] theta) {
		
		int[] dx = new int[]{0,0,1,-1};
		int[] dy = new int[]{1,-1,0,0};
		List<Point> toAdd = new LinkedList<Point>();
		boolean change = false;
		
		for(int i=0; i<list1.size(); i++){
			Point p = list1.get(i);
			if( func.apply(p)){
				change = true;
				list1.remove(i);
				i--;
				theta[p.x][p.y] = b2;
				list2.add(p);
				for(int d=0; d<4; d++){
					int x = p.x+dx[d];
					int y = p.y+dy[d];
					if( 0<=x && x<theta.length &&
						0<=y && y<theta[0].length && theta[x][y]==ext1){
							theta[x][y]=b1;
							toAdd.add(new Point(x,y));
					}
				}
			}
		}
		removeNotBorderPoint(list2, theta, b1, ext2);
		list1.addAll(toAdd);
		return change;
	}

	private static void removeNotBorderPoint(List<Point> removable, int[][] theta, int expectedNeigbour, int replaceValue){
		int[] dx = new int[]{0,0,1,-1};
		int[] dy = new int[]{1,-1,0,0};
		Iterator<Point> it = removable.iterator();
		while(it.hasNext()){
			Point p = it.next();
			boolean found = false;
			for(int d=0; d<4 && !found; d++){
				int x = p.x+dx[d];
				int y = p.y+dy[d];
				if( 0<=x && x<theta.length &&
					0<=y && y<theta[0].length && theta[x][y]==expectedNeigbour){
						found = true;
				}
			}
			if(!found){
				it.remove();
				theta[p.x][p.y] = replaceValue;
			}
		}
	}
	
	private double[] avg(int[][] mat, int v1, int v2){
		double r=0;
		double b=0;
		double g=0;
		int q = 0;
		for(int i=0; i<mat.length; i++){
			for(int j=0; j<mat[0].length; j++){
				if(mat[i][j]==v1 || mat[i][j]==v2){
					q++;
					r += getOnlyR(i, j);
					g += getOnlyG(i, j);
					b += getOnlyB(i, j);
				}
			}
		}
		return new double[]{r/q, g/q, b/q};
	}
	
	private void fillValues(int[][] mat, List<Point> list, int value){
		for(Point p : list){
			int i = p.x;
			int j = p.y;
			mat[i][j] = value;
		}
	}
	
	private void fillValues(int[][] mat, int x1, int x2, int y1, int y2, int value){
		for(int i=x1; i<=x2; i++){
			for(int j=y1; j<=y2; j++){
				mat[j][i] = value;
			}
		}
	}
	
	private void addBorders(List<Point> list, int x1, int x2, int y1, int y2){
		for(int i=x1; i<=x2; i++){
			list.add(new Point(y1, i));
			list.add(new Point(y2, i));
		}
		for(int i=y1+1; i<=y2-1; i++){
			list.add(new Point(i, x1));
			list.add(new Point(i, x2));
		}
	}
	
}
