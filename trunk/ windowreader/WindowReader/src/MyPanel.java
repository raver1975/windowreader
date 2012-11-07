import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class MyPanel extends JPanel {

	private BufferedImage backgroundImage;

	// Some code to initialize the background image.
	// Here, we use the constructor to load the image. This
	// can vary depending on the use case of the panel.
	public MyPanel(String fileName) throws IOException {
		super();
		backgroundImage = ImageIO.read(new File(fileName));
	}

	public MyPanel(BufferedImage capture) {
		super();
		backgroundImage = capture;
	}

	public void paintComponent(Graphics g) {
		g.drawImage(backgroundImage, 0, 0, null);
		super.paintComponent(g);
	}
}