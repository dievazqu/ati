package dnv.ati.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

public class AppFrame extends JFrame{

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	public AppFrame(){
		super("ATI");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		MenuBar menuBar = new MenuBar();
		Canvas canvas = new Canvas();

		setJMenuBar(menuBar);
		
		JScrollPane scrollPanel = new JScrollPane(canvas);
		add(scrollPanel);
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
