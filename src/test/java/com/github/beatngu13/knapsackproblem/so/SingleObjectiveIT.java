package com.github.beatngu13.knapsackproblem.so;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

import org.junit.jupiter.api.Test;

import com.github.beatngu13.knapsackproblem.so.ga.ItemGene;
import com.github.beatngu13.knapsackproblem.so.ga.KnapsackChromosome;
import com.github.beatngu13.knapsackproblem.so.ga.KnapsackCodec;
import com.github.beatngu13.knapsackproblem.so.ga.ProfitFitness;
import com.github.beatngu13.knapsackproblem.so.ga.UnusedItemsMutator;
import com.github.beatngu13.knapsackproblem.so.ga.WeightConstraint;

import io.jenetics.Genotype;
import io.jenetics.Mutator;
import io.jenetics.SinglePointCrossover;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.util.RandomRegistry;

public class SingleObjectiveIT {

	@Test
	void should_find_optimal_solution() throws Exception {
		final Engine<ItemGene, Integer> knapsackEngine = Engine.builder(new ProfitFitness(), new KnapsackCodec()) //
				.executor(Runnable::run) // Single-threaded for reproducibility.
				.alterers(new SinglePointCrossover<>(0.2), new Mutator<>(0.15), new UnusedItemsMutator(0.3)) //
				.constraint(new WeightConstraint()) //
				.build();

		final EvolutionStatistics<Integer, ?> stats = EvolutionStatistics.ofNumber();

		final Genotype<ItemGene> bestPhenotype = RandomRegistry.with(new Random(0L), // Fixed seed for reproducibility.
				rand -> knapsackEngine.stream() //
						.limit(200L) //
						.peek(stats) //
						.collect(EvolutionResult.toBestGenotype()));

		final var bestKnapsack = ((KnapsackChromosome) bestPhenotype.getChromosome()).getKnapsack();
		assertThat(bestKnapsack).isEqualTo(SingeObjectiveProblem.OPTIMAL_KNAPSACK);
		System.out.println(stats);
	}

}
