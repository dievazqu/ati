package dnv.ati.view;

import java.awt.Cursor;
import java.io.File;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

import dnv.ati.model.Image;
import dnv.ati.model.State;
import dnv.ati.model.Status;
import dnv.ati.util.ImageUtils;

public class MenuBar extends JMenuBar {

	public MenuBar() {
		JMenu fileMenu = new JMenu("Archivo");
		JMenuItem menuItem = new JMenuItem("Nueva Ventana");
		menuItem.addActionListener(e -> new AppFrame());
		fileMenu.add(menuItem);

		JMenu loadMenu = new JMenu("Cargar Imagen");
		menuItem = new JMenuItem("Cargar desde .raw");
		menuItem.addActionListener(e -> new LoadRAWFrame());
		loadMenu.add(menuItem);
		menuItem = new JMenuItem("Cargar desde .pgm");
		menuItem.addActionListener(e -> loadImage(ImageUtils::readFromPGM));
		loadMenu.add(menuItem);
		menuItem = new JMenuItem("Cargar desde .ppm");
		menuItem.addActionListener(e -> loadImage(ImageUtils::readFromPPM));
		loadMenu.add(menuItem);
		menuItem = new JMenuItem("Cargar desde .bmp");
		menuItem.addActionListener(e -> loadImage(ImageUtils::readFromBPM));
		loadMenu.add(menuItem);

		JMenu saveMenu = new JMenu("Guardar Imagen");
		menuItem = new JMenuItem("Guardar en .raw");
		menuItem.addActionListener(e -> saveImage(ImageUtils::saveInRAW));
		saveMenu.add(menuItem);
		menuItem = new JMenuItem("Guardar en .pgm");
		menuItem.addActionListener(e -> saveImage(ImageUtils::saveInPGM));
		saveMenu.add(menuItem);
		menuItem = new JMenuItem("Guardar en .ppm");
		menuItem.addActionListener(e -> saveImage(ImageUtils::saveInPPM));
		saveMenu.add(menuItem);
		menuItem = new JMenuItem("Guardar en .bmp");
		menuItem.addActionListener(e -> saveImage(ImageUtils::saveInBMP));
		saveMenu.add(menuItem);

		fileMenu.add(loadMenu);
		fileMenu.add(saveMenu);
		add(fileMenu);

		JMenu selectionMenu = new JMenu("Selecciones");
		JMenu selectPixelMenu = new JMenu("Seleccion de pixel");
		JMenuItem selectPixelByKey = new JMenuItem("Por teclado");
		selectPixelByKey.addActionListener(e -> {
			new SelectPixelFrame();
		});
		selectPixelMenu.add(selectPixelByKey);
		JMenuItem selectPixelByMouse = new JMenuItem("Por mouse");
		selectPixelByMouse.addActionListener(e -> {
			State.getInstance().setStatus(Status.SELECTING_PIXEL);
		});
		selectPixelMenu.add(selectPixelByMouse);
		selectionMenu.add(selectPixelMenu);

		JMenu selectRectMenu = new JMenu("Seleccion de rectangulo");
		JMenuItem selectRectByKey = new JMenuItem("Por teclado");
		selectRectMenu.add(selectRectByKey);
		JMenuItem selectRectByMouse = new JMenuItem("Por mouse");
		selectRectByMouse.addActionListener(e -> {
			State.getInstance().setStatus(Status.SELECTING_RECT);
		});
		selectRectMenu.add(selectRectByMouse);
		selectionMenu.add(selectRectMenu);
		add(selectionMenu);

		JMenu custumImagesMenu = new JMenu("Imagenes creadas");

		JMenuItem grayScale = new JMenuItem("Escala de grises");
		grayScale.addActionListener(e -> State.getInstance().setImage(
				ImageUtils.grayScale()));
		custumImagesMenu.add(grayScale);

		JMenu colorScaleMenu = new JMenu("Escala de colores");

		JMenuItem redScale = new JMenuItem("Rojo de base");
		redScale.addActionListener(e -> State.getInstance().setImage(
				ImageUtils.colorScale(0)));
		colorScaleMenu.add(redScale);

		JMenuItem greenScale = new JMenuItem("Verde de base");
		greenScale.addActionListener(e -> State.getInstance().setImage(
				ImageUtils.colorScale(1)));
		colorScaleMenu.add(greenScale);

		JMenuItem blueScale = new JMenuItem("Azul de base");
		blueScale.addActionListener(e -> State.getInstance().setImage(
				ImageUtils.colorScale(2)));
		colorScaleMenu.add(blueScale);

		custumImagesMenu.add(colorScaleMenu);
		add(custumImagesMenu);
	}

	private void loadImage(Function<File, Image> imageConverter) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("./images"));
		fileChooser.showDialog(null, "Cargar Imagen");
		if (fileChooser.getSelectedFile() != null) {
			State.getInstance().setImage(
					imageConverter.apply(fileChooser.getSelectedFile()));
		}
	}

	private void saveImage(BiConsumer<File, Image> imageConverter) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("./images"));
		fileChooser.showDialog(null, "Guardar Imagen");
		if (fileChooser.getSelectedFile() != null) {
			imageConverter.accept(fileChooser.getSelectedFile(), State
					.getInstance().getImage());
		}
	}

}
