package dnv.ati.view;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dnv.ati.model.Image;
import dnv.ati.model.State;
import dnv.ati.model.State.ImageChangedListener;

public class Canvas extends JPanel implements ImageChangedListener{

	Point mouseClick;
	JLabel imageLabel;
	
	public Canvas() {
		imageLabel = new JLabel();
		imageLabel.addMouseListener(mouseListener);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(imageLabel);
		State.getInstance().addImageChangedListener(this);
	}
	
	@Override
	public void onImageChange(Image image) {
		setImage(image);
	}
	
	private void setImage(Image img) {
		if(img==null){
			imageLabel.setIcon(null);
		}else{
			// Esto puede ser ineficiente, pero sin la label
			// no podia hacer que funcione el scrollable Panel. 
			imageLabel.setIcon(new ImageIcon(img.toBufferedImage()));
		}
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
	}
	
	
	private MouseListener mouseListener = new MouseListener() {
		
		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			State state = State.getInstance();
			if(state.getImage() != null){
				state.notifyUniqueOnClick(arg0.getPoint());
			}
		}
	};
	
}
