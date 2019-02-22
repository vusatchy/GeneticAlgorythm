package service;

import choose.Chooser;
import evaluation.Evaluator;
import manager.PopulationManager;
import manager.SimplePopulationManager;
import model.BitEntity;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.math3.util.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeneticAlgorithmService implements GeneticAlgorythm {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneticAlgorithmService.class);

    private int populationSize = 100;

    private double mutationPossibility = 0.01;

    private double selectionPossibility = 0.8;

    private long splittingSize = (long) Math.pow(10, 6);

    private int timesWithoutChanges = 20;

    private List<Range<Double>> ranges = Collections.singletonList(Range.between(0d, 1d));

    private Chooser chooser;

    private Evaluator evaluator;

    private PopulationManager populationManager;

    private List<Integer> dimensions = new ArrayList<>();


    public GeneticAlgorithmService() {

    }

    private void recalculateRange() {
        for (Range<Double> range : ranges) {
            double rightBorder = FastMath.log(2, (range.getMaximum() - range.getMinimum()) * splittingSize);
            rightBorder = FastMath.floor(rightBorder) + 1;
            dimensions.add((int) rightBorder);
        }
    }

    @Override
    public Triple<Integer, Double, BitEntity> findBestSolution() {
        int count = 0;
        List<BitEntity> population = populationManager.initialPopulation();
        Pair<Double, BitEntity> doubleBitEntityPair = bestSolution(population);
        int bestPopulation = populationManager.lastPopulationIndex();
        double bestSolution = doubleBitEntityPair.getLeft();
        BitEntity bitEntity = doubleBitEntityPair.getRight();
        while (true) {
            population = populationManager.mutateAndSelect(mutationPossibility, selectionPossibility);
            doubleBitEntityPair = bestSolution(population);
            double tempResult = doubleBitEntityPair.getLeft();

            LOGGER.info("Population № {} best population result: {}", populationManager.lastPopulationIndex(), tempResult);

            if (tempResult < bestSolution) {
                count = 0;
                bestSolution = tempResult;
                bestPopulation = populationManager.lastPopulationIndex();
                bitEntity = doubleBitEntityPair.getRight();
            } else if (count == timesWithoutChanges) {
                break;
            } else {
                count++;
            }
        }
        return Triple.of(bestPopulation, bestSolution, bitEntity);
    }

    @Override
    public List<Double> convert(BitEntity bitEntity) {
        return evaluator.convert(bitEntity, ranges);
    }


    private Pair<Double, BitEntity> bestSolution(List<BitEntity> population) {
        BitEntity entity = population.get(0);
        double min = evaluator.evaluate(entity, ranges);
        for (int i = 1; i < population.size(); i++) {
            double result = evaluator.evaluate(population.get(i), ranges);
            if (result < min) {
                min = result;
                entity = population.get(i);
            }
        }
        return Pair.of(min, entity);
    }

    public static AlgorithmBuilder newAlgorithmBuilder() {
        return new GeneticAlgorithmService().new AlgorithmBuilder();
    }

    public class AlgorithmBuilder {

        private AlgorithmBuilder() {

        }

        public AlgorithmBuilder withPopulationSize(int populationSize) {
            GeneticAlgorithmService.this.populationSize = populationSize;
            return this;
        }

        public AlgorithmBuilder wihtMutationPossibility(double mutationPossibility) {
            GeneticAlgorithmService.this.mutationPossibility = mutationPossibility;
            return this;
        }

        public AlgorithmBuilder withSelectionPossibility(double selectionPossibility) {
            GeneticAlgorithmService.this.selectionPossibility = selectionPossibility;
            return this;
        }

        public AlgorithmBuilder withTimesWithoutChanges(int timesWithoutChanges) {
            GeneticAlgorithmService.this.timesWithoutChanges = timesWithoutChanges;
            return this;
        }

        public AlgorithmBuilder withRanges(List<Range<Double>> ranges) {
            GeneticAlgorithmService.this.ranges = ranges;
            return this;
        }

        public AlgorithmBuilder withSplittingSize(long splittingSize) {
            GeneticAlgorithmService.this.splittingSize = splittingSize;
            return this;
        }

        public AlgorithmBuilder withEvaluator(Evaluator evaluator) {
            GeneticAlgorithmService.this.evaluator = evaluator;
            return this;
        }

        public AlgorithmBuilder withChooser(Chooser chooser) {
            GeneticAlgorithmService.this.chooser = chooser;
            return this;
        }

        public GeneticAlgorithmService build() {
            GeneticAlgorithmService geneticAlgorithmService = GeneticAlgorithmService.this;
            if (geneticAlgorithmService.evaluator == null) {
                throw new RuntimeException("Evaluator not selected");
            }
            chooser.setRanges(ranges);
            geneticAlgorithmService.recalculateRange();
            geneticAlgorithmService.populationManager = new SimplePopulationManager(chooser, geneticAlgorithmService.populationSize, geneticAlgorithmService.dimensions);
            return geneticAlgorithmService;
        }
    }


}
