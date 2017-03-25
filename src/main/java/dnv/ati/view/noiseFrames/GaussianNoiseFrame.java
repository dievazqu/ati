package dnv.ati.view.noiseFrames;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import dnv.ati.model.Image;
import dnv.ati.model.State;
import dnv.ati.util.ImageUtils;

@SuppressWarnings("serial")
public class GaussianNoiseFrame extends JFrame {

	public GaussianNoiseFrame(State state){
		super("Ruido gaussiano");
		setSize(310, 230);
		setLayout(null);
		setLocationRelativeTo(null);
		setVisible(true);
		JLabel percentageLabel = new JLabel("Porcentaje de pixeles a modificar:");
		percentageLabel.setBounds(20, 10, 200, 30);
		add(percentageLabel);
		JLabel sliderLabel = new JLabel("50%");
		sliderLabel.setBounds(240, 50, 40, 20);
		add(sliderLabel);
		JSlider slider = new JSlider(0, 100);
		slider.setBounds(20, 50, 200, 20);
		slider.addChangeListener(l -> {
			sliderLabel.setText(slider.getValue()+"%");
		});
		add(slider);
		
		
		JLabel meanLabel = new JLabel("Ingrese media:");
		meanLabel.setBounds(20, 70, 100, 30);
		JTextField meanTextField = new JTextField("0.0");
		meanTextField.setBounds(30, 110, 60, 30);
		add(meanLabel);
		add(meanTextField);
		JLabel stdLabel = new JLabel("Ingrese desvío estandar:");
		stdLabel.setBounds(140, 70, 150, 30);
		JTextField stdTextField = new JTextField("1.0");
		stdTextField.setBounds(170, 110, 60, 30);
		add(stdLabel);
		add(stdTextField);
		JButton button = new JButton("Realizar operacion");
		button.setBounds(40, 150, 180, 30);
		add(button);
		button.addActionListener(l -> {
			double percentage = slider.getValue();
			double mean = Double.parseDouble(meanTextField.getText());
			double std = Double.parseDouble(stdTextField.getText());
			Image img = state.getImage();
			state.setImage(ImageUtils.sumImage(img, 
					ImageUtils.gaussianNoiseImage(img.getWidth(), img.getHeight(),
							percentage/100.0, mean, std)));
			dispose();
		});
	}
}
