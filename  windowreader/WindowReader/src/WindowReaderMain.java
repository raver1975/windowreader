import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import com.keyhook.KeyHook;
import com.keyhook.KeyHookData;
import com.keyhook.KeyHookEvent;
import com.keyhook.KeyHookListener;

public class WindowReaderMain implements KeyHookListener {

	static JFrame window;
	boolean ctrl = false;
	boolean shift = false;
	boolean alt = false;

	public WindowReaderMain() {
		// unloadJar();
		KeyHook kh = new KeyHook();
		kh.addKeyHookListener(this);
		window = new JFrame("Java windows demo");
		JLabel text = new JLabel("<HTML><center>Java<BR>rocks");
		MyCanvas canvas = new MyCanvas();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		canvas.setPreferredSize(screenSize);
		window.setUndecorated(true);
		window.setAlwaysOnTop(true);
		canvas.addMouseListener(canvas);
		canvas.addMouseMotionListener(canvas);
		window.setBackground(new Color(0, 0, 0, .01f));
		window.setOpacity(1f);
		text.setFont(new Font("Arial", 1, 60));
		text.setForeground(Color.red);

		window.setLocation(0, 0);
		window.add(canvas);
		window.pack();

		final JFrame j = new JFrame("Window Reader");
		JTextArea t = new JTextArea();
		JScrollPane sp = new JScrollPane(t);
		t.setText("This program lets you select an area of\nyour screen to convert to text using OCR\n\nPress CTRL - R to activate program.\nThen drag a region with your mouse.\n\nPress CTRL - Q to quit program.\n\n           paulklemstine@yahoo.com");
		j.getContentPane().add(sp);
		t.setCaretPosition(0);
		sp.scrollRectToVisible(new Rectangle(0, 0, 0, 0));
		j.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		j.pack();
		j.setSize(240, 190);
		j.setLocation((screenSize.width - j.getWidth()) / 2,
				(screenSize.height - j.getHeight()) / 2);
		j.setVisible(true);
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				j.dispose();
			}
		}).start();

		// window.setVisible(true);
	}

	/**
	 * @param args
	 * @throws AWTException
	 */
	public static void main(String[] args) throws AWTException {
		new WindowReaderMain();

	}

	private void unloadJar() {
		String dllName = "libtesseract302.dll";
		File tmpDir = new File(WindowReaderMain.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath());
		File tmpFile = new File(tmpDir.getParent(), dllName);
		if (tmpDir.exists())
			return;

		try {
			InputStream in = getClass().getResourceAsStream(dllName);
			OutputStream out = new FileOutputStream(tmpFile);

			byte[] buf = new byte[8192];
			int len;
			while ((len = in.read(buf)) != -1) {
				out.write(buf, 0, len);
			}

			in.close();
			out.close();

			// System.load(tmpFile.getAbsolutePath());

		} catch (Exception e) {
			// deal with exception
		} catch (Error e) {
			// deal with exception
		}
		dllName = "liblep168.dll";
		tmpDir = new File(WindowReaderMain.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath());
		tmpFile = new File(tmpDir.getParent(), dllName);
		if (tmpDir.exists())
			return;

		try {
			InputStream in = getClass().getResourceAsStream(dllName);
			OutputStream out = new FileOutputStream(tmpFile);

			byte[] buf = new byte[8192];
			int len;
			while ((len = in.read(buf)) != -1) {
				out.write(buf, 0, len);
			}

			in.close();
			out.close();

			// System.load(tmpFile.getAbsolutePath());

		} catch (Exception e) {
			// deal with exception
		} catch (Error e) {
			// deal with exception
		}

	}

	public static void capture(Rectangle screenRect) {
		window.repaint();
		window.setVisible(false);
		BufferedImage capture = null;
		try {
			capture = new Robot().createScreenCapture(screenRect);
		} catch (AWTException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		int w = capture.getWidth();
		int h = capture.getHeight();
		BufferedImage after = new BufferedImage(w * 3, h * 3,
				BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(3.0, 3.0);
		AffineTransformOp scaleOp = new AffineTransformOp(at,
				AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(capture, after);
		Tesseract instance = Tesseract.getInstance(); // JNA Interface Mapping
		// Tesseract1 instance = new Tesseract1(); // JNA Direct Mapping
		try {
			String result = instance.doOCR(after);
			final JFrame j = new JFrame("OCR");
//			float scaleFactor = .2f; // out of 100 ... obviously
//			RescaleOp op = new RescaleOp(scaleFactor, 0, null);
//			capture = op.filter(capture, null);
			JTextArea dd = new JTextArea();
			MyPanel pan = new MyPanel(capture);
			pan.setForeground(Color.white);
			pan.setBackground(new Color(0,0,0,1));
			JScrollPane sp = new JScrollPane(dd);
			dd.setText(result);
			j.getContentPane().add(sp,"West");
			pan.setPreferredSize(new Dimension(capture.getWidth(),capture.getHeight()));
			sp.setPreferredSize(new Dimension(200,300));
			dd.setPreferredSize(new Dimension(200,300));
			j.getContentPane().add(pan,"Center");
			j.setPreferredSize(new Dimension(200,300));
			dd.setCaretPosition(0);
			sp.scrollRectToVisible(new Rectangle(0, 0, 0, 0));
			j.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			j.pack();
			j.setSize(400, 300);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			j.setLocation((screenSize.width - j.getWidth()) / 2,
					(screenSize.height - j.getHeight()) / 2);
			j.setVisible(true);
			// System.out.println(result);
		} catch (TesseractException e1) {
			System.err.println(e1.getMessage());
		}
		// window.setVisible(true);
	}

	@Override
	public void KeyHookEventOccurred(KeyHookEvent evt) {
		String s = ((KeyHookData) evt.getSource()).vk;
		if (s.equals("<Ctrl>"))
			ctrl = true;
		if (s.equals("</Ctrl>"))
			ctrl = false;
		if (s.equals("<Shift>"))
			shift = true;
		if (s.equals("</Shift>"))
			shift = false;
		if (s.equals("<Alt>"))
			alt = true;
		if (s.equals("</Alt>"))
			alt = false;
		if (ctrl && s.equals("Q")) {
			final JFrame j = new JFrame("Window Reader");
			window.setVisible(false);
			JTextArea t = new JTextArea();
			JScrollPane sp = new JScrollPane(t);
			t.setText("        Quitting...");
			j.getContentPane().add(sp);
			t.setCaretPosition(0);
			sp.scrollRectToVisible(new Rectangle(0, 0, 0, 0));
			j.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			j.pack();
			j.setSize(100, 70);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			j.setLocation((screenSize.width - j.getWidth()) / 2,
					(screenSize.height - j.getHeight()) / 2);
			j.setVisible(true);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(0);
		}
		if (ctrl && s.equals("R"))
			window.setVisible(true);
	}
	/**
	* This method resizes the given image using Image.SCALE_SMOOTH.
	*
	* @param image the image to be resized
	* @param width the desired width of the new image. Negative values force the only constraint to be height.
	* @param height the desired height of the new image. Negative values force the only constraint to be width.
	* @param max if true, sets the width and height as maximum heights and widths, if false, they are minimums.
	* @return the resized image.
	*/
	public static Image resizeImage(Image image, int width, int height, boolean max) {
	  if (width < 0 && height > 0) {
	    return resizeImageBy(image, height, false);
	  } else if (width > 0 && height < 0) {
	    return resizeImageBy(image, width, true);
	  } else if (width < 0 && height < 0) {
	    return image;
	    //alternatively you can use System.err.println("");
	    //or you could just ignore this case
	  }
	  int currentHeight = image.getHeight(null);
	  int currentWidth = image.getWidth(null);
	  int expectedWidth = (height * currentWidth) / currentHeight;
	  //Size will be set to the height
	  //unless the expectedWidth is greater than the width and the constraint is maximum
	  //or the expectedWidth is less than the width and the constraint is minimum
	  int size = height;
	  if (max && expectedWidth > width) {
	    size = width;
	  } else if (!max && expectedWidth < width) {
	    size = width;
	  }
	  return resizeImageBy(image, size, (size == width));
	}

	/**
	* Resizes the given image using Image.SCALE_SMOOTH.
	*
	* @param image the image to be resized
	* @param size the size to resize the width/height by (see setWidth)
	* @param setWidth whether the size applies to the height or to the width
	* @return the resized image
	*/
	public static Image resizeImageBy(Image image, int size, boolean setWidth) {
	  if (setWidth) {
	    return image.getScaledInstance(size, -1, Image.SCALE_SMOOTH);
	  } else {
	    return image.getScaledInstance(-1, size, Image.SCALE_SMOOTH);
	  }
	}
	
	public static BufferedImage colorImage(BufferedImage loadImg, int red, int green, int blue) {
	    BufferedImage img = new BufferedImage(loadImg.getWidth(), loadImg.getHeight(),
	        BufferedImage.TRANSLUCENT);
	    Graphics2D graphics = img.createGraphics(); 
	    Color newColor = new Color(red, green, blue, 0 /* alpha needs to be zero */);
	    graphics.setXORMode(newColor);
	    graphics.drawImage(loadImg, null, 0, 0);
	    graphics.dispose();
	    return img;
	}
}
