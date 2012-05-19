package genom.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.List;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class Plot extends JComponent {

	private Image image;
	private Graphics2D graphics2d;
	int width, height;
	final int PAD = 20; // odległość lini bocznych od krawędzi

	double pointXDistance; // odległość na x pomiędzy punktami
	double scaleY; // skalowanie maksymalnej wartości

	final int dividers = 11; // 10+0

	private List<Point> plotPoints;
	private List<Point> population;

	public Plot(int w, int h) {

		setDoubleBuffered(false);

		setWidth(w);
		setHeight(h);

	}

	public void setWidth(int w) {
		width = w;
	}

	public void setHeight(int h) {
		height = h;
	}

	public double getScaleY() {
		return scaleY;
	}

	public void setScaleY(double scaleY) {
		this.scaleY = scaleY;
	}

	public void paint(Graphics g) {

		super.paintComponent(g);

		if (image == null) {
			image = createImage(width, height);
			graphics2d = (Graphics2D) image.getGraphics();
			graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			
		}
		clear();
		graphics2d.setPaint(Color.BLACK);
		drawSetup();
		drawPlot();
		drawPopulation();
		
		
		g.drawImage(image, 0, 0, null);
	}
	
	public void clear()
	{
		graphics2d.setPaint(getBackground());
		graphics2d.fillRect(0, 0, width, height);
		repaint();
		
	}

	private void drawSetup() {
		// linia pionowa - Y
		graphics2d.draw(new Line2D.Double(PAD, PAD, PAD, height - PAD));
		// linia pozioma - X
		graphics2d.draw(new Line2D.Double(PAD, height - PAD, width - PAD,
				height - PAD));

		for (int i = 0; i < dividers; i++) {
			double x1 = PAD + i * (width / dividers);
			double y1 = height - PAD;
			double x2 = x1;
			double y2 = y1 + 10;
			graphics2d.draw(new Line2D.Double(x1, y1, x2, y2));
			graphics2d.drawString(Integer.toString(i), (float)x1, (float)y2+10);
		}

		repaint();
	}

	public void drawScaledPoint(double x, double y, double size) {

		double scaledX = PAD + x * pointXDistance;
		double scaledY = height - PAD - scaleY * y;

		graphics2d.fill(new Ellipse2D.Double(scaledX - size / 2, scaledY - size
				/ 2, size, size));
	}

	public void drawPointList(List<Point> list, Color color, int size){
		
		graphics2d.setPaint(color);
		
		for (int i = 0; i < list.size(); i++) {

			// g2.drawString(Integer.toString(i), (int)x, h-10);

			// g2.drawString(Double.toString(list.get(i).getY()), 10, (int)y);
			drawScaledPoint(list.get(i).getX(), list.get(i).getY(), size);
		}
		repaint();
		
		
	}
	public void drawPlot() {

		drawPointList(this.plotPoints, Color.RED, 2);
		
	}
	public void drawPopulation(){
		drawPointList(this.population, Color.BLUE, 6);
	}

	private int getMax(List<Point> list) { // maksymalna wartość Y
		int max = -Integer.MAX_VALUE;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getY() > max)
				max = (int) list.get(i).getY();
		}
		return max;
	}

	public void setPlotPoints(List<Point> plotPoints) {
		this.plotPoints = plotPoints;

		pointXDistance = (double) (width - 2 * PAD) / (plotPoints.size());
		scaleY = (double) (height - 2 * PAD) / getMax(plotPoints);
	}


	public void setPopulation(List<Point> population) {
		this.population = population;
	}
	
}
