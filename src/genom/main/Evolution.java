package genom.main;

import java.util.Arrays;
import java.util.Random;

public class Evolution {

	final int SPECIMEN_COUNT = 10;

	public Chromosome[] createFirstGeneration() {

		Chromosome[] generation = new Chromosome[SPECIMEN_COUNT];

		Random randomGenerator = new Random();

		for (int i = 0; i < SPECIMEN_COUNT; i++) {
			int val = randomGenerator.nextInt(1023);
			System.out.println(val);
			generation[i] = new Chromosome(val);
		}

		return generation;
	}

	public Chromosome[] crossingOver(Chromosome[] generation) {

		Chromosome[] children = new Chromosome[SPECIMEN_COUNT];

		Random randomGenerator = new Random();

		for (int i = 0; i < SPECIMEN_COUNT; i++) {
			int one = randomGenerator.nextInt(SPECIMEN_COUNT - 1);
			int two = randomGenerator.nextInt(SPECIMEN_COUNT - 1);
			while(one==two)
				two = randomGenerator.nextInt(SPECIMEN_COUNT - 1);
			
			System.out.println(one + " with " + two);
			Chromosome temp = generation[one]
					.makeSingleChildWith(generation[two]);
			children[i] = temp;
		}

		return children;
	}

	public Chromosome[] newGeneration(Chromosome[] oldGeneration) {

		final int n = oldGeneration.length;

		int[] values = new int[n];
		int sumOfValues = 0;
		
		int[] genes = new int[n];
		
		for(int i=0;i<n;i++){
			genes[i] = oldGeneration[i].getGenesAsInt();
		}
		System.out.println("GEN = " + Arrays.toString(genes));

		// selekcja - ruletka
		// wyliczenie wartości funkcji celu dla wszystkich osobników

		for (int i = 0; i < n; i++) {
			int value = Function.xSquare(genes[i]);
			values[i] = value;
			sumOfValues += value;
		}
		System.out.println("VA = " + Arrays.toString(values));
		// obliczenie prawdopodobieństwa wyboru osobnika
		double[] probabilityTable = new double[n];

		for (int i = 0; i < n; i++) {
			probabilityTable[i] = (double) values[i] / (double) sumOfValues;
		}
		System.out.println("PRO = " + Arrays.toString(probabilityTable));
		// Obliczenie skumulowanego rozkładu prawd ?? chyba

		double[] qTable = new double[n];

		for (int i = 0; i < n; i++) {

			for (int j = 0; j <= i; j++) {
				qTable[i] += probabilityTable[j];
			}
		}
		System.out.println("QT = " + Arrays.toString(qTable));

		// generowanie n liczb losowych z zakresu 0,1
		Random doubleGenerator = new Random();
		double[] randTable = new double[n];

		for (int i = 0; i < n; i++) {
			randTable[i] = doubleGenerator.nextDouble();
		}
		System.out.println("RAN = " + Arrays.toString(randTable));
		// wybieramy osobniki

		int[] idTable = new int[n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (randTable[i] <= qTable[j]) {
					idTable[i] = j;
					break;
				}
			}
		}

		System.out.println("ID = " + Arrays.toString(idTable));
		// ============================

		Chromosome[] newGeneration = new Chromosome[n];

		for (int i = 0; i < n; i++) {

			int chromosomeId = idTable[i];
			int newGenes = oldGeneration[chromosomeId].getGenesAsInt();
			newGeneration[i] = new Chromosome(newGenes);
		}

		return newGeneration;
	}

	public int getBestSpecimen(Chromosome[] generation) {
		int max = 0;
		for (Chromosome chr : generation) {
			if (chr.getGenesAsInt() > max) {
				max = chr.getGenesAsInt();
			}
		}
		return max;
	}
}
