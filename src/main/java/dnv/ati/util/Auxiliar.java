package dnv.ati.util;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Auxiliar {

	private static final double EPS = 1e-9;
	
	public static void sortPoint(BiConsumer<Point, Point> consumer, Point p, Point q){
		int minx = (int)Math.min(p.getX(), q.getX());
		int maxx = (int)Math.max(p.getX(), q.getX());
		int miny = (int)Math.min(p.getY(), q.getY());
		int maxy = (int)Math.max(p.getY(), q.getY());
		consumer.accept(new Point(minx, miny), new Point(maxx, maxy));
	}
	
	public interface TriFunction<T,U,V,W>{
		public W apply(T t, U u, V v);
	}
	
	public static double[][] merge(double[][] mat1, double[][] mat2, double[][] mat3, TriFunction<Double, Double, Double, Double> f){
		int a = mat1.length;
		int b = mat1[0].length;
		double[][] aux = new double[a][b];
		for(int i=0; i<a; i++){
			for(int j=0; j<b; j++){
				aux[i][j] = f.apply(mat1[i][j], mat2[i][j], mat3[i][j]);
			}
		}
		return aux;
	}
	
	
	public static double[][] merge(double[][] mat1, double[][] mat2, BiFunction<Double, Double, Double> f){
		int a = mat1.length;
		int b = mat1[0].length;
		double[][] aux = new double[a][b];
		for(int i=0; i<a; i++){
			for(int j=0; j<b; j++){
				aux[i][j] = f.apply(mat1[i][j], mat2[i][j]);
			}
		}
		return aux;
	}
	
	public static void map(double[][] mat, Function<Double, Double> f){
		for(int i=0; i<mat.length; i++){
			for(int j=0; j<mat[0].length; j++){
					mat[i][j] = f.apply(mat[i][j]);
			}
		}
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
	
	public static double min(double[][] matrix) {
		double min = Double.MAX_VALUE;
		double current;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				current = matrix[i][j];
				min = min > current ? current : min;
			}
		}
		return min;
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
	
	public static double max(double[][] matrix) {
		double max = Double.MIN_VALUE;
		double current;
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
	
	public static void find(List<Point> list, double[][] mat, int value){
		for(int i=0; i<mat.length; i++){
			for(int j=0; j<mat[0].length; j++){
				if(Math.abs(mat[i][j]-value)<EPS)
					list.add(new Point(i,j));
			}
		}
	}
	
	public static List<Point> find(double[][] mat, int value){
		List<Point> l = new LinkedList<Point>();
		for(int i=0; i<mat.length; i++){
			for(int j=0; j<mat[0].length; j++){
				if(Math.abs(mat[i][j]-value)<EPS)
					l.add(new Point(i,j));
			}
		}
		return l;
	}
}
