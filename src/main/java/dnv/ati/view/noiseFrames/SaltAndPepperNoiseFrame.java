package dnv.ati.view.noiseFrames;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import dnv.ati.model.Image;
import dnv.ati.model.State;
import dnv.ati.util.ImageUtils;

public class SaltAndPepperNoiseFrame extends JFrame {

	public SaltAndPepperNoiseFrame(State state){
		super("Ruido \"sal y pimienta\" (0<p0<p1<1)");
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
		
		
		JLabel p0Label = new JLabel("Ingrese p0:");
		p0Label.setBounds(20, 70, 100, 30);
		JTextField p0TextField = new JTextField("0.2");
		p0TextField.setBounds(30, 110, 60, 30);
		add(p0Label);
		add(p0TextField);
		JLabel p1Label = new JLabel("Ingrese p1:");
		p1Label.setBounds(140, 70, 150, 30);
		JTextField p1TextField = new JTextField("0.8");
		p1TextField.setBounds(170, 110, 60, 30);
		add(p1Label);
		add(p1TextField);
		JButton button = new JButton("Realizar operacion");
		button.setBounds(40, 150, 180, 30);
		add(button);
		button.addActionListener(l -> {
			double percentage = slider.getValue();
			double p0 = Double.parseDouble(p0TextField.getText());
			double p1 = Double.parseDouble(p1TextField.getText());
			Image img = state.getImage();
			ImageUtils.saltAndPepperNoiseImage(img, percentage/100.0, p0, p1);
			state.setImage(img);
			dispose();
		});
	}
}
