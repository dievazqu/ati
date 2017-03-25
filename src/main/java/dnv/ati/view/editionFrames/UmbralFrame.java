package dnv.ati.view.editionFrames;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;

import dnv.ati.model.Image;
import dnv.ati.model.State;

@SuppressWarnings("serial")
public class UmbralFrame extends JFrame {

	public UmbralFrame(State state){
		super("Umbralizacion");
		setSize(310, 170);
		setLayout(null);
		setLocationRelativeTo(null);
		setVisible(true);

		JLabel percentageLabel = new JLabel("Seleccione umbral:");
		percentageLabel.setBounds(20, 10, 200, 30);
		add(percentageLabel);
		JLabel sliderLabel = new JLabel("128");
		sliderLabel.setBounds(240, 50, 40, 20);
		add(sliderLabel);
		JSlider slider = new JSlider(0, 255);
		slider.setBounds(20, 50, 200, 20);
		slider.addChangeListener(l -> {
			sliderLabel.setText(slider.getValue()+"");
		});
		add(slider);
		
		JButton button = new JButton("Realizar operacion");
		button.setBounds(40, 90, 180, 30);
		add(button);
		button.addActionListener(l -> {
			Image img = state.getImage();
			img.umbralize(slider.getValue());
			state.setImage(img);
			dispose();
		});
	}
}
