package genom.main;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Panel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class App extends JFrame {

	private Evolution evolution;
	final int maxGeneration = 2000;
	final static int MAX_X_NUM = 1024;
	
	int plotSize = 400;
	int appHeight = 450;
	int appWidth = plotSize+300;
	
	int[] data;

	private JPanel contentPane;
	private Plot plot;
	private JLabel generationLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		//EventQueue.invokeLater(new Runnable() {
			//public void run() {
				try {
					App frame = new App();
					frame.setVisible(true);
				//	frame.search();
				} catch (Exception e) {
					e.printStackTrace();
				}
			//}
		//});
	}

	/**
	 * Create the frame.
	 */
	public App() {
		setTitle("Genom");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 470);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel outputPanel = new JPanel();
		outputPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null,
				null));
		outputPanel.setBounds(420, 11, 254, 171);
		contentPane.add(outputPanel);
		outputPanel.setLayout(null);

		generationLabel = new JLabel("Genration = ");
		generationLabel.setBounds(10, 11, 111, 23);
		outputPanel.add(generationLabel);
		
		JLabel bestLabel = new JLabel("Best specimen");
		bestLabel.setBounds(10, 45, 86, 23);
		outputPanel.add(bestLabel);

		JPanel plotPanel = new JPanel();
		plotPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		plotPanel.setBounds(10, 10, 400, 410);
		contentPane.add(plotPanel);
		plotPanel.setLayout(new BorderLayout(0, 0));
		
		plot = new Plot(plotSize, plotSize);
		plot.setBounds(0, 0, plotSize, plotSize);
		plotPanel.add(plot, BorderLayout.CENTER);
		
		JPanel optionPanel = new JPanel();
		optionPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		optionPanel.setBounds(420, 193, 254, 227);
		contentPane.add(optionPanel);
		
		JButton startButton = new JButton("START");
	
		optionPanel.add(startButton);
		
		
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

	public void search() throws InterruptedException {

		evolution = new Evolution();

		double stop = 0;
		int generation = 1;
		evolution.createFirstGeneration();
		double oldAverage = evolution.getAverageFitness();

		do {
			plot.setPopulation(evolution.getPopulationAsPoints());
			Thread.sleep(500);

			System.out.println("GENERATION " + generation);
			getGenerationLabel().setText("Generation = " +generation);
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
	public JLabel getGenerationLabel() {
		return generationLabel;
	}
}
