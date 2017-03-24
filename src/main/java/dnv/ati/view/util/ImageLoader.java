package dnv.ati.view.util;

import java.io.File;
import java.util.function.Consumer;

import javax.swing.JFileChooser;

import dnv.ati.model.Image;
import dnv.ati.model.State;
import dnv.ati.util.ImageUtils;

public class ImageLoader {

	public static void loadImage(Consumer<Image> consumer){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("./images"));
		fileChooser.showDialog(null, "Cargar Imagen");
		if (fileChooser.getSelectedFile() != null) {
			String fileName = fileChooser.getSelectedFile().getName();
			String[] split = fileName.split("\\.", -1);
			String ext = split[split.length-1].toLowerCase();
			switch (ext) {
			case "raw":
				new LoadRAWFrame(fileChooser.getSelectedFile(), consumer);
				break;
			case "pgm":
				consumer.accept(
						ImageUtils.readFromPGM(fileChooser.getSelectedFile()));
				break;
			case "ppm":
				consumer.accept(
						ImageUtils.readFromPPM(fileChooser.getSelectedFile()));
				break;
			case "bmp":
				consumer.accept(
						ImageUtils.readFromBMP(fileChooser.getSelectedFile()));
				break;
			default:
				throw new Error("Invalid format");
			}
		}
	}
}
