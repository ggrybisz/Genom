package genom.main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class Evolution {

	protected int populationSize = 20;

	protected List<Chromosome> population;
	protected Random randomGenerator;

	public Evolution() {

		population = new ArrayList<Chromosome>();
		randomGenerator = new Random();
	}
	

	public void createFirstGeneration() {

		for (int i = 0; i < populationSize; i++) {
			// int val = randomGenerator.nextInt(200) + 100;
			int val = randomGenerator.nextInt(1023);
			population.add(new Chromosome(val));
		}
		System.out.println("FIRST = " + population.toString());

		Collections.sort(population);
	}

	public void crossingOver() {

		List<Chromosome> children = new ArrayList<Chromosome>();

		for (int i = 0; i < populationSize; i++) {
			int one = randomGenerator.nextInt(populationSize - 1);
			int two = randomGenerator.nextInt(populationSize - 1);
			while (one == two)
				two = randomGenerator.nextInt(populationSize - 1);

			// System.out.println(one + " with " + two);
			Chromosome temp = population.get(one).makeSingleChildWith(
					population.get(two));
			children.add(temp);
		}

		population = children;
	}

	public Chromosome getBestSpecimen() {
		Collections.sort(population);
		return population.get(0);//Collections.max(population);
	}

	public double getAverageFitness() {

		double sum = 0;
		for (Chromosome chromosome : population) {
			double value = chromosome.getFenotype();
			sum += value;
		}
		sum = sum / populationSize;
		return sum;
	}

	public List<Point> getPopulationAsPoints() {

		List<Point> list = new ArrayList<Point>();

		for (Chromosome chromosome : population) {
			list.add(new Point((int) chromosome.getRawGenes(),
					(int) chromosome.getRawFenotype()));
		}
		return list;
	}
	
	public abstract void newGeneration();
}
