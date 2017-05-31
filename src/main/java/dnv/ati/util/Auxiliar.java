package dnv.ati.util;

import java.awt.Point;
import java.util.function.BiConsumer;

public class Auxiliar {

	public static void sortPoint(BiConsumer<Point, Point> consumer, Point p, Point q){
		int minx = (int)Math.min(p.getX(), q.getX());
		int maxx = (int)Math.max(p.getX(), q.getX());
		int miny = (int)Math.min(p.getY(), q.getY());
		int maxy = (int)Math.max(p.getY(), q.getY());
		consumer.accept(new Point(minx, miny), new Point(maxx, maxy));
	}
	
	public static double norm(double[] a, double[] b){
		if(a.length != b.length)
			throw new IllegalArgumentException();
		double sum = 0;
		for(int i=0; i<a.length; i++){
			double diff = a[i]-b[i];
			sum+=(diff*diff);
		}
		return Math.sqrt(sum);
	}

	public static int max(int[][] matrix) {
		int max = Integer.MIN_VALUE;
		int current;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				current = matrix[i][j];
				max = max < current ? current : max;
			}
		}
		return max;
	}

	public static int max(int[] matrix) {
		int max = Integer.MIN_VALUE;
		int current;
		for (int i = 0; i < matrix.length; i++) {
			current = matrix[i];
			max = max < current ? current : max;
		}
		return max;
	}
}
