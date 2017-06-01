package dnv.ati.view.borderFrames;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import dnv.ati.model.Image;
import dnv.ati.model.State;

@SuppressWarnings("serial")
public class CannyBorderFrame extends JFrame {

	public CannyBorderFrame(State state) {
		super("Detector de Canny");
		setSize(290, 240);
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
		JLabel sigmaLabel = new JLabel("sigma:");
		sigmaLabel.setBounds(20, 80, 40, 30);
		add(sigmaLabel);
		JTextField simgaText = new JTextField("1.0");
		simgaText.setBounds(70, 80, 40, 30);
		add(simgaText);
		
		JLabel tLabel = new JLabel("t1 t2:");
		tLabel.setBounds(20, 120, 40, 30);
		add(tLabel);
		JTextField t1Text = new JTextField("85");
		t1Text.setBounds(70, 120, 40, 30);
		add(t1Text);
		
		JTextField t2Text = new JTextField("170");
		t2Text.setBounds(120, 120, 40, 30);
		add(t2Text);
		
		JButton button = new JButton("Realizar operacion");
		button.setBounds(40, 160, 180, 30);
		add(button);

		button.addActionListener(l -> {
			Image img = state.getImage();
			int windowSize = slider.getValue() * 2 + 1;
			double sigma = Double.parseDouble(simgaText.getText());
			double t1 = Double.parseDouble(t1Text.getText());
			double t2 = Double.parseDouble(t2Text.getText());
			img.cannyBorderDetector(windowSize, sigma, t1, t2);
			state.setImage(img);
			dispose();
		});
	}
}