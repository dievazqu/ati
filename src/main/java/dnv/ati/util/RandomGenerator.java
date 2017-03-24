package dnv.ati.util;

import java.util.Random;

public class RandomGenerator {
	
	private static Random random = new Random();
	
	public static double generateUniformNumber(double min, double max){
		return random.nextDouble()*(max-min)+min;
	}
	
	public static double generateGaussNumber(double mean, double std){
		return random.nextGaussian()*std+mean;
	}
	
	public static double generateExponencialNumber(double lambda){
		return - 1.0/lambda * Math.log(1 - random.nextDouble());
	}
	
	public static double generateRayleighNumber(double phi){
		return phi * Math.sqrt(-2 * Math.log(1 - random.nextDouble()));
	}
	
	
}
