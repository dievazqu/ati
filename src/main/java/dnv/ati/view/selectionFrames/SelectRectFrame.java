package dnv.ati.view.selectionFrames;

import java.awt.Point;
import java.util.function.BiConsumer;

import javax.print.attribute.standard.OutputDeviceAssigned;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dnv.ati.model.Image;
import dnv.ati.model.State;
import dnv.ati.util.Auxiliar;
import dnv.ati.view.AppFrame;

public class SelectRectFrame extends JFrame {

	public SelectRectPanel panel;

	public SelectRectFrame(State state, Point start, Point end) {
		this(state);
		panel.xiPositionText.setText(String.valueOf(start.x));
		panel.yiPositionText.setText(String.valueOf(start.y));
		panel.xfPositionText.setText(String.valueOf(end.x));
		panel.yfPositionText.setText(String.valueOf(end.y));
	}

	public SelectRectFrame(State state) {
		super("Seleccion de region");
		setSize(320, 370);
		setLocationRelativeTo(null);
		setVisible(true);
		panel = new SelectRectPanel(state);
		add(panel);
	}

	private static class SelectRectPanel extends JPanel {

		private Point pixelSelected;
		private JFormattedTextField xiPositionText;
		private JFormattedTextField yiPositionText;
		private JFormattedTextField xfPositionText;
		private JFormattedTextField yfPositionText;
		private State state;

		public SelectRectPanel(State state){
			setLayout(null);
			this.state = state;
			Image img = state.getImage();
			JLabel positionLabel = new JLabel("Region seleccionada (xi, yi, xf, yf):");
			positionLabel.setBounds(50, 20, 250, 20);
			add(positionLabel);
			xiPositionText = new JFormattedTextField(new Integer(0));
			xiPositionText.setBounds(50, 50, 40, 30);
			add(xiPositionText);
			yiPositionText = new JFormattedTextField(new Integer(0));
			yiPositionText.setBounds(100, 50, 40, 30);
			add(yiPositionText);
			xfPositionText = new JFormattedTextField(img==null?0:(img.getWidth()-1));
			xfPositionText.setBounds(150, 50, 40, 30);
			add(xfPositionText);
			yfPositionText = new JFormattedTextField(img==null?0:(img.getHeight()-1));
			yfPositionText.setBounds(200, 50, 40, 30);
			add(yfPositionText);
			
			JButton openNewImageButton = new JButton("Abrir en nueva imagen");
			openNewImageButton.setBounds(40, 100, 220, 30);
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

			JLabel pixelAmountLabel = new JLabel("");
			pixelAmountLabel.setBounds(40, 250, 220, 30);
			add(pixelAmountLabel);
			
			JLabel averageDisplayLabel = new JLabel("");
			averageDisplayLabel.setBounds(40, 280, 220, 30);
			add(averageDisplayLabel);
			
			JButton averageButton = new JButton("Calcular promedios de grises");
			averageButton.setBounds(40, 150, 220, 30);
			averageButton.addActionListener(e -> {
				fromPoints((p,q) -> {
					Auxiliar.sortPoint((p1, p2) ->{
						pixelAmountLabel.setText("Cantidad de pixeles: "+((p2.x-p1.x)*(p2.y-p1.y)));
						averageDisplayLabel.setText("Promedio Gris: "+String.format("%.2f", img.averageGray(p1.x, p1.y, p2.x, p2.y)));
					}, p, q);	
				});
			});
			add(averageButton);
			JButton averageColorButton = new JButton("Calcular promedios de colores");
			averageColorButton.setBounds(40, 200, 220, 30);
			averageColorButton.addActionListener(e -> {
				fromPoints((p,q) -> {
					Auxiliar.sortPoint((p1, p2) ->{
						pixelAmountLabel.setText("Cantidad de pixeles: "+((p2.x-p1.x)*(p2.y-p1.y)));
						averageDisplayLabel.setText("Promedios: R: "+String.format("%.2f", img.averageR(p1.x, p1.y, p2.x, p2.y))
								+ " G: " +String.format("%.2f", img.averageG(p1.x, p1.y, p2.x, p2.y))
								+ " B: " +String.format("%.2f", img.averageB(p1.x, p1.y, p2.x, p2.y)));
					}, p, q);	
				});
			});
			add(averageColorButton);
		}

		private void fromPoints(BiConsumer<Point, Point> consumer) {
			int xi = Integer.parseInt(xiPositionText.getText());
			int yi = Integer.parseInt(yiPositionText.getText());
			int xf = Integer.parseInt(xfPositionText.getText());
			int yf = Integer.parseInt(yfPositionText.getText());
			consumer.accept(new Point(xi, yi), new Point(xf, yf));
		}

	}

}
