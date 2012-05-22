package genom.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JSlider;

@SuppressWarnings("serial")
public class Plot extends JComponent {

	private Image image;
	private Graphics2D graphics2d;
	private int width, height;
	private final int PAD = 20; // odległość lini bocznych od krawędzi

	private double pointXDistance; // odległość na x pomiędzy punktami
	private int maxYValue = 0;
	private double scaleY; // skalowanie maksymalnej wartości

	final int dividers = 11; // 10+0

	private List<Point> plotPoints;
	private List<Point> population;

	public Plot(int w, int h) {

		plotPoints = new ArrayList<Point>();
		population = new ArrayList<Point>();

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

	public void paintComponent(Graphics g) {

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

	public void clear() {
		graphics2d.setPaint(getBackground());
		graphics2d.fillRect(0, 0, width, height);
		repaint();

	}

	private void drawSetup() {
		// linia pionowa - Y
		graphics2d.draw(new Line2D.Double(PAD, PAD, PAD, height - PAD));
		graphics2d.draw(new Line2D.Double(PAD, PAD, PAD-10, PAD));
		graphics2d.drawString(Integer.toString(maxYValue), (float) PAD -10,(float) PAD-5);
		// linia pozioma - X
		graphics2d.draw(new Line2D.Double(PAD, height - PAD, width - PAD,
				height - PAD));

		for (int i = 0; i < dividers; i++) {
			double x1 = PAD + i * (width / dividers);
			double y1 = height - PAD;
			double x2 = x1;
			double y2 = y1 + 10;
			graphics2d.draw(new Line2D.Double(x1, y1, x2, y2));
			graphics2d.drawString(Integer.toString(i), (float) x1,
					(float) y2 + 10);
		}

		repaint();
	}

	public void drawScaledPoint(double x, double y, double size) {

		double scaledX = PAD + x * pointXDistance;
		double scaledY = height - PAD - scaleY * y;

		graphics2d.fill(new Ellipse2D.Double(scaledX - size / 2, scaledY - size
				/ 2, size, size));
	}

	public void drawPointList(List<Point> list, Color color, int size) {

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

	public void drawPopulation() {
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
		maxYValue = getMax(plotPoints);
		scaleY = (double) (height - 2 * PAD) / maxYValue;
	}

	public void setPopulation(List<Point> population) {
		this.population = population;
	}

	public void setMaxYValue(int maxYValue) {
		this.maxYValue = maxYValue;
		scaleY = (double) (height - 2 * PAD) / this.maxYValue;
	}

	public int getMaxYValue() {
		return maxYValue;
	}

}
