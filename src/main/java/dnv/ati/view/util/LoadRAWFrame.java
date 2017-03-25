package dnv.ati.view.util;

import java.io.File;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;

import dnv.ati.model.Image;
import dnv.ati.util.ImageUtils;

@SuppressWarnings("serial")
public class LoadRAWFrame extends JFrame{

	public LoadRAWFrame(File file, Consumer<Image> consumer){
		super("Seleccione tamaño del .raw");
		setSize(400, 170);
		setLocationRelativeTo(null);
		setLayout(null);
		JFormattedTextField widthText = new JFormattedTextField(new Integer(0));
		widthText.setBounds(130, 20, 50, 30);
		widthText.setToolTipText("El valor en pixeles del ancho de la imagen a cargar");
		JFormattedTextField heightText = new JFormattedTextField(new Integer(0));
		heightText.setBounds(210, 20, 50, 30);
		heightText.setToolTipText("El valor en pixeles del alto de la imagen a cargar");
		JButton selectFileButton = new JButton("Cargar imagen");
		selectFileButton.setBounds(110, 80, 170, 30);
		selectFileButton.addActionListener(ee -> {
			int width, height;
			try{
				width = Integer.parseInt(widthText.getText());
				height = Integer.parseInt(heightText.getText());
			}catch(Exception e){
				return;
			}
			if (file != null) {
				consumer.accept(ImageUtils.readFromRAW(file, width, height));
			}
			dispose();
		});
		add(widthText);
		add(heightText);
		add(selectFileButton);

		setVisible(true);
	}
}
