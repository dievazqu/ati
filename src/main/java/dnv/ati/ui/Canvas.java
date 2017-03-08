package dnv.ati.ui;

import java.awt.Graphics;

import javax.swing.JPanel;

import dnv.ati.model.Image;

public class Canvas extends JPanel {

	Image image;

	public void setImage(Image img) {
		image = img;
		repaint();
	}

	public Canvas() {
		setBounds(0, 0, 600, 600);
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		if (image != null)
			image.draw(g, 0, 0);
	}
}
