package dnv.ati.ui;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

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
		
		JScrollPane jScrollPane = new JScrollPane(canvas);
		// only a configuration to the jScrollPane...
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// Then, add the jScrollPane to your frame
		getContentPane().add(jScrollPane);
		
		setLocationRelativeTo(null);
		setVisible(true);
		
	}
	
}
