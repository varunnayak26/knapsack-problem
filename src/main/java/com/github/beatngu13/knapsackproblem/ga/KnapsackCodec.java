package com.github.beatngu13.knapsackproblem.ga;

import java.util.function.Function;
import java.util.stream.Collectors;

import com.github.beatngu13.knapsackproblem.Problem;
import com.github.beatngu13.knapsackproblem.base.Knapsack;

import io.jenetics.AnyChromosome;
import io.jenetics.AnyGene;
import io.jenetics.Genotype;
import io.jenetics.engine.Codec;
import io.jenetics.util.Factory;
import io.jenetics.util.RandomRegistry;

public class KnapsackCodec implements Codec<Knapsack, AnyGene<Knapsack>> {

	@Override
	public Factory<Genotype<AnyGene<Knapsack>>> encoding() {
		return Genotype.of(AnyChromosome.of(KnapsackCodec::create));
	}

	private static Knapsack create() {
		final var random = RandomRegistry.getRandom();
		final var items = Problem.ITEMS.stream() //
				.filter(item -> random.nextBoolean()) //
				.collect(Collectors.toList());
		final var knapsack = new Knapsack(items);
		// Make sure only valid solutions are created.
		return knapsack.getWeight() <= Problem.MAX_CAPACITY ? knapsack : create();
	}

	@Override
	public Function<Genotype<AnyGene<Knapsack>>, Knapsack> decoder() {
		return genotype -> genotype.getGene().getAllele();
	}

}