package dnv.ati.model;

import java.util.LinkedList;
import java.util.List;

public class State {

	private static State instance;
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
	
	public static State getInstance(){
		if(instance==null){
			instance = new State();
		}
		return instance;
	}
	
	/* ------------------ Listeners --------------*/
	
	private List<ImageChangedListener> imageChangedListeners;
	
	private State(){
		imageChangedListeners = new LinkedList<ImageChangedListener>();
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
