package genom.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TournamentEvolution extends Evolution {

	int parentUsePercent = 10; // procent najlepszych rodziców przy elityzmie

	public TournamentEvolution() {
		super();
	}

	public TournamentEvolution(int populationSize) {
		this();
		this.populationSize = populationSize;
	}

	@Override
	public void newGeneration() {

		Collections.sort(population);
		System.out.println(population.toString());
		List<Chromosome> newGeneration = new ArrayList<Chromosome>();

		int maxTournaments = (int) (populationSize * (1.0 - (parentUsePercent / 100.0)));
		if((maxTournaments%2)!=0)
			maxTournaments--;
		int maxCarriedOver = populationSize - maxTournaments;
		// pętla - zapełniamy nową populację
		while (newGeneration.size() < maxTournaments) {
			int size = populationSize;

			// 4 różne osobniki

			int one = randomGenerator.nextInt(size);
			int two, three, four;
			two = three = four = one;
			while (two == one)
				two = randomGenerator.nextInt(size);
			while (three == one || three == two)
				three = randomGenerator.nextInt(size);
			while (four == one || four == two || three == four)
				four = randomGenerator.nextInt(size);

			Chromosome c1 = population.get(one);
			Chromosome c2 = population.get(two);
			Chromosome c3 = population.get(three);
			Chromosome c4 = population.get(four);

			double f1 = c1.getFenotype();
			double f2 = c2.getFenotype();
			double f3 = c3.getFenotype();
			double f4 = c4.getFenotype();

			Chromosome w1, w2;

			// turniej - porównujemy dwóch osobników w parze, wybieramy lepszego
			if (f1 > f2)
				w1 = c1;
			else
				w1 = c2;

			if (f3 > f4)
				w2 = c3;
			else
				w2 = c4;

			Chromosome child1, child2;

			// krzyżujemy ze sobą zwycięzców
			Chromosome[] children = w1.makeChildrenWith(w2);

			// otrzymujemy dwa osobniki
			child1 = children[0];
			child2 = children[1];

			// mutacja
			double mutatePercent = 0.01;
			boolean m1 = randomGenerator.nextFloat() <= mutatePercent;
			boolean m2 = randomGenerator.nextFloat() <= mutatePercent;

			if (m1)
				child1.mutate();
			if (m2)
				child2.mutate();

			// czy dziecko jest lepsze od rodzica?
			boolean isChild1Good = child1.getFenotype() >= w1.getFenotype();
			boolean isChild2Good = child2.getFenotype() >= w2.getFenotype();

			// jeśli tak - weź dziecko, jeśli nie - rodzica
			newGeneration.add(isChild1Good ? child1 : w1);
			newGeneration.add(isChild2Good ? child2 : w2);
		}
		// przeniesienie najepszych rodziców z poprzedniej populacji
		
		for (int i = 0; i < maxCarriedOver; i++) {
			newGeneration.add(population.get(i));
		}
		population = newGeneration;
		Collections.sort(population);

	}

}
