package dnv.ati.util;

public class ConversionUtils {

	
	public static int doubleToRGBInt(double dr, double dg, double db){
		int r = ((int)dr) & 0xFF;
		int g = ((int)dg) & 0xFF;
		int b = ((int)db) & 0xFF;
		return (r<<16)+(g<<8)+b;
	}
}
