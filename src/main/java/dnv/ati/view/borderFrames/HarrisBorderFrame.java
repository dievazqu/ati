package dnv.ati.view.borderFrames;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;

import dnv.ati.model.Image;
import dnv.ati.model.State;

@SuppressWarnings("serial")
public class HarrisBorderFrame extends JFrame {

	public HarrisBorderFrame(State state){
		super("Detector Harris");
		setSize(310, 180);
		setLayout(null);
		setLocationRelativeTo(null);
		setVisible(true);
		JLabel percentageLabel = new JLabel("Relacion con el maximo para");
		percentageLabel.setBounds(40, 10, 200, 30);
		add(percentageLabel);
		JLabel percentageLabel2 = new JLabel("ser considerado esquina:");
		percentageLabel2.setBounds(40, 30, 200, 30);
		add(percentageLabel2);
		JLabel sliderLabel = new JLabel("50.0%");
		sliderLabel.setBounds(240, 70, 40, 20);
		add(sliderLabel);
		JSlider slider = new JSlider(0, 1000);
		slider.setBounds(20, 70, 200, 20);
		slider.addChangeListener(l -> {
			sliderLabel.setText(slider.getValue()/10+"."+slider.getValue()%10+"%");
		});
		add(slider);
		
		
		
		JButton button = new JButton("Realizar operacion");
		button.setBounds(40, 100, 180, 30);
		add(button);
		button.addActionListener(l -> {
			double percentage = slider.getValue();
			Image img = state.getImage();
			img.harrisCornerDetector(percentage/1000.0);
			state.setImage(img);
			dispose();
		});
	}
}
