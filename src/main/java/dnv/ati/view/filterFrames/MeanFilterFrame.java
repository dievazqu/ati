package dnv.ati.view.filterFrames;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;

import dnv.ati.model.Image;
import dnv.ati.model.State;

@SuppressWarnings("serial")
public class MeanFilterFrame extends JFrame {

	public MeanFilterFrame(State state){
		super("Filtro de la media");
		setSize(310, 170);
		setLayout(null);
		setLocationRelativeTo(null);
		setVisible(true);

		JLabel percentageLabel = new JLabel("Seleccione tamaño de ventana:");
		percentageLabel.setBounds(20, 10, 200, 30);
		add(percentageLabel);
		JLabel sliderLabel = new JLabel();
		sliderLabel.setBounds(240, 50, 40, 20);
		add(sliderLabel);
		JSlider slider = new JSlider(0, 50);
		slider.setValue(0);
		slider.setBounds(20, 50, 200, 20);
		slider.addChangeListener(l -> {
			sliderLabel.setText((slider.getValue()*2+1)+"");
		});
		sliderLabel.setText((slider.getValue()*2+1)+"");
		add(slider);
		
		JButton button = new JButton("Realizar operacion");
		button.setBounds(40, 90, 180, 30);
		add(button);
		button.addActionListener(l -> {
			Image img = state.getImage();
			int windowSize = slider.getValue()*2+1;
			img.meanFilter(windowSize);
			state.setImage(img);
			dispose();
		});
	}
}
