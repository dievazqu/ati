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
	
}
