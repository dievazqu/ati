package dnv.ati.view.selectionFrames;

import java.awt.Point;
import java.util.function.BiConsumer;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dnv.ati.model.Image;
import dnv.ati.model.State;
import dnv.ati.util.Auxiliar;
import dnv.ati.view.AppFrame;


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
			
			JButton averageButton = new JButton("Calcular promedios");
			averageButton.setBounds(20, 150, 200, 30);
			averageButton.addActionListener(e -> {
				fromPoints((p,q) -> {
					Image img = state.getImage();
					Auxiliar.sortPoint((p1, p2) ->{
						//TODO: Mostrar los promedios en el frame.
						System.out.println(img.averageR(p1.x, p1.y, p2.x, p2.y));
						System.out.println(img.averageG(p1.x, p1.y, p2.x, p2.y));
						System.out.println(img.averageB(p1.x, p1.y, p2.x, p2.y));
						System.out.println(img.averageGray(p1.x, p1.y, p2.x, p2.y));
					}, p, q);	
				});
			});
			add(averageButton);
		}
		private void fromPoints(BiConsumer<Point, Point> consumer){
			int xi = Integer.parseInt(xiPositionText.getText());
			int yi = Integer.parseInt(yiPositionText.getText());
			int xf = Integer.parseInt(xfPositionText.getText());
			int yf = Integer.parseInt(yfPositionText.getText());
			consumer.accept(new Point(xi, yi), new Point(xf, yf));
		}
		
		
		
		
		
	}
	
	
	
}
