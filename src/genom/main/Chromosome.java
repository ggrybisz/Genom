package genom.main;

import java.util.Random;

public class Chromosome implements Comparable<Chromosome> {

	public static final int GENES_SIZE = 10;
	private final double min1 = 0;
	private final double max1 = 1023;
	private final double min2 = 0;
	private final double max2 = 10;

	private int[] genes;

	private Random randomGenerator;

	public Chromosome() {
		randomGenerator = new Random();
	}

	public Chromosome(int value) {
		this();
		setGenes(value);
	}

	public Chromosome(int[] genes) {
		this();
		setGenes(genes);
	}

	public void setGenes(int value) {

		genes = new int[10];
		for (int i = 9; i >= 0; i--) {
			boolean b = (value & (1 << i)) != 0;
			genes[i] = b ? 1 : 0;
		}
	}

	public void setGenes(int[] genes) {

		int[] gen = new int[genes.length];
		for (int i = 0; i < genes.length; i++)
			gen[i] = genes[i];

		this.genes = gen;
	}

	public int getRawGenes() {

		int value = 0;

		for (int i = 9; i >= 0; i--) {
			value = value + genes[i] * (1 << i);
		}

		return value;
	}

	public double getGenotype() {

		return changeRange(min1, max1, min2, max2, getRawGenes());
	}

	public int[] getGenesAsArray() {
		return genes;
	}

	public int getRawFenotype(){
		return Function.xSquare(getRawGenes());
	}
	public double getFenotype() {

		double x = changeRange(min1, max1, min2, max2, getRawGenes());

		return Function.xSquare(x);
	}

	// Mutacja - zamiana losowego genu
	public void mutate() {

		randomGenerator = new Random();
		int mutationPoint = randomGenerator.nextInt(GENES_SIZE);

		genes[mutationPoint] = genes[mutationPoint] == 0 ? 1 : 0;
		System.out.println("MUTATION occured at " + mutationPoint);
	}

	public Chromosome[] makeChildrenWith(Chromosome partner) {

		randomGenerator = new Random();
		//int cutPoint = randomGenerator.nextInt(GENES_SIZE - 2) + 1;
		int cutPoint = randomGenerator.nextInt(GENES_SIZE);

		int[] one = new int[GENES_SIZE];
		for (int i = 0; i < one.length; i++) {
			one[i] = this.genes[i];
		}
		int[] two = new int[GENES_SIZE];
		for (int i = 0; i < one.length; i++) {
			two[i] = partner.getGenesAsArray()[i];
		}

		int[] temp = new int[cutPoint + 1];

		for (int i = 0; i < temp.length; i++) {
			temp[i] = one[i];
			one[i] = two[i];
			two[i] = temp[i];
		}

		Chromosome[] children = new Chromosome[2];
		children[0] = new Chromosome(one);
		children[1] = new Chromosome(two);

		return children;
	}

	public Chromosome makeSingleChildWith(Chromosome partner) {

		randomGenerator = new Random();
		//int cutPoint = randomGenerator.nextInt(GENES_SIZE - 2) + 1;
		int cutPoint = randomGenerator.nextInt(GENES_SIZE);
		// int cutPoint = 3;

		int[] one = new int[10];
		for (int i = 0; i < one.length; i++) {
			one[i] = this.genes[i];
		}
		int[] two = new int[10];
		for (int i = 0; i < one.length; i++) {
			two[i] = partner.getGenesAsArray()[i];
		}
		for (int i = cutPoint; i < 10; i++) {

			one[i] = two[i];

		}

		Chromosome child = new Chromosome(one);

		return child;
	}

	private double changeRange(double oldMin, double oldMax, double newMin,
			double newMax, double value) {

		double oldRange = oldMax - oldMin;
		double newRange = newMax - newMin;

		double newValue = ((((value - oldMin) * newRange) / oldRange) + newMin);

		return newValue;
	}

	@Override
	public int compareTo(Chromosome compared) {

		double one = this.getFenotype();
		double two = compared.getFenotype();

		if (one < two)
			return 1;
		else if (one > two)
			return -1;
		else
			return 0;
	}

	@Override
	public String toString() {

		return Integer.toString(getRawGenes());
	}
}
