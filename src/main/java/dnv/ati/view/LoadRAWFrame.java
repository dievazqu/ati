package dnv.ati.view;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JTextField;

import dnv.ati.model.State;
import dnv.ati.util.ImageUtils;

public class LoadRAWFrame extends JFrame{

	
	public LoadRAWFrame(){
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
				State.getInstance().setImage(ImageUtils.readFromRAW(fileChooser.getSelectedFile(), width, height));
			}
			dispose();
		});
		add(widthText);
		add(heightText);
		add(selectFileButton);

		setVisible(true);
	}
}
