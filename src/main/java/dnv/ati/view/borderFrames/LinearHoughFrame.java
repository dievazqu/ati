package dnv.ati.view.borderFrames;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import dnv.ati.model.Image;
import dnv.ati.model.State;

@SuppressWarnings("serial")
public class LinearHoughFrame extends JFrame {

	public LinearHoughFrame(State state) {
		super("Transformada lineal de Hough");
		setSize(210, 280);
		setLayout(null);
		setLocationRelativeTo(null);
		setVisible(true);

		JLabel titaStepsLabel = new JLabel("Tita steps:");
		titaStepsLabel.setBounds(20, 10, 200, 30);
		JTextField titaText = new JTextField("280");
		titaText.setBounds(20, 40, 40, 30);
		add(titaStepsLabel);
		add(titaText);

		JLabel roStepsLabel = new JLabel("Ro steps:");
		roStepsLabel.setBounds(20, 70, 200, 30);
		JTextField roText = new JTextField("500");
		roText.setBounds(20, 100, 40, 30);
		add(roStepsLabel);
		add(roText);

		JLabel epsilonLabel = new JLabel("Epsilon:");
		epsilonLabel.setBounds(20, 130, 200, 30);
		JTextField epsilonText = new JTextField("1");
		epsilonText.setBounds(20, 160, 40, 30);
		add(epsilonLabel);
		add(epsilonText);

		JButton button = new JButton("Realizar operacion");
		button.setBounds(30, 200, 140, 30);
		add(button);

		button.addActionListener(l -> {
			Image img = state.getImage();
			int tita = Integer.parseInt(titaText.getText());
			int ro = Integer.parseInt(roText.getText());
			double epsilon = Double.parseDouble(epsilonText.getText());
			img.linearHoughTransformation(tita, ro, epsilon);
			state.setImage(img);
			dispose();
		});
	}
}