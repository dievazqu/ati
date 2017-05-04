package dnv.ati.view.filterFrames;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import dnv.ati.model.Image;
import dnv.ati.model.State;

public class IsotropicDiffusionFrame extends JFrame {

	public IsotropicDiffusionFrame(State state) {
		super("Difusión isotrópica");
		setSize(280, 175);
		setLocationRelativeTo(null);
		setVisible(true);
		setLayout(null);
		JLabel tLabel = new JLabel("Ingrese t:");
		tLabel.setBounds(100, 20, 100, 30);
		add(tLabel);
		JTextField tText = new JTextField("1");
		tText.setBounds(100, 50, 60, 30);
		add(tText);
		JButton button = new JButton("Realizar operación");
		button.setBounds(60, 100, 140, 30);
		add(button);
		button.addActionListener(l -> {
			Image img = state.getImage();
			int t = Integer.parseInt(tText.getText());
			img.isotropicDiffusion(t);
			state.setImage(img);
			dispose();
		});
	}
}
