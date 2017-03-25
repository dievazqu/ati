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
public class RayleighNoiseFrame extends JFrame {

	public RayleighNoiseFrame(State state){
		super("Ruido de Rayleigh");
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
		
		
		JLabel phiLabel = new JLabel("Ingrese phi:");
		phiLabel.setBounds(20, 70, 100, 30);
		JTextField phiTextField = new JTextField("0.2");
		phiTextField.setBounds(30, 110, 60, 30);
		add(phiLabel);
		add(phiTextField);
		
		JButton button = new JButton("Realizar operacion");
		button.setBounds(40, 150, 180, 30);
		add(button);
		button.addActionListener(l -> {
			double percentage = slider.getValue();
			double phi = Double.parseDouble(phiTextField.getText());
			Image img = state.getImage();
			state.setImage(ImageUtils.prodImage(img, 
					ImageUtils.rayleighNoiseImage(img.getWidth(), img.getHeight(),
							percentage/100.0, phi)));
			dispose();
		});
	}
}
