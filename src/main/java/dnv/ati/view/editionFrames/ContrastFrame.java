package dnv.ati.view.editionFrames;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import dnv.ati.model.Image;
import dnv.ati.model.State;


@SuppressWarnings("serial")
public class ContrastFrame extends JFrame {

	private JTextField r1Text;
	private JTextField s1Text;
	private JTextField r2Text;
	private JTextField s2Text;

	public ContrastFrame(State state){
		super("Aplicar contraste");
		setSize(225, 175);
		setLocationRelativeTo(null);
		setVisible(true);
		setLayout(null);

		JLabel positionLabel = new JLabel("Puntos (r1, s1) y (r2, s2):");
		positionLabel.setBounds(20, 20, 300, 20);
		add(positionLabel);
		r1Text = new JTextField(new Integer(0));
		r1Text.setBounds(20, 50, 40, 30);
		add(r1Text);
		s1Text = new JTextField(new Integer(0));
		s1Text.setBounds(70, 50, 40, 30);
		add(s1Text);
		r2Text = new JTextField(new Integer(0));
		r2Text.setBounds(120, 50, 40, 30);
		add(r2Text);
		s2Text = new JTextField(new Integer(0));
		s2Text.setBounds(170, 50, 40, 30);
		add(s2Text);

		JButton contrastApply = new JButton("Aplicar contraste");
		contrastApply.setBounds(20, 100, 190, 30);
		contrastApply.addActionListener(ee -> {
			try{
				double r1 = Double.parseDouble(r1Text.getText());
				double s1 = Double.parseDouble(s1Text.getText());
				double r2 = Double.parseDouble(r2Text.getText());
				double s2 = Double.parseDouble(s2Text.getText());
				Image img = state.getImage();
				img.contrast(r1, s1, r2, s2);
				state.setImage(img);
				dispose();
				
			}catch(Exception e){
				e.printStackTrace();
			}
		});
		add(contrastApply);
	}

}
