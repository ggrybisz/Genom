package genom.main;

import java.util.List;

import javax.swing.SwingWorker;

class GeneticAlgorithmTask extends SwingWorker<Boolean, ResultsContainer> {

	private int maxGenerations = 2000;
	private int slow = 1000;

	private Evolution evolution;
	private Informable informable;

	public GeneticAlgorithmTask(Evolution evolution, Informable informable) {
		this.evolution = evolution;
		this.informable = informable;
	}

	@Override
	protected Boolean doInBackground() throws Exception {

		double stop = 0;
		int generation = 1;
		evolution.createFirstGeneration(); //pierwsza populacja
		double oldAverage = evolution.getAverageFitness();

		do {
			ResultsContainer results = new ResultsContainer();
			results.setPlotData(evolution.getPopulationAsPoints());

			results.setGeneration(generation);

			evolution.newGeneration(); //nowa populacja

			int best = evolution.getBestSpecimen().getRawGenes();
			double bestDouble = evolution.getBestSpecimen().getGenotype();

			results.setBest(best, bestDouble);

			System.out.println("MAX = " + best);

			double newAverage = evolution.getAverageFitness();
			System.out.println("AVERAGE = " + newAverage);

			results.setAverage(newAverage);

			publish(results);

			stop = oldAverage - newAverage;
			stop = (stop < 0) ? (-stop) : stop;

			oldAverage = newAverage;
			Thread.sleep(slow);
			generation++;

			

		} while (stop > 0.01 && generation <= maxGenerations);

		return true;
	}

	@Override
	protected void process(List<ResultsContainer> chunks) {
		for (ResultsContainer results : chunks)
			informable.resultsChanged(results);
	}

	public void setFastMode(boolean fast) {
		if (fast)
			slow = 0;
		else
			slow = 1000;
	}
}
