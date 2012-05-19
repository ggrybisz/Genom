package genom.main;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class App {

	final int maxGeneration = 2000;
	final static int MAX_X_NUM = 1024;
	
	int[] data;

	private Plot plot;
	private JPanel sidePanel;
	private Evolution evolution;

	public App() {
		
		JFrame frame = new JFrame("Genom");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(450, 450);
		frame.setLocation(200, 200);
		frame.setLayout(new BorderLayout());
		frame.setVisible(true);
		plot = new Plot(400, 400);

		frame.add(plot, BorderLayout.CENTER);
		
		//sidePanel = new JPanel();
		//frame.add(sidePanel, BorderLayout.EAST);
		//sidePanel.setBorder(BorderFactory.createEtchedBorder());

		Function f = new Function();
		f.calculate(createXTable(MAX_X_NUM));

		plot.setPlotPoints(f.getPlotData());
	}
	
	public static int[] createXTable(int max) {
		int[] table = new int[max];

		for (int i = 0; i < max; i++) {
			table[i] = i;
		}
		return table;
	}

	public static void main(String[] args) {

		App app = new App();
		
		try {

			app.run();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void run() throws InterruptedException {

		evolution = new Evolution();

		double stop = 0;
		int generation = 1;
		evolution.createFirstGeneration();
		double oldAverage = evolution.getAverageFitness();

		do {
			plot.setPopulation(evolution.getPopulationAsPoints());
			Thread.sleep(500);

			System.out.println("GENERATION " + generation);
			evolution.crossingOver();
			evolution.newGenerationRoulette();

			int best = evolution.getBestSpecimen();
			System.out.println("MAX = " + best);

			double newAverage = evolution.getAverageFitness();
			System.out.println("AVERAGE = " + newAverage);

			stop = oldAverage - newAverage;
			stop = (stop < 0) ? (-stop) : stop;

			oldAverage = newAverage;
			generation++;
		} while (stop > 0 || generation >= maxGeneration);

	}

}
