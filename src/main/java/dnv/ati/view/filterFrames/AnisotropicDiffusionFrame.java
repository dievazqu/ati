package dnv.ati.view.filterFrames;

import java.util.function.BiFunction;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import dnv.ati.model.Image;
import dnv.ati.model.State;

public class AnisotropicDiffusionFrame extends JFrame{
	
	public AnisotropicDiffusionFrame(State state, BiFunction<Double, Double, Double> f){
		super("Difusión anisotrópica");
		setSize(250, 175);
		setLocationRelativeTo(null);
		setVisible(true);
		setLayout(null);
		JLabel sigmaLabel = new JLabel("Ingrese sigma:");
		sigmaLabel.setBounds(20, 20, 100, 30);
		add(sigmaLabel);
		JTextField sigmaText = new JTextField("10.0");
		sigmaText.setBounds(20, 50, 60, 30);
		add(sigmaText);
		JLabel tLabel = new JLabel("Ingrese t:");
		tLabel.setBounds(150, 20, 100, 30);
		add(tLabel);
		JTextField tText = new JTextField("1");
		tText.setBounds(150, 50, 60, 30);
		add(tText);
		JButton button = new JButton("Realizar operación");
		button.setBounds(45, 100, 140, 30);
		add(button);
		button.addActionListener(l -> {
			Image img = state.getImage();
			int t = Integer.parseInt(tText.getText());
			double sigma = Double.parseDouble(sigmaText.getText());
			img.anisotropicDiffusion(t, x -> f.apply(sigma, x));
			state.setImage(img);
			dispose();
		});
		
	}
	
	
}
