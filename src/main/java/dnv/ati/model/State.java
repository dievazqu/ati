package dnv.ati.model;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class State {

	private static State instance;
	private Image image;
	
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
	private OnClickListener onUniqueClickListener;
	
	private State(){
		imageChangedListeners = new LinkedList<ImageChangedListener>();
		onUniqueClickListener = null;
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
	
	public static interface OnClickListener{
		public void onClick(Point click);
	}
	
	public void addUniqueOnClickListener(OnClickListener listener){
		onUniqueClickListener = listener;
	}
	
	/**
	 * Notifies and remove observers.
	 */
	public void notifyUniqueOnClick(Point click){
		if(onUniqueClickListener != null){
			onUniqueClickListener.onClick(click);
			onUniqueClickListener = null;
		}
	}
	
}
