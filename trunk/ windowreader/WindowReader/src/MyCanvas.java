import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class MyCanvas extends JPanel implements MouseListener,
		MouseMotionListener {

	int x1 = 0;
	int y1 = 0;

	// static BufferedImage capture = null;

	public MyCanvas() {
		super();
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.red);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		g.drawRect(0, 0, screenSize.width - 1, screenSize.height - 1);
		g.drawRect(1, 1, screenSize.width - 3, screenSize.height - 3);
		((Graphics2D) g).setComposite(AlphaComposite.getInstance(
				AlphaComposite.DST_IN, .7f));
		Rectangle2D.Double rect = new Rectangle2D.Double(0, 0, this.getWidth(),
				this.getHeight());
		((Graphics2D) g).fill(rect);
		// g.setColor(new Color(0,0,0,1f));
		// g.fillRect(0, 0,this.getWidth(), this.getHeight());
		// g.setColor(new Color(0,0,0,.01f));
		// g.fillRect(0, 0,this.getWidth(), this.getHeight());

		// if (capture!=null){g.drawImage(capture,100,100, null);
		// g.drawRect(100, 100, capture.getWidth() - 1, capture.getHeight() -
		// 1);}
		// g.setColor(Color.blue);
		// g.drawString("o", x, y);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		x1 = e.getX();
		y1 = e.getY();

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x3 = e.getX();
		int y3 = e.getY();
		if (x1 == x3 || y1 == y3)
			return;
		int x2 = x3;
		int y2 = y3;

		if (x3 < x1) {
			x2 = x1;
			x1 = x3;
		}
		if (y3 < y1) {
			y2 = y1;
			y1 = y3;
		}
		Rectangle screenRect = new Rectangle(x1, y1, x2 - x1, y2 - y1);
		WindowReaderMain.capture(screenRect);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Graphics graphics = this.getGraphics();
		graphics.setColor(new Color(1, 0, 0, 0.5f));
		graphics.fillRect(x1, y1, e.getX() - x1, e.getY() - y1);
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Graphics graphics = this.getGraphics();
		graphics.setColor(Color.blue);
		graphics.drawOval(e.getX() - 2, e.getY() - 2, 5, 5);
		//graphics.drawString("Drag a window to OCR", e.getX() + 5, e.getY() - 10);
		repaint();
	}

}
