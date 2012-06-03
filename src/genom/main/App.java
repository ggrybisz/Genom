package genom.main;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class App extends JFrame {

	private Evolution evolution;
	private GeneticAlgorithmTask genetics;
	final int maxGeneration = 2000;
	final static int MAX_X_NUM = 1024;

	int plotSize = 400;
	int appHeight = 450;
	int appWidth = plotSize + 300;

	int initialPopulationSize = 20;

	int[] data;

	private JPanel contentPane;
	private Plot plot;
	private JLabel generationLabel;
	private JLabel bestLabel;
	private JLabel avgLabel;
	private JCheckBox fastModeCheckbox;

	private boolean isFast = false;
	private JComboBox selectionMethodBox;
	private JTextField populationSizeTextField;
	private JProgressBar progressBar;
	private JButton startButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App frame = new App();
					frame.setVisible(true);
					// frame.search();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public App() {

		try {
			javax.swing.UIManager.setLookAndFeel(UIManager
					.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(App.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(App.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(App.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(App.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		}

		setTitle("Genom");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 740, 470);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel outputPanel = new JPanel();
		outputPanel
				.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		outputPanel.setBounds(459, 10, 254, 171);
		contentPane.add(outputPanel);
		outputPanel.setLayout(null);

		generationLabel = new JLabel("Generation = ");
		generationLabel.setBounds(16, 21, 120, 14);
		outputPanel.add(generationLabel);

		bestLabel = new JLabel("Best specimen = ");
		bestLabel.setBounds(16, 46, 150, 14);
		outputPanel.add(bestLabel);

		avgLabel = new JLabel("Average f(x) = ");
		avgLabel.setBounds(16, 71, 150, 14);
		outputPanel.add(avgLabel);

		progressBar = new JProgressBar();
		int barWidth = 100;
		progressBar.setBounds((outputPanel.getWidth() / 2) - (barWidth / 2),
				132, barWidth, 14);
		outputPanel.add(progressBar);

		JPanel plotPanel = new JPanel();
		plotPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		plotPanel.setBounds(10, 10, 439, 410);
		contentPane.add(plotPanel);
		plotPanel.setLayout(new BorderLayout(0, 0));

		plot = new Plot(plotSize, plotSize);
		plot.setBounds(0, 0, plotSize, plotSize);
		plotPanel.add(plot, BorderLayout.CENTER);

		JPanel optionPanel = new JPanel();
		optionPanel
				.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		optionPanel.setBounds(459, 193, 254, 227);
		contentPane.add(optionPanel);

		fastModeCheckbox = new JCheckBox("Fast mode");
		fastModeCheckbox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {

				isFast = (arg0.getStateChange() == ItemEvent.SELECTED) ? true
						: false;
			}
		});
		fastModeCheckbox.setBounds(35, 115, 128, 23);
		optionPanel.add(fastModeCheckbox);

		// =========================================================
		final Informable informable = new Informable() {

			@Override
			public void resultsChanged(ResultsContainer results) {
				generationLabel.setText(results.getGeneration());
				bestLabel.setText(results.getBest());
				avgLabel.setText(results.getAverage());
				plot.setPopulation(results.getPlotData());
			}
		};

		startButton = new JButton("START");
		startButton.setBounds(35, 157, 73, 34);
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				
				
				if (startButton.getText().equals("START")) {
					int populationSize = Integer
							.parseInt(populationSizeTextField.getText());

					if (selectionMethodBox.getSelectedIndex() == 0)
						evolution = new RouletteEvolution(populationSize);
					else if (selectionMethodBox.getSelectedIndex() == 1)
						evolution = new TournamentEvolution(populationSize);

					genetics = new GeneticAlgorithmTask(
							evolution, informable) {
						@Override
						protected void done() {
							startButton.setText("START");
							progressBar.setIndeterminate(false);
						}
					};
					genetics.setFastMode(isFast);

					startButton.setText("CANCEL");
					progressBar.setIndeterminate(true);
					genetics.execute();
				}
				else
				{
					genetics.cancel(true);
				}
			}
		});
		optionPanel.setLayout(null);

		optionPanel.add(startButton);

		selectionMethodBox = new JComboBox();
		selectionMethodBox.setModel(new DefaultComboBoxModel(new String[] {
				"Roulette", "Tournament" }));
		selectionMethodBox.setBounds(35, 76, 108, 20);
		optionPanel.add(selectionMethodBox);

		JLabel populationSizeLabel = new JLabel("Population size:");
		populationSizeLabel.setBounds(35, 28, 86, 14);
		optionPanel.add(populationSizeLabel);

		populationSizeTextField = new JTextField();
		populationSizeTextField.setBounds(131, 25, 86, 20);
		optionPanel.add(populationSizeTextField);
		populationSizeTextField.setColumns(10);
		populationSizeTextField
				.setText(Integer.toString(initialPopulationSize));

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
}
