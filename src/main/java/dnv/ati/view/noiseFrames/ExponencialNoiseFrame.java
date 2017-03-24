package dnv.ati.view.noiseFrames;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import dnv.ati.model.Image;
import dnv.ati.model.State;
import dnv.ati.util.ImageUtils;

public class ExponencialNoiseFrame extends JFrame {

	public ExponencialNoiseFrame(State state){
		super("Ruido exponencial");
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
		
		
		JLabel lambdaLabel = new JLabel("Ingrese lambda:");
		lambdaLabel.setBounds(20, 70, 100, 30);
		JTextField lambdaTextField = new JTextField("5.0");
		lambdaTextField.setToolTipText("Este valor va entre 5 y 10");
		lambdaTextField.setBounds(30, 110, 60, 30);
		add(lambdaLabel);
		add(lambdaTextField);
		
		JButton button = new JButton("Realizar operacion");
		button.setBounds(40, 150, 180, 30);
		add(button);
		button.addActionListener(l -> {
			double percentage = slider.getValue();
			double lambda = Double.parseDouble(lambdaTextField.getText());
			Image img = state.getImage();
			state.setImage(ImageUtils.prodImage(img, 
					ImageUtils.exponencialNoiseImage(img.getWidth(), img.getHeight(),
							percentage/100.0, lambda)));
			dispose();
		});
	}
}
