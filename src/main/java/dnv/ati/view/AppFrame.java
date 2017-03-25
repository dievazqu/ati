package dnv.ati.view;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import dnv.ati.model.Image;
import dnv.ati.model.State;

@SuppressWarnings("serial")
public class AppFrame extends JFrame{

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	public AppFrame(Image image){
		this();
		state.setImage(image);
	}
	
	private State state;
	
	public AppFrame(){
		super("ATI");
		state = new State();
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		MenuBar menuBar = new MenuBar(state);
		Canvas canvas = new Canvas(state);
		setJMenuBar(menuBar);
		
		JScrollPane scrollPanel = new JScrollPane(canvas);
		add(scrollPanel);
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
