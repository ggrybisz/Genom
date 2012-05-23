package genom.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RouletteEvolution extends Evolution {

	public RouletteEvolution() {
		super();
	}
	
	public RouletteEvolution(int populationSize){
		this();
		this.populationSize = populationSize;
	}
	
	@Override
	public void newGeneration() {
		List<Chromosome> newGeneration = new ArrayList<Chromosome>();

		System.out.println("POP = " + population.toString());

		double[] values = new double[populationSize];
		int sumOfValues = 0;

		//int[] genes = new int[populationSize];

		// selekcja - ruletka
		// wyliczenie wartości funkcji celu dla wszystkich osobników

		for (int i = 0; i < populationSize; i++) {
			double value = population.get(i).getFenotype();
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
			int newGenes = population.get(chromosomeId).getRawGenes();
			newGeneration.add(new Chromosome(newGenes));
		}

		population = newGeneration;
	}

}
