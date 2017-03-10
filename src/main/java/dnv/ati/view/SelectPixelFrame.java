package dnv.ati.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dnv.ati.model.Image;
import dnv.ati.model.State;
import dnv.ati.util.ConversionUtils;

public class SelectPixelFrame extends JFrame {

	
	public SelectPixelFrame(){
		super("Seleccion de pixel");
		setSize(450, 350);
		setLocationRelativeTo(null);
		setVisible(true);
		add(new SelectPixelPanel());
	}
	
	private static class SelectPixelPanel extends JPanel{

		Point pixelSelected;
		
		public SelectPixelPanel(){
			setLayout(null);
			JLabel positionLabel = new JLabel("Posicion del pixel seleccionado (x,y):");
			positionLabel.setBounds(20, 20, 300, 20);
			add(positionLabel);
			JFormattedTextField xPositionText = new JFormattedTextField(new Integer(0));
			xPositionText.setBounds(70, 50, 40, 30);
			xPositionText.setToolTipText("El valor de la primer coordenada del pixel");
			add(xPositionText);
			JFormattedTextField yPositionText = new JFormattedTextField(new Integer(0));
			yPositionText.setBounds(130, 50, 40, 30);
			yPositionText.setToolTipText("El valor de la segunda coordenada del pixel");
			add(yPositionText);
			JButton selectPixelButton = new JButton("Seleccionar Pixel");
			selectPixelButton.setBounds(20, 100, 200, 30);
			
			
			JLabel valueLabel = new JLabel("Valor del pixel seleccionado (r,g,b):");
			valueLabel.setBounds(20, 160, 300, 20);
			add(valueLabel);
			JFormattedTextField rValueText = new JFormattedTextField(new Integer(-1));
			rValueText.setBounds(40, 190, 40, 30);
			add(rValueText);
			JFormattedTextField gValueText = new JFormattedTextField(new Integer(-1));
			gValueText.setBounds(100, 190, 40, 30);
			add(gValueText);
			JFormattedTextField bValueText = new JFormattedTextField(new Integer(-1));
			bValueText.setBounds(160, 190, 40, 30);
			add(bValueText);
			JButton editPixelButton = new JButton("Modificar Pixel");
			editPixelButton.setBounds(20, 240, 200, 30);
			editPixelButton.addActionListener(ee -> {
				try{
					if(pixelSelected!=null){
						Image img = State.getInstance().getImage();
						int r = Integer.parseInt(rValueText.getText());
						int g = Integer.parseInt(gValueText.getText());
						int b = Integer.parseInt(bValueText.getText());
						img.setRGB(pixelSelected.y, pixelSelected.x, ConversionUtils.doubleToRGBInt(r, g, b));
						State.getInstance().notifyImageChanged();
						repaint();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			});
			add(editPixelButton);
			
			selectPixelButton.addActionListener(ee -> {
				try{
					int x = Integer.parseInt(xPositionText.getText());
					int y = Integer.parseInt(yPositionText.getText());
					int rgb = State.getInstance().getImage().getRGB(y, x);
					pixelSelected = new Point(x,y);
					rValueText.setText(String.valueOf((rgb & 0x0FF0000) >> 16));
					gValueText.setText(String.valueOf((rgb & 0x000FF00) >> 8));
					bValueText.setText(String.valueOf( (rgb & 0x00000FF)));
					repaint();
				}catch(Exception e){
					e.printStackTrace();
				}
			});
			add(selectPixelButton);
		}
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			if(pixelSelected!=null){
				Image img = State.getInstance().getImage();
				g.setColor(new Color(img.getRGB(pixelSelected.y, pixelSelected.x)));
				g.fillRect(330, 20, 40, 40);
			}
		}
	}
	
	
	
}
