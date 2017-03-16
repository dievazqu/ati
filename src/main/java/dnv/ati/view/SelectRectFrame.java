package dnv.ati.view;

import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dnv.ati.model.State;
import dnv.ati.util.Auxiliar;

public class SelectRectFrame extends JFrame {

	public SelectRectPanel panel;

	
	public SelectRectFrame(State state, Point start, Point end){
		this(state);
		panel.xiPositionText.setText(String.valueOf(start.x));
		panel.yiPositionText.setText(String.valueOf(start.y));
		panel.xfPositionText.setText(String.valueOf(end.x));
		panel.yfPositionText.setText(String.valueOf(end.y));
	}
	
	public SelectRectFrame(State state){
		super("Seleccion de region");
		setSize(450, 350);
		setLocationRelativeTo(null);
		setVisible(true);
		panel = new SelectRectPanel(state);
		add(panel);
	}
	
	private static class SelectRectPanel extends JPanel{

		private Point pixelSelected;
		private JFormattedTextField xiPositionText;
		private JFormattedTextField yiPositionText;
		private JFormattedTextField xfPositionText;
		private JFormattedTextField yfPositionText;
		private State state; 
		
		public SelectRectPanel(State state){
			setLayout(null);
			this.state = state;
			JLabel positionLabel = new JLabel("Region seleccionada (xi, yi, xf, yf):");
			positionLabel.setBounds(20, 20, 300, 20);
			add(positionLabel);
			xiPositionText = new JFormattedTextField(new Integer(0));
			xiPositionText.setBounds(20, 50, 40, 30);
			add(xiPositionText);
			yiPositionText = new JFormattedTextField(new Integer(0));
			yiPositionText.setBounds(70, 50, 40, 30);
			add(yiPositionText);
			xfPositionText = new JFormattedTextField(new Integer(0));
			xfPositionText.setBounds(120, 50, 40, 30);
			add(xfPositionText);
			yfPositionText = new JFormattedTextField(new Integer(0));
			yfPositionText.setBounds(170, 50, 40, 30);
			add(yfPositionText);
			JButton openNewImageButton = new JButton("Abrir en nueva imagen");
			openNewImageButton.setBounds(20, 100, 200, 30);
			openNewImageButton.addActionListener(ee -> {
				try{
					int xi = Integer.parseInt(xiPositionText.getText());
					int yi = Integer.parseInt(yiPositionText.getText());
					int xf = Integer.parseInt(xfPositionText.getText());
					int yf = Integer.parseInt(yfPositionText.getText());
					Auxiliar.sortPoint((p, q) -> {
						new AppFrame(state.getImage().copy(p.x, p.y, q.x, q.y));
					}, new Point(xi, yi), new Point(xf, yf));
					
				}catch(Exception e){
					e.printStackTrace();
				}
			});
			add(openNewImageButton);
		}
		
		
		
		
		
		
	}
	
	
	
}
