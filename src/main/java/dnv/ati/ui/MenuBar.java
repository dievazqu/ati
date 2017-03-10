package dnv.ati.ui;

import java.io.File;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

import dnv.ati.model.Image;
import dnv.ati.util.ImageUtils;

public class MenuBar extends JMenuBar {

	private Canvas canvas;
	
	public MenuBar(){
		JMenu fileMenu = new JMenu("Archivo");
		JMenuItem menuItem = new JMenuItem("Nueva Ventana");
		menuItem.addActionListener(e -> new Window());
		fileMenu.add(menuItem);
		
		JMenu loadMenu = new JMenu("Cargar Imagen");
		menuItem = new JMenuItem("Cargar desde .raw");
		menuItem.addActionListener(e -> loadRawImage());
		loadMenu.add(menuItem);
		menuItem = new JMenuItem("Cargar desde .pgm");
		menuItem.addActionListener(e -> loadImage(ImageUtils::readFromPGM));
		loadMenu.add(menuItem);
		menuItem = new JMenuItem("Cargar desde .ppm");
		menuItem.addActionListener(e -> loadImage(ImageUtils::readFromPPM));
		loadMenu.add(menuItem);
		menuItem = new JMenuItem("Cargar desde .bpm");
		menuItem.addActionListener(e -> loadImage(ImageUtils::readFromBPM));
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
		
		JMenu editionMenu = new JMenu("Edicion");
		
		JMenuItem grayScale = new JMenuItem("Escala de grises");
		grayScale.addActionListener(e -> canvas.setImage(ImageUtils.grayScale()));
		editionMenu.add(grayScale);
		
		JMenu colorScaleMenu = new JMenu("Escala de colores");
		
		JMenuItem redScale = new JMenuItem("Rojo de base");
		redScale.addActionListener(e -> canvas.setImage(ImageUtils.colorScale(0)));
		colorScaleMenu.add(redScale);
		
		JMenuItem greenScale = new JMenuItem("Verde de base");
		greenScale.addActionListener(e -> canvas.setImage(ImageUtils.colorScale(1)));
		colorScaleMenu.add(greenScale);
		
		JMenuItem blueScale = new JMenuItem("Azul de base");
		blueScale.addActionListener(e -> canvas.setImage(ImageUtils.colorScale(2)));
		colorScaleMenu.add(blueScale);
		
		editionMenu.add(colorScaleMenu);
		add(editionMenu);
	}
	
	
	public void setCanvas(Canvas c){
		canvas = c;
	}
	
	private void loadRawImage(){
		JFrame rawFrame = new JFrame("Seleccione tamaño del .raw");
		rawFrame.setSize(400, 170);
		rawFrame.setLocationRelativeTo(null);
		rawFrame.setLayout(null);
		JTextField widthText = new JTextField("Ancho");
		widthText.setBounds(130, 20, 50, 30);
		JTextField heightText = new JTextField("Largo");
		heightText.setBounds(210, 20, 50, 30);
		JButton selectFileButton = new JButton("Seleccionar imagen");
		selectFileButton.setBounds(110, 80, 170, 30);
		selectFileButton.addActionListener(ee -> {
			int width, height;
			try{
				width = Integer.parseInt(widthText.getText());
				height = Integer.parseInt(heightText.getText());
			}catch(Exception e){
				return;
			}
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File("./images"));
			fileChooser.showDialog(null, "Load File");
			if (fileChooser.getSelectedFile() != null) {
					canvas.setImage(ImageUtils.readFromRAW(fileChooser.getSelectedFile(), width, height));
			}
			rawFrame.dispose();
		});
		rawFrame.add(widthText);
		rawFrame.add(heightText);
		rawFrame.add(selectFileButton);

		rawFrame.setVisible(true);
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
