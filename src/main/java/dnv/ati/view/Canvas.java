package dnv.ati.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EventListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import dnv.ati.model.Image;
import dnv.ati.model.State;
import dnv.ati.model.State.ImageChangedListener;
import dnv.ati.model.Status;

public class Canvas extends JPanel implements ImageChangedListener{

	Point mouseClick;
	JLabel imageLabel;
	
	public Canvas() {
		imageLabel = new JLabel();
		imageLabel.addMouseMotionListener(mouseListener);
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
		if (startDragginPoint!=null && currentDragPoint != null){
			
			g.setColor(Color.black);
			int minx = (int)Math.min(startDragginPoint.getX(), currentDragPoint.getX());
			int maxx = (int)Math.max(startDragginPoint.getX(), currentDragPoint.getX());
			int miny = (int)Math.min(startDragginPoint.getY(), currentDragPoint.getY());
			int maxy = (int)Math.max(startDragginPoint.getY(), currentDragPoint.getY());

			g.drawRect(minx, miny, maxx-minx, maxy-miny);
		}
	}
	
	Point startDragginPoint;
	Point currentDragPoint;
	
	public void draggingFrom(Point p){
		startDragginPoint = p;
	}
	
	public void finishDragging(Point finish){
		// Do Things
		startDragginPoint = null;
	}
	
	private MouseInputListener mouseListener = new MouseInputListener() {
		
		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub	
		}
		
		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub
			State state = State.getInstance();
			if(state.getStatus()==Status.SELECTING_RECT){
				if(startDragginPoint == null)
					draggingFrom(arg0.getPoint());
				currentDragPoint = arg0.getPoint();	
				repaint();
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent arg0) {
			State state = State.getInstance();
			if(state.getStatus() == Status.SELECTING_PIXEL){
				//state.setStatus(Status.STAND_BY);
				new SelectPixelFrame(arg0.getPoint());
			}
			if(state.getStatus() == Status.SELECTING_RECT && startDragginPoint!=null){
				finishDragging(arg0.getPoint());
			}
		}
		
		@Override
		public void mousePressed(MouseEvent arg0) {
				
		}
		
		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseEntered(MouseEvent arg0) {
			
		}
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
		
		}
	};
	
}
