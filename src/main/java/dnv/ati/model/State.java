package dnv.ati.model;

import java.util.LinkedList;
import java.util.List;

public class State implements ImageFilter{

	public static class LevelSetsInfo{
		double[][] theta;
		double[] t;
		
		public LevelSetsInfo(double[][] theta, double[] t) {
			this.theta = theta;
			this.t = t;
		}
	}
	
	
	private Image image;
	private Status status;
	private ImageFilter imageFilter; 
	private LevelSetsInfo info;
	
	public void setInfo(LevelSetsInfo info) {
		this.info = info;
	}
	
	public LevelSetsInfo getInfo() {
		return info;
	}
	
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
	
	public ImageFilter getImageFilter() {
		return imageFilter;
	}
	
	public void setImageFilter(ImageFilter imageFilter) {
		this.imageFilter = imageFilter;
		notifyImageChanged(image);
	}
	
	@Override
	public Image filter(Image img) {
		if(imageFilter == null)
			return img;
		return imageFilter.filter(img);
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
