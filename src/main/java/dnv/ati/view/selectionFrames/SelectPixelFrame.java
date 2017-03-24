package dnv.ati.view.selectionFrames;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import dnv.ati.model.Image;
import dnv.ati.model.State;
import dnv.ati.util.ConversionUtils;

public class SelectPixelFrame extends JFrame {

	public SelectPixelPanel panel;
	private State state;
	
	public SelectPixelFrame(State state, Point click){
		this(state);
		panel.xPositionText.setText(String.valueOf(click.x));
		panel.yPositionText.setText(String.valueOf(click.y));
		panel.selectPixel();
	}
	
	public SelectPixelFrame(State state){
		super("Seleccion de pixel");
		setSize(500, 400);
		setLocationRelativeTo(null);
		setVisible(true);
		panel = new SelectPixelPanel(state);
		add(panel);
	}
	
	private static class SelectPixelPanel extends JPanel{

		private Point pixelSelected;
		private JFormattedTextField xPositionText;
		private JFormattedTextField yPositionText;
		private JSlider rValueSlider;
		private JSlider gValueSlider;
		private JSlider bValueSlider;
		private State state;
		
		public SelectPixelPanel(State state){
			this.state = state;
			setLayout(null);
			JLabel positionLabel = new JLabel("Posicion del pixel seleccionado (x,y):");
			positionLabel.setBounds(20, 20, 300, 20);
			add(positionLabel);
			xPositionText = new JFormattedTextField(new Integer(0));
			xPositionText.setBounds(70, 50, 40, 30);
			xPositionText.setToolTipText("El valor de la primer coordenada del pixel");
			add(xPositionText);
			yPositionText = new JFormattedTextField(new Integer(0));
			yPositionText.setBounds(130, 50, 40, 30);
			yPositionText.setToolTipText("El valor de la segunda coordenada del pixel");
			add(yPositionText);
			JButton selectPixelButton = new JButton("Seleccionar Pixel");
			selectPixelButton.setBounds(20, 100, 200, 30);
			
			
			JLabel valueLabel = new JLabel("Valor del pixel seleccionado (r,g,b):");
			valueLabel.setBounds(20, 160, 300, 20);
			add(valueLabel);
			rValueSlider = new JSlider(0, 255);
			rValueSlider.setBounds(30, 190, 160, 20);
			rValueSlider.addChangeListener(e -> repaint());
			add(rValueSlider);
			gValueSlider = new JSlider(0, 255);
			gValueSlider.setBounds(30, 220, 160, 20);
			gValueSlider.addChangeListener(e -> repaint());
			add(gValueSlider);
			bValueSlider = new JSlider(0, 255);
			bValueSlider.setBounds(30, 250, 160, 20);
			bValueSlider.addChangeListener(e -> repaint());
			add(bValueSlider);
			JButton editPixelButton = new JButton("Modificar Pixel");
			editPixelButton.setBounds(20, 280, 200, 30);
			editPixelButton.addActionListener(ee -> {
				try{
					if(pixelSelected!=null){
						editPixel();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			});
			add(editPixelButton);
			
			selectPixelButton.addActionListener(ee -> {
				try{
					selectPixel();
				}catch(Exception e){
					e.printStackTrace();
				}
			});
			add(selectPixelButton);
			
			JLabel colorLabel = new JLabel("Color del pixel seleccionado:");
			colorLabel.setBounds(280, 20, 200, 20);
			add(colorLabel);
			
			JLabel nextColorLabel = new JLabel("Nuevo color del pixel:");
			nextColorLabel.setBounds(300, 160, 200, 20);
			add(nextColorLabel);
		}
		
		private void selectPixel(){
			int x = Integer.parseInt(xPositionText.getText());
			int y = Integer.parseInt(yPositionText.getText());
			int rgb = state.getImage().getRGB(y, x);
			pixelSelected = new Point(x,y);
			rValueSlider.setValue((rgb & 0x0FF0000) >> 16);
			gValueSlider.setValue((rgb & 0x000FF00) >> 8);
			bValueSlider.setValue((rgb & 0x00000FF));
			repaint();
		}
		
		private void editPixel(){
			Image img = state.getImage();
			int r = rValueSlider.getValue();
			int g = gValueSlider.getValue();
			int b = bValueSlider.getValue();
			img.setRGB(pixelSelected.y, pixelSelected.x, ConversionUtils.doubleToRGBInt(r, g, b));
			state.notifyImageChanged(img);
			repaint();
		}
		
		@Override
		public void paint(Graphics gg) {
			super.paint(gg);
			if(pixelSelected!=null){
				Image img = state.getImage();
				gg.setColor(new Color(img.getRGB(pixelSelected.y, pixelSelected.x)));
				gg.fillRect(340, 50, 40, 40);
				gg.setColor(Color.BLACK);
				int r = rValueSlider.getValue();
				gg.drawString(String.valueOf(r), 200, 200);
				int g = gValueSlider.getValue();
				gg.drawString(String.valueOf(g), 200, 230);
				int b = bValueSlider.getValue();
				gg.drawString(String.valueOf(b), 200, 260);
				gg.setColor(new Color(ConversionUtils.doubleToRGBInt(r, g, b)));
				gg.fillRect(340, 190, 40, 40);
			}
		}
	}
	
	
	
}
