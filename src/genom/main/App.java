package genom.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class App {

	final static int MAX_X_NUM = 1024;
	int[] data;

	static Plot plot;
	static JPanel sidePanel;

	public static int[] createXTable(int max) {
		int[] table = new int[max];

		for (int i = 0; i < max; i++) {
			table[i] = i;
		}
		return table;
	}
	
	public static void main(String[] args) {

		App app = new App();
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
		
		try {
			app.performEvolution();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	private void performEvolution() throws InterruptedException {

		Evolution evo = new Evolution();

		Chromosome[] generation = evo.createFirstGeneration();
		int best = evo.getBestSpecimen(generation);
		double oldAverage = evo.getAverageFitness(generation);
		double stop = oldAverage;
		int i=1;	
		//g2.setPaint(Color.red);
		
		while(stop>0) {
			
			plot.setPopulation(evo.getPopulationAsPoints(generation));
			Thread.sleep(500);
			
			System.out.println("Generation " + i++);
			
			Chromosome[] cross = evo.crossingOver(generation);
			generation = evo.newGeneration(cross);
			double newAverage = evo.getAverageFitness(generation);
			System.out.println("AVERAGE = " + newAverage);
			best = evo.getBestSpecimen(generation);
			System.out.println("MAX = " + best);
			
			stop = oldAverage - newAverage;
			stop = (stop < 0) ? (-stop) : stop;
				
				oldAverage = newAverage;
		}

	}

}
