package genom.main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Evolution {

	final int populationSize = 10;

	List<Chromosome> population;
	Random randomGenerator;

	public Evolution() {

		population = new ArrayList<Chromosome>();
		randomGenerator = new Random();
	}

	public void createFirstGeneration() {

		for (int i = 0; i < populationSize; i++) {
			int val = randomGenerator.nextInt(200) + 100;
			population.add(new Chromosome(val));
		}
		System.out.println("FIRST = "+population.toString());
		
		Collections.sort(population);
	}

	public void crossingOver() {

		List<Chromosome> children = new ArrayList<Chromosome>();

		for (int i = 0; i < populationSize; i++) {
			int one = randomGenerator.nextInt(populationSize - 1);
			int two = randomGenerator.nextInt(populationSize - 1);
			while (one == two)
				two = randomGenerator.nextInt(populationSize - 1);

			//System.out.println(one + " with " + two);
			Chromosome temp = population.get(one).makeSingleChildWith(population.get(two));
			children.add(temp);
		}

		population = children;
	}

	public void newGenerationRoulette() {

		List<Chromosome> newGeneration = new ArrayList<Chromosome>();
		
		int[] values = new int[populationSize];
		int sumOfValues = 0;

		int[] genes = new int[populationSize];
/*
		for (int i = 0; i < populationSize; i++) {
			genes[i] = oldGeneration[i].getGenesAsInt();
		}
		System.out.println("GEN = " + Arrays.toString(genes));
*/
		// selekcja - ruletka
		// wyliczenie wartości funkcji celu dla wszystkich osobników

		for (int i = 0; i < populationSize; i++) {
			int value = population.get(i).getFenotype();
			values[i] = value;
			sumOfValues += value;
		}
		System.out.println("VA = " + Arrays.toString(values));
		// obliczenie prawdopodobieństwa wyboru osobnika
		
		double[] probabilityTable = new double[populationSize];

		for (int i = 0; i < populationSize; i++) {
			probabilityTable[i] = (double) values[i] / (double) sumOfValues;
		}
		System.out.println("PRO = " + Arrays.toString(probabilityTable));
		// Obliczenie skumulowanego rozkładu prawd ?? chyba

		double[] qTable = new double[populationSize];

		for (int i = 0; i < populationSize; i++) {

			for (int j = 0; j <= i; j++) {
				qTable[i] += probabilityTable[j];
			}
		}
		System.out.println("QT = " + Arrays.toString(qTable));

		// generowanie n liczb losowych z zakresu 0,1
		randomGenerator = new Random();
		double[] randTable = new double[populationSize];

		for (int i = 0; i < populationSize; i++) {
			randTable[i] = randomGenerator.nextDouble();
		}
		System.out.println("RAN = " + Arrays.toString(randTable));
		// wybieramy osobniki

		int[] idTable = new int[populationSize];

		for (int i = 0; i < populationSize; i++) {
			for (int j = 0; j < populationSize; j++) {
				if (randTable[i] <= qTable[j]) {
					idTable[i] = j;
					break;
				}
			}
		}

		System.out.println("ID = " + Arrays.toString(idTable));
		// ============================

		for (int i = 0; i < populationSize; i++) {

			int chromosomeId = idTable[i];
			int newGenes = population.get(chromosomeId).getGenesAsInt();
			newGeneration.add(new Chromosome(newGenes));
		}

		population = newGeneration;
	}

	public int getBestSpecimen() {
		/*int max = 0;
		for (Chromosome chr : generation) {
			if (chr.getGenesAsInt() > max) {
				max = chr.getGenesAsInt();
			}
		}
		return max;*/
		
		return Collections.max(population).getGenesAsInt();
	}

	public double getAverageFitness() {

		double sum = 0;
		for (Chromosome chromosome : population) {
			int value = chromosome.getFenotype();
			sum += value;
		}
		sum = sum / populationSize;
		return sum;
	}

	public List<Point> getPopulationAsPoints() {

		List<Point> list = new ArrayList<Point>();

		for (Chromosome chromosome : population) {
			list.add(new Point(chromosome.getGenesAsInt(), chromosome.getFenotype()));
		}
		return list;
	}
}
