package dnv.ati.ui;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dnv.ati.model.Image;

public class Window extends JFrame{

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final int DRAWING_WIDTH = 100;
	public static final int DRAWING_HEIGHT = 100;
	
	public Window(){
		super("ATI");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		MenuBar menuBar = new MenuBar();
		Canvas canvas = new Canvas();
		menuBar.setCanvas(canvas);
		setJMenuBar(menuBar);

		
		add(canvas);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
}
