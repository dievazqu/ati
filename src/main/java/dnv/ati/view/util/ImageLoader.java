package dnv.ati.view.util;

import java.io.File;
import java.util.function.Consumer;

import javax.swing.JFileChooser;
import javax.swing.SwingWorker;

import dnv.ati.model.Image;
import dnv.ati.util.ImageUtils;

public class ImageLoader {

	static File[] ficheros;
	
	public static void loadDirectory(Consumer<Image> consumer){
		JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setCurrentDirectory(new File("./images"));
		fileChooser.showDialog(null, "Cargar Carpeta");
		if (fileChooser.getSelectedFile() != null) {
			ficheros = fileChooser.getSelectedFile().listFiles();
			recursion(consumer, 0);	
		}
	}
	
	private static void recursion(Consumer<Image> consumer, int index){
		if(index>=ficheros.length) return;
		final SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>(){
			@Override
			protected Object doInBackground() throws Exception {
				loadImage(consumer, ficheros[index]);
				return null;
			}	
			@Override
			protected void done() {
				super.done();
				recursion(consumer, index+1);
			}
		};
		worker.execute();
	}
	
	
	public static void loadImage(Consumer<Image> consumer){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("./images"));
		fileChooser.showDialog(null, "Cargar Imagen");
		if (fileChooser.getSelectedFile() != null) {
			loadImage(consumer, fileChooser.getSelectedFile());
		}
	}
	
	private static void loadImage(Consumer<Image> consumer, File file){
		String fileName = file.getName();
		String[] split = fileName.split("\\.", -1);
		String ext = split[split.length-1].toLowerCase();
		switch (ext) {
		case "raw":
			new LoadRAWFrame(file, consumer);
			break;
		case "pgm":
			consumer.accept(
					ImageUtils.readFromPGM(file));
			break;
		case "ppm":
			consumer.accept(
					ImageUtils.readFromPPM(file));
			break;
		case "bmp":
			consumer.accept(
					ImageUtils.readFromBMP(file));
			break;
		default:
			consumer.accept(
					ImageUtils.readFromBMP(file));
		}
	}
}
