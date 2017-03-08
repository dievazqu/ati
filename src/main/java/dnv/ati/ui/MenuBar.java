package dnv.ati.ui;

import java.io.File;
import java.util.function.Function;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import dnv.ati.model.Image;

public class MenuBar extends JMenuBar {

	private Canvas canvas;
	
	public MenuBar(){
		JMenu fileMenu = new JMenu("Archivo");
		JMenuItem menuItem = new JMenuItem("Nueva Ventana");
		menuItem.addActionListener(e -> new Window());
		fileMenu.add(menuItem);
		
		JMenu loadMenu = new JMenu("Cargar Imagen");
		menuItem = new JMenuItem("Cargar desde .raw");
		menuItem.addActionListener(e -> loadImage(Image::readFromRAW));
		loadMenu.add(menuItem);
		menuItem = new JMenuItem("Cargar desde .pgm");
		menuItem.addActionListener(e -> loadImage(Image::readFromPGM));
		loadMenu.add(menuItem);
		menuItem = new JMenuItem("Cargar desde .ppm");
		menuItem.addActionListener(e -> loadImage(Image::readFromPPM));
		loadMenu.add(menuItem);
		menuItem = new JMenuItem("Cargar desde .bpm");
		menuItem.addActionListener(e -> loadImage(Image::readFromBPM));
		loadMenu.add(menuItem);
		
		JMenu saveMenu = new JMenu("Guardar Imagen");
		menuItem = new JMenuItem("Guardar en .raw");
		saveMenu.add(menuItem);
		menuItem = new JMenuItem("Guardar en .pgm");
		saveMenu.add(menuItem);
		menuItem = new JMenuItem("Guardar en .ppm");
		saveMenu.add(menuItem);
		menuItem = new JMenuItem("Guardar en .bpm");
		saveMenu.add(menuItem);
		
		
		fileMenu.add(loadMenu);
		fileMenu.add(saveMenu);
		add(fileMenu);
	}
	
	
	public void setCanvas(Canvas c){
		canvas = c;
	}
	
	private void loadImage(Function<File, Image> imageConverter){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("./images"));
		fileChooser.showDialog(null, "Load File");
		if (fileChooser.getSelectedFile() != null) {
				canvas.setImage(imageConverter.apply(fileChooser.getSelectedFile()));
		}
	}
	
}
