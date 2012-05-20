package genom.main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Evolution {

	final int populationSize = 10;
	int parentUsePercent = 10; //procent najlepszych rodziców przy elityzmie

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

	public void newGenerationTournament() {

		Collections.sort(population);
		List<Chromosome> newGeneration = new ArrayList<Chromosome>();

		while (newGeneration.size() < populationSize * (1.0 - (parentUsePercent / 100.0))) {
			int size = populationSize;

			// 4 różne osobniki

			int i = randomGenerator.nextInt(size);
			int j, k, l;
			j = k = l = i;
			while (j == i)
				j = randomGenerator.nextInt(size);
			while (k == i || k == j)
				k = randomGenerator.nextInt(size);
			while (l == i || l == j || k == l)
				l = randomGenerator.nextInt(size);

			Chromosome c1 = population.get(i);
			Chromosome c2 = population.get(j);
			Chromosome c3 = population.get(k);
			Chromosome c4 = population.get(l);

			int f1 = c1.getFenotype();
			int f2 = c2.getFenotype();
			int f3 = c3.getFenotype();
			int f4 = c4.getFenotype();

			Chromosome w1, w2;

			if (f1 > f2)
				w1 = c1;
			else
				w1 = c2;

			if (f2 > f4)
				w2 = c3;
			else
				w2 = c4;

			Chromosome child1, child2;

			Chromosome[] children = w1.makeChildrenWith(w2);

			child1 = children[0];
			child2 = children[1];

			double mutatePercent = 0.01;
			boolean m1 = randomGenerator.nextFloat() <= mutatePercent;
			boolean m2 = randomGenerator.nextFloat() <= mutatePercent;

			if (m1)
				child1.mutate();
			if (m2)
				child2.mutate();

			boolean isChild1Good = child1.getFenotype() >= w1.getFenotype();
			boolean isChild2Good = child2.getFenotype() >= w2.getFenotype();

			newGeneration.add(isChild1Good ? child1 : w1);
			newGeneration.add(isChild2Good ? child2 : w2);
		}
		//dodanie 
		int j = (int)(populationSize*parentUsePercent/100.0);
		for(int i=0;i<j;i++){
			newGeneration.add(population.get(i));
		}
		population = newGeneration;
		Collections.sort(population);
	}

	public void newGenerationRoulette() {

		List<Chromosome> newGeneration = new ArrayList<Chromosome>();

		System.out.println("POP = " + population.toString());

		int[] values = new int[populationSize];
		int sumOfValues = 0;

		int[] genes = new int[populationSize];

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
			list.add(new Point(chromosome.getGenesAsInt(), chromosome
					.getFenotype()));
		}
		return list;
	}
}
