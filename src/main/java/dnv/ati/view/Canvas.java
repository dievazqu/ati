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
import dnv.ati.util.Auxiliar;
import dnv.ati.util.ConversionUtils;
import dnv.ati.view.selectionFrames.SelectPixelFrame;
import dnv.ati.view.selectionFrames.SelectRectFrame;

public class Canvas extends JPanel implements ImageChangedListener{

	private Point mouseClick;
	private JLabel imageLabel;
	private State state;
	
	public Canvas(State state) {
		this.state = state;
		imageLabel = new JLabel();
		imageLabel.addMouseMotionListener(mouseListener);
		imageLabel.addMouseListener(mouseListener);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(imageLabel);
		state.addImageChangedListener(this);
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
			imageLabel.setIcon(new ImageIcon(state.filter(img).toBufferedImage()));
		}
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		if (startDragginPoint!=null && currentDragPoint != null){
			
			g.setColor(Color.black);
			Auxiliar.sortPoint((p,q) -> {
				g.drawRect(p.x, p.y, q.x-p.x, q.y-p.y);	
			}, startDragginPoint, currentDragPoint);

			
		}
	}
	
	Point startDragginPoint;
	Point currentDragPoint;
	
	public void draggingFrom(Point p){
		startDragginPoint = p;
	}
	
	public void finishDragging(Point finish){
		// Do Things
		state.setStatus(Status.STAND_BY);
		new SelectRectFrame(state, startDragginPoint, currentDragPoint);
		startDragginPoint = null;
		repaint();
	}
	
	private MouseInputListener mouseListener = new MouseInputListener() {
		
		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub	
		}
		
		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub
			if(state.getStatus()==Status.SELECTING_RECT){
				if(startDragginPoint == null)
					draggingFrom(arg0.getPoint());
				currentDragPoint = arg0.getPoint();	
				repaint();
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent arg0) {
			if(state.getStatus() == Status.SELECTING_PIXEL){
				new SelectPixelFrame(state, arg0.getPoint());
				state.setStatus(Status.STAND_BY);
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
