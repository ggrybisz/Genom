package genom.main;

import java.awt.Point;
import java.util.List;

public class ResultsContainer {

	private String generation;
	private String best;
	private String average;

	private List<Point> plotData;

	public void setGeneration(int generationCount) {
		this.generation = "Generation = " + generationCount;
	}

	public String getGeneration() {
		return generation;
	}

	public void setBest(int bestInt, double bestDouble) {

		this.best = "Best specimen = " + bestInt + " ("
				+ Function.roundToTwo(bestDouble) + ") ";
	}

	public String getBest() {
		return best;
	}

	public void setAverage(double newAverage) {

		this.average = "Average f(x) = " + Function.roundToTwo(newAverage);
	}

	public String getAverage() {
		return average;
	}

	public void setPlotData(List<Point> plotData) {
		this.plotData = plotData;
	}

	public List<Point> getPlotData() {
		return plotData;
	}

}
