package dnv.ati.view.filterFrames;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import dnv.ati.model.Image;
import dnv.ati.model.State;

@SuppressWarnings("serial")
public class GaussianFilterFrame extends JFrame {

	public GaussianFilterFrame(State state) {
		super("Filtro Gaussiano");
		setSize(310, 210);
		setLayout(null);
		setLocationRelativeTo(null);
		setVisible(true);

		JLabel percentageLabel = new JLabel("Seleccione tamaño de ventana:");
		percentageLabel.setBounds(20, 10, 200, 30);
		add(percentageLabel);
		JLabel sliderLabel = new JLabel();
		sliderLabel.setBounds(240, 50, 40, 20);
		add(sliderLabel);
		JSlider slider = new JSlider(1, 50);
		slider.setValue(0);
		slider.setBounds(20, 50, 200, 20);
		slider.addChangeListener(l -> {
			sliderLabel.setText((slider.getValue() * 2 + 1) + "");
		});
		sliderLabel.setText((slider.getValue() * 2 + 1) + "");
		add(slider);
		JTextField simgaText = new JTextField("1.0");
		simgaText.setBounds(20, 80, 40, 30);
		add(simgaText);
		JButton button = new JButton("Realizar operacion");
		button.setBounds(40, 130, 180, 30);
		add(button);
		button.addActionListener(l -> {
			Image img = state.getImage();
			int windowSize = slider.getValue() * 2 + 1;
			double sigma = Double.parseDouble(simgaText.getText());
			img.gaussianFilter(windowSize, sigma);
			state.setImage(img);
			dispose();
		});
	}
}