package genom.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TournamentEvolution extends Evolution {

	int parentUsePercent = 10; // procent najlepszych rodziców przy elityzmie
	
	@Override
	public void newGeneration() {

		Collections.sort(population);
		List<Chromosome> newGeneration = new ArrayList<Chromosome>();

		while (newGeneration.size() < populationSize
				* (1.0 - (parentUsePercent / 100.0))) {
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

			double f1 = c1.getFenotype();
			double f2 = c2.getFenotype();
			double f3 = c3.getFenotype();
			double f4 = c4.getFenotype();

			Chromosome w1, w2;

			if (f1 > f2)
				w1 = c1;
			else
				w1 = c2;

			if (f3 > f4)
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
		// dodanie
		int j = (int) (populationSize * parentUsePercent / 100.0);
		for (int i = 0; i < j; i++) {
			newGeneration.add(population.get(i));
		}
		population = newGeneration;
		Collections.sort(population);

	}

}
