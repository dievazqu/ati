package dnv.ati.view.util;

import java.util.function.BiFunction;

public final class AnisotropicFunctions {
	public static final BiFunction<Double, Double, Double> leclercFunction = new BiFunction<Double, Double, Double>() {
		public Double apply(Double sigma, Double x) {
			double xOverSigma = (Math.abs(x) / sigma);
			return Math.pow(Math.E, -Math.pow(xOverSigma, 2));
		}
	};

	public static final BiFunction<Double, Double, Double> lorentzianFunction = new BiFunction<Double, Double, Double>() {
		public Double apply(Double sigma, Double x) {
			double xOverSigma = (Math.abs(x) / sigma);
			return 1.0 / (Math.pow(xOverSigma, 2) + 1);
		}
	};
}
