package com.github.beatngu13.knapsackproblem.mo.ga;

import static java.lang.Math.min;
import static java.lang.String.format;

import java.util.Random;

import io.jenetics.Gene;
import io.jenetics.util.MSeq;
import io.jenetics.util.RandomRegistry;

public class CustomSinglePointCrossover<
		G extends Gene<?, G>,
		C extends Comparable<? super C>
		>
		extends CustomMultiPointCrossover<G, C>
{

	/**
	 * Constructs an alterer with a given recombination probability.
	 *
	 * @param probability the crossover probability.
	 * @throws IllegalArgumentException if the {@code probability} is not in the
	 *         valid range of {@code [0, 1]}.
	 */
	public CustomSinglePointCrossover(final double probability) {
		super(probability, 1);
	}

	/**
	 * Create a new single point crossover object with crossover probability of
	 * {@code 0.05}.
	 */
	public CustomSinglePointCrossover() {
		this(0.05);
	}

	@Override
	protected int crossover(final MSeq<G> that, final MSeq<G> other) {
		final Random random = RandomRegistry.getRandom();

		final int index = random.nextInt(min(that.length(), other.length()));
		crossover(that, other, index);
		return 2;
	}

	// Package private for testing purpose.
	static <T> void crossover(
			final MSeq<T> that,
			final MSeq<T> other,
			final int index
	) {
		assert index >= 0 :
				format(
						"Crossover index must be within [0, %d) but was %d",
						that.length(), index
				);

		that.swap(index, min(that.length(), other.length()), other, index);
	}

	@Override
	public String toString() {
		return format("%s[p=%f]", getClass().getSimpleName(), _probability);
	}

}

