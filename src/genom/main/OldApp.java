package genom.main;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class OldApp extends JFrame{

	final int maxGeneration = 2000;
	final static int MAX_X_NUM = 1024;
	
	int plotSize = 400;
	int appHeight = 450;
	int appWidth = plotSize+300;
	
	int[] data;

	private JFrame frame;
	private Plot plot;
	private JPanel sidePanel;
	private Evolution evolution;
	private JPanel contentPane;
	private JLabel generationLabel;
	private JLabel bestLabel;
	private JLabel avgLabel;

	public OldApp() {
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
		generationLabel.setBounds(10, 11, 86, 23);
		outputPanel.add(generationLabel);
		
		bestLabel = new JLabel("Best specimen = ");
		bestLabel.setBounds(10, 45, 200, 23);
		outputPanel.add(bestLabel);
		
		avgLabel = new JLabel("Average f(x) = ");
		avgLabel.setBounds(10, 85, 200, 23);
		outputPanel.add(avgLabel);

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

		Function f = new Function();
		f.calculate(createXTable(MAX_X_NUM));

		plot.setPlotPoints(f.getPlotData());
		this.setVisible(true);
	}

	public static int[] createXTable(int max) {
		int[] table = new int[max];

		for (int i = 0; i < max; i++) {
			table[i] = i;
		}
		return table;
	}

	public static void main(String[] args) {

		OldApp app = new OldApp();
		
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

			generationLabel.setText("Generation = "+ generation);
			System.out.println("GENERATION " + generation);
			evolution.crossingOver();
			evolution.newGenerationRoulette();
			//evolution.newGenerationTournament();
			int best = evolution.getBestSpecimen();
			System.out.println("MAX = " + best);

			bestLabel.setText("Best specimen = "+ best);
			
			double newAverage = evolution.getAverageFitness();
			System.out.println("AVERAGE = " + newAverage);
			
			avgLabel.setText("Average f(x) = "+ newAverage);
			
			stop = oldAverage - newAverage;
			stop = (stop < 0) ? (-stop) : stop;

			oldAverage = newAverage;
			generation++;
		} while (stop > 0 && generation <= maxGeneration);

	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
