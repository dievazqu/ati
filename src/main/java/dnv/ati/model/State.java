package dnv.ati.model;

import java.util.LinkedList;
import java.util.List;

public class State {

	private static State instance;
	private Image image;
	private List<ImageChangedListener> imageChangedListeners;
	
	public static interface ImageChangedListener{
		public void onImageChange();
	}
	
	public void addImageChangedListener(ImageChangedListener listener){
		imageChangedListeners.add(listener);
	}
	
	public Image getImage() {
		return image;
	}
	
	public void setImage(Image image) {
		this.image = image;
		notifyImageChanged();
	}
	
	public void notifyImageChanged(){
		for (ImageChangedListener imageChangedListener : imageChangedListeners) {
			imageChangedListener.onImageChange();
		}
	}
	
	private State(){
		imageChangedListeners = new LinkedList<ImageChangedListener>();
	}
	
	public static State getInstance(){
		if(instance==null){
			instance = new State();
		}
		return instance;
	}
	
}
