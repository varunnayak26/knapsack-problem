package com.github.beatngu13.knapsackproblem.mo.ga;

import static java.lang.Math.min;

import java.util.Arrays;
import java.util.Random;

import io.jenetics.Chromosome;
import io.jenetics.Gene;
import io.jenetics.Genotype;
import io.jenetics.Phenotype;
import io.jenetics.Recombinator;
import io.jenetics.util.MSeq;
import io.jenetics.util.RandomRegistry;

public abstract class CustomCrossover <
		G extends Gene<?, G>,
		C extends Comparable<? super C>
		>
		extends Recombinator<G, C> {

	/**
	 * Constructs an alterer with a given recombination probability.
	 *
	 * @param probability the recombination probability
	 * @throws IllegalArgumentException if the {@code probability} is not in the valid range of {@code [0, 1]}
	 */
	protected CustomCrossover( final double probability ) {
		super( probability, 2 );
	}

	@Override
	protected final int recombine(
			final MSeq<Phenotype<G, C>> population,
			final int[] individuals,
			final long generation
	) {
		assert individuals.length == 2 : "Required order of 2";
		System.out.printf( "generation=%d, individuals = %s\n",generation,Arrays.toString(individuals) );
		final Random random = RandomRegistry.getRandom();

		final Phenotype<G, C> pt1 = population.get( individuals[0] );
		final Phenotype<G, C> pt2 = population.get( individuals[1] );
		final Genotype<G> gt1 = pt1.getGenotype();
		final Genotype<G> gt2 = pt2.getGenotype();

		//Choosing the Chromosome index for crossover.
		final int chIndex = random.nextInt( min( gt1.length(), gt2.length() ) );

		final MSeq<Chromosome<G>> c1 = gt1.toSeq().copy();
		final MSeq<Chromosome<G>> c2 = gt2.toSeq().copy();
		final MSeq<G> genes1 = c1.get( chIndex ).toSeq().copy();
		final MSeq<G> genes2 = c2.get( chIndex ).toSeq().copy();

		crossover( genes1, genes2 );

		c1.set( chIndex, c1.get( chIndex ).newInstance( genes1.toISeq() ) );
		c2.set( chIndex, c2.get( chIndex ).newInstance( genes2.toISeq() ) );

		//Creating two new Phenotypes and exchanging it with the old.
		population.set(
				individuals[0],
				Phenotype.of( Genotype.of( c1 ), generation )
		);
		population.set(
				individuals[1],
				Phenotype.of( Genotype.of( c2 ), generation )
		);

		return getOrder();
	}

	/**
	 * Template method which performs the crossover. The arguments given are mutable non null arrays of the same
	 * length.
	 *
	 * @param that  the genes of the first chromosome
	 * @param other the genes of the other chromosome
	 * @return the number of altered genes
	 */
	protected abstract int crossover( final MSeq<G> that, final MSeq<G> other );
}