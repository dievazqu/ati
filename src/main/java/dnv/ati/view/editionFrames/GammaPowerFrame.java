package dnv.ati.view.editionFrames;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import dnv.ati.model.Image;
import dnv.ati.model.State;

public class GammaPowerFrame extends JFrame {

	public GammaPowerFrame(State state){
		super("Potencia gamma");
		setSize(300, 170);
		setLayout(null);
		setLocationRelativeTo(null);
		setVisible(true);
		JLabel label = new JLabel("Ingrese gamma:");
		label.setBounds(40, 10, 200, 30);
		JTextField gammaTextField = new JTextField("1.0");
		gammaTextField.setBounds(50, 50, 60, 30);
		add(label);
		add(gammaTextField);
		JButton button = new JButton("Realizar operacion");
		button.setBounds(40, 90, 200, 30);
		add(button);
		button.addActionListener(l -> {
			Image img = state.getImage();
			String text = gammaTextField.getText();
			double value = Double.parseDouble(text);
			img.gammaPower(value);
			state.setImage(img);
			dispose();
		});
	}
}

