package dnv.ati.view.borderFrames;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import dnv.ati.model.Image;
import dnv.ati.model.State;

@SuppressWarnings("serial")
public class CircularHoughFrame extends JFrame {

	public CircularHoughFrame(State state) {
		super("Transformada circular de Hough");
		setSize(210, 220);
		setLayout(null);
		setLocationRelativeTo(null);
		setVisible(true);

		JLabel radiusStepsLabel = new JLabel("Radius steps:");
		radiusStepsLabel.setBounds(20, 10, 200, 30);
		JTextField radiusText = new JTextField("500");
		radiusText.setBounds(20, 40, 40, 30);
		add(radiusStepsLabel);
		add(radiusText);

		JLabel epsilonLabel = new JLabel("Epsilon:");
		epsilonLabel.setBounds(20, 70, 200, 30);
		JTextField epsilonText = new JTextField("1");
		epsilonText.setBounds(20, 100, 40, 30);
		add(epsilonLabel);
		add(epsilonText);

		JButton button = new JButton("Realizar operacion");
		button.setBounds(30, 140, 140, 30);
		add(button);

		button.addActionListener(l -> {
			Image img = state.getImage();
			int radius = Integer.parseInt(radiusText.getText());
			double epsilon = Double.parseDouble(epsilonText.getText());
			img.circularHoughTransformation(radius, epsilon);
			state.setImage(img);
			dispose();
		});
	}
}