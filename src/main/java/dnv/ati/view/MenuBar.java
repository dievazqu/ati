package dnv.ati.view;

import java.io.File;
import java.util.function.BiConsumer;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import dnv.ati.model.Image;
import dnv.ati.model.State;
import dnv.ati.model.Status;
import dnv.ati.util.ImageUtils;
import dnv.ati.util.view.ImageLoader;

public class MenuBar extends JMenuBar {

	private State state;
	JMenuItem activeFilterItem;
	
	public MenuBar(State state) {
		this.state = state;
		JMenu fileMenu = new JMenu("Archivo");
		JMenuItem menuItem = new JMenuItem("Nueva Ventana");
		menuItem.addActionListener(e -> new AppFrame());
		fileMenu.add(menuItem);

		JMenuItem loadMenu = new JMenuItem("Cargar Imagen");
		loadMenu.addActionListener(l -> {
			ImageLoader.loadImage(img -> {
				state.setImage(img);	
			});
		});
		

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
		
		JMenu editionMenu = new JMenu("Edicion");
		JMenu operationImageMenu = new JMenu("Operacion entre imagenes");
		JMenuItem imageSum = new JMenuItem("Sumar Imagen");
		imageSum.addActionListener(l -> {
			ImageLoader.loadImage(img -> state.setImage(
					ImageUtils.sumImage(state.getImage(), img)));
		});
		operationImageMenu.add(imageSum);
		
		JMenuItem imageDiff = new JMenuItem("Restar Imagen");
		imageDiff.addActionListener(l -> {
			ImageLoader.loadImage(img -> state.setImage(
					ImageUtils.diffImage(state.getImage(), img)));
		});
		operationImageMenu.add(imageDiff);
		
		JMenuItem imageProd = new JMenuItem("Producto de Imagen");
		imageProd.addActionListener(l -> {
			ImageLoader.loadImage(img -> state.setImage(
					ImageUtils.prodImage(state.getImage(), img)));
		});
		operationImageMenu.add(imageProd);
		editionMenu.add(operationImageMenu);
		
		JMenuItem prodByScalarItem = new JMenuItem("Producto por escalar");
		prodByScalarItem.addActionListener(l -> {
			new ProdByScalarFrame(state);
		});
		editionMenu.add(prodByScalarItem);
		
		JMenuItem normalizeItem = new JMenuItem("Normalizar");
		normalizeItem.addActionListener(l -> {
			Image img = state.getImage();
			img.normalize();
			state.setImage(img);
		});
		editionMenu.add(normalizeItem);
		
		JMenuItem dynamicRangeItem = new JMenuItem("Rango Dinamico");
		dynamicRangeItem.addActionListener(l -> {
			Image img = state.getImage();
			img.dynamicRange();
			state.setImage(img);
		});
		editionMenu.add(dynamicRangeItem);
		
		add(editionMenu);
		
		JMenu selectionMenu = new JMenu("Selecciones");
		JMenu selectPixelMenu = new JMenu("Seleccion de pixel");
		JMenuItem selectPixelByKey = new JMenuItem("Por teclado");
		selectPixelByKey.addActionListener(e -> {
			new SelectPixelFrame(state);
		});
		selectPixelMenu.add(selectPixelByKey);
		JMenuItem selectPixelByMouse = new JMenuItem("Por mouse");
		selectPixelByMouse.addActionListener(e -> {
			state.setStatus(Status.SELECTING_PIXEL);
		});
		selectPixelMenu.add(selectPixelByMouse);
		selectionMenu.add(selectPixelMenu);

		JMenu selectRectMenu = new JMenu("Seleccion de rectangulo");
		JMenuItem selectRectByKey = new JMenuItem("Por teclado");
		selectRectByKey.addActionListener(e -> {
			new SelectRectFrame(state);
		});
		selectRectMenu.add(selectRectByKey);
		JMenuItem selectRectByMouse = new JMenuItem("Por mouse");
		selectRectByMouse.addActionListener(e -> {
			state.setStatus(Status.SELECTING_RECT);
		});
		selectRectMenu.add(selectRectByMouse);
		selectionMenu.add(selectRectMenu);
		add(selectionMenu);

		JMenu customImagesMenu = new JMenu("Imagenes creadas");

		JMenuItem grayScale = new JMenuItem("Escala de grises");
		grayScale.addActionListener(e -> state.setImage(
				ImageUtils.grayScale()));
		customImagesMenu.add(grayScale);

		JMenu colorScaleMenu = new JMenu("Escala de colores");

		JMenuItem redScale = new JMenuItem("Rojo de base");
		redScale.addActionListener(e -> state.setImage(
				ImageUtils.colorScale(0)));
		colorScaleMenu.add(redScale);

		JMenuItem greenScale = new JMenuItem("Verde de base");
		greenScale.addActionListener(e -> state.setImage(
				ImageUtils.colorScale(1)));
		colorScaleMenu.add(greenScale);

		JMenuItem blueScale = new JMenuItem("Azul de base");
		blueScale.addActionListener(e -> state.setImage(
				ImageUtils.colorScale(2)));
		colorScaleMenu.add(blueScale);

		customImagesMenu.add(colorScaleMenu);
		add(customImagesMenu);
		
		JMenu viewMenu = new JMenu("Vista");
		JMenu filterMenu = new JMenu("Banda mostrada");
		viewMenu.add(filterMenu);
		JMenuItem noFilter = new JMenuItem("RGB");
		activeFilterItem = noFilter;
		noFilter.setEnabled(false);
		noFilter.addActionListener(e->{
			activeFilterItem.setEnabled(true); 
			state.setImageFilter(null);
			activeFilterItem = noFilter;
			activeFilterItem.setEnabled(false);	
		});
		filterMenu.add(noFilter);
		JMenuItem redFilter = new JMenuItem("Solo Rojo");
		redFilter.addActionListener(e->{
			activeFilterItem.setEnabled(true); 
			state.setImageFilter(ImageUtils::redFilter);
			activeFilterItem = redFilter;
			activeFilterItem.setEnabled(false);
		});
		filterMenu.add(redFilter);
		JMenuItem greenFilter = new JMenuItem("Solo Verde");
		greenFilter.addActionListener(e-> {
			activeFilterItem.setEnabled(true); 
			state.setImageFilter(ImageUtils::greenFilter);
			activeFilterItem = greenFilter;
			activeFilterItem.setEnabled(false);
		});
		filterMenu.add(greenFilter);
		JMenuItem blueFilter = new JMenuItem("Solo Azul");
		blueFilter.addActionListener(e->{
			activeFilterItem.setEnabled(true); 
			state.setImageFilter(ImageUtils::blueFilter);
			activeFilterItem = blueFilter;
			activeFilterItem.setEnabled(false);
		});
		filterMenu.add(blueFilter);
		JMenuItem grayFilter = new JMenuItem("Grises");
		grayFilter.addActionListener(e->{
			activeFilterItem.setEnabled(true); 
			state.setImageFilter(ImageUtils::grayFilter);
			activeFilterItem = grayFilter;
			activeFilterItem.setEnabled(false);	
		});
		filterMenu.add(grayFilter);
		add(viewMenu);
		
	}

	private void saveImage(BiConsumer<File, Image> imageConverter) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("./images"));
		fileChooser.showDialog(null, "Guardar Imagen");
		if (fileChooser.getSelectedFile() != null) {
			imageConverter.accept(fileChooser.getSelectedFile(), state.getImage());
		}
	}

}
