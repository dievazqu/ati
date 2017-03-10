package dnv.ati.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dnv.ati.model.Image;

public class Canvas extends JPanel{

	Image image;
	Point mouseClick;
	JLabel imageLabel;
	
	public Canvas() {
	//	addMouseListener(mouseListener);
		imageLabel = new JLabel();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(imageLabel);
	}
	
	public void setImage(Image img) {
		if(img==null){
			imageLabel.setIcon(null);
		}else{
			imageLabel.setIcon(new ImageIcon(img.toBufferedImage()));
		}
		//imageLabel.addMouseListener(mouseListener);
		image = img;
		repaint();
	}
	
	public Image getImage(){
		return image;
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		if (image != null){
		//	image.draw(g, 0, 0);
			if(mouseClick != null){
				try{
					g.setColor(new Color(image.getRGB(mouseClick.x, mouseClick.y)));
					g.fillRect(600, 100, 100, 100);
				}catch(ArrayIndexOutOfBoundsException e){
					//TODO: Maybe we can check before
				}
			}
		}
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
			mouseClick = new Point(arg0.getX(), arg0.getY());
			repaint();
		}
	};
	
}
