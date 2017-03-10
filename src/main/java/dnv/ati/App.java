package dnv.ati;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import dnv.ati.view.Window;



/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	new Window();
    	/*
    	BufferedImage img = null;
    	try {
    	    img = ImageIO.read(new File("images/blanco.bmp"));
    	    System.out.println(img.getHeight());
    	    System.out.println(img.getWidth());
    	    System.out.println(String.format("%h", img.getRGB(0, 0)));
    	    
    	    
    	} catch (IOException e) {
    		System.out.println("Exception");
    	}*/
    }
}
