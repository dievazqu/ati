package dnv.ati.view.editionFrames;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import dnv.ati.model.Image;
import dnv.ati.model.State;

@SuppressWarnings("serial")
public class ProdByScalarFrame extends JFrame {

	public ProdByScalarFrame(State state){
		super("Producto por escalar");
		setSize(300, 170);
		setLayout(null);
		setLocationRelativeTo(null);
		setVisible(true);
		JLabel label = new JLabel("Ingrese el escalar:");
		label.setBounds(40, 10, 200, 30);
		JTextField scalarTextField = new JTextField("1.0");
		scalarTextField.setBounds(50, 50, 60, 30);
		add(label);
		add(scalarTextField);
		JButton button = new JButton("Realizar operacion");
		button.setBounds(40, 90, 200, 30);
		add(button);
		button.addActionListener(l -> {
			Image img = state.getImage();
			String text = scalarTextField.getText();
			double value = Double.parseDouble(text);
			img.prodByScalar(value);
			state.setImage(img);
			dispose();
		});
	}
}
