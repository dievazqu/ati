package dnv.ati.model;

import java.util.LinkedList;
import java.util.List;

public class State {

	private Image image;
	private Status status;
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public Image getImage() {
		return image;
	}
	
	public void setImage(Image image) {
		this.image = image;
		notifyImageChanged(image);
	}
	
	/* ------------------ Listeners --------------*/
	
	private List<ImageChangedListener> imageChangedListeners;
	
	public State(){
		imageChangedListeners = new LinkedList<ImageChangedListener>();
		status = Status.STAND_BY;
	}
	
	public static interface ImageChangedListener{
		public void onImageChange(Image image);
	}
	
	public void addImageChangedListener(ImageChangedListener listener){
		imageChangedListeners.add(listener);
	}
	
	public void notifyImageChanged(Image image){
		for (ImageChangedListener imageChangedListener : imageChangedListeners) {
			imageChangedListener.onImageChange(image);
		}
	}
	
	
}
