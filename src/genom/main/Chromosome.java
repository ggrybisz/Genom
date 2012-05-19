package genom.main;

import java.util.Random;

public class Chromosome implements Comparable<Chromosome>{

	public static final int GENES_SIZE = 10; 
		
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

		int [] gen = new int [genes.length] ;
		for(int i = 0; i<genes.length;i++)
			gen[i] = genes[i];
		
		this.genes = gen;
	}

	public int getGenesAsInt() {

		int value = 0;

		for (int i = 9; i >= 0; i--) {
			value = value + genes[i] * (1 << i);
		}

		return value;
	}

	public int[] getGenesAsArray() {
		return genes;
	}
	
	public int getFenotype(){
		return Function.xSquare(this.getGenesAsInt());
	}

	// Mutacja - zamiana losowego genu
	public void mutate() {

		randomGenerator = new Random();
		int mutationPoint = randomGenerator.nextInt(9);

		genes[mutationPoint] = genes[mutationPoint] == 0 ? 1 : 0;
		System.out.println("MUTATION occured at " + mutationPoint);
	}

	public Chromosome[] makeChildrenWith(Chromosome partner) {

		randomGenerator = new Random();
		int cutPoint = randomGenerator.nextInt(8);

		int[] one = new int[10];
		for (int i = 0; i < one.length; i++) {
			one[i] = this.genes[i];
		}
		int[] two = new int[10];
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
	
	public Chromosome makeSingleChildWith(Chromosome partner){
		
		randomGenerator = new Random();
		int cutPoint = randomGenerator.nextInt(9);
		//int cutPoint = 3;

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

	@Override
	public int compareTo(Chromosome compared) {
		
		int one = this.getFenotype();
		int two = compared.getFenotype();
		
		if(one<two)
			return 1;
		else if(one>two)
			return -1;
		else
			return 0;
	}
	@Override
	public String toString() {
		
		return Integer.toString(getGenesAsInt()); 
	}
}
