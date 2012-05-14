package genom.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class App extends JPanel {

	//final int GEN_COUNT = 100;
	final int MAX_X_NUM = 1024;
	int[] data;
	final int PAD = 50;

	// final int maxX = 10;

	private int[] createXTable(int max) {
		int[] table = new int[max];

		for (int i = 0; i < max; i++) {
			table[i] = i;
		}
		return table;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		int w = getWidth();
		int h = getHeight();
		// y.
		g2.draw(new Line2D.Double(PAD, PAD, PAD, h - PAD));
		// x.
		g2.draw(new Line2D.Double(PAD, h - PAD, w - PAD, h - PAD));

		// Mark data points.
		g2.setPaint(Color.red);

		data = createXTable(MAX_X_NUM);
		Function fun = new Function();
		fun.calculate(data);

		List<Point> list = fun.getPlotData();

		double subX = (double) (w - 2 * PAD) / (data.length);
		double scale = (double) (h - 2 * PAD) / getMax(list);

		for (int i = 0; i < data.length; i++) {

			double x = PAD + list.get(i).getX() * subX;
			// g2.drawString(Integer.toString(i), (int)x, h-10);
			double y = h - PAD - scale * list.get(i).getY();
			// g2.drawString(Double.toString(list.get(i).getY()), 10, (int)y);
			g2.fill(new Ellipse2D.Double(x - 2, y - 2, 2, 2));
		}
	}

	// maksymalna wartość Y
	private int getMax(List<Point> list) {
		int max = -Integer.MAX_VALUE;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getY() > max)
				max = (int) list.get(i).getY();
		}
		return max;
	}

	public static void main(String[] args) {

		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		App app = new App();
		f.add(app);
		f.setSize(400, 400);
		f.setLocation(200, 200);
		//f.setVisible(true);
		app.performEvolution();
	}

	private void performEvolution() {

		Evolution evo = new Evolution();

		Chromosome[] generation = evo.createFirstGeneration();
		int oldBest = evo.getBestSpecimen(generation);
		int stop = oldBest;
		int i=1;	
		
		while(stop>=1) {
			System.out.println("Generation " + i++);
			Chromosome[] cross = evo.crossingOver(generation);
			generation = evo.newGeneration(cross);
			int newBest = evo.getBestSpecimen(generation);
			System.out.println("MAX = " + newBest);

			stop = oldBest - newBest;
			stop = (stop < 0) ? (-stop) : stop;

			
				oldBest = newBest;
		}

	}

}
