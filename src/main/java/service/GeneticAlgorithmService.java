package service;

import choose.Chooser;
import evaluation.Evaluator;
import manager.PopulationManager;
import model.Entity;
import model.GeneticResult;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.util.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeneticAlgorithmService implements GeneticAlgorithm {

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

    private int maxIterations;


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
    public GeneticResult findBestSolution() {
        GeneticResult result = new GeneticResult();
        int count = 0;
        List<? extends Entity> population = populationManager.initialPopulation();
        Pair<Double, Entity> doubleBitEntityPair = bestSolution(population);
        result.setBestPopulation(populationManager.lastPopulationIndex());
        result.setBestSolution(doubleBitEntityPair.getLeft());
        result.setBitEntity(doubleBitEntityPair.getRight());
        while (true) {
            population = populationManager.mutateAndSelect(mutationPossibility, selectionPossibility);
            doubleBitEntityPair = bestSolution(population);
            double tempResult = doubleBitEntityPair.getLeft();
            int populationIndex = populationManager.lastPopulationIndex();
            LOGGER.info("Population № {} best population result: {}", populationIndex, Math.abs(tempResult));
            result.add(populationIndex, tempResult);
            if (tempResult > result.getBestSolution()) {
                count = 0;
                result.setBestSolution(tempResult);
                result.setBestPopulation(populationIndex);
                result.setBitEntity(doubleBitEntityPair.getRight());
            } else if (count == timesWithoutChanges || populationIndex > maxIterations) {
                break;
            } else {
                count++;
            }
        }
        return result;
    }

    @Override
    public List<Double> convert(Entity bitEntity) {
        return evaluator.convert(bitEntity, ranges);
    }


    private Pair<Double, Entity> bestSolution(List<? extends Entity> population) {
        Entity entity = population.get(0);
        double max = evaluator.evaluate(entity, ranges);
        for (int i = 1; i < population.size(); i++) {
            double result = evaluator.evaluate(population.get(i), ranges);
            if (result > max) {
                max = result;
                entity = population.get(i);
            }
        }
        return Pair.of(max, entity);
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

        public AlgorithmBuilder maxIterations(int maxIterations) {
            GeneticAlgorithmService.this.maxIterations = maxIterations;
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
        public AlgorithmBuilder withPopulationManager(PopulationManager populationManager) {
            GeneticAlgorithmService.this.populationManager = populationManager;
            return this;
        }


        public GeneticAlgorithmService build() {
            GeneticAlgorithmService geneticAlgorithmService = GeneticAlgorithmService.this;
            if (geneticAlgorithmService.evaluator == null) {
                throw new RuntimeException("Evaluator not selected");
            }
            chooser.setRanges(ranges);
            geneticAlgorithmService.recalculateRange();
            geneticAlgorithmService.populationManager.setChooser(chooser);
            geneticAlgorithmService.populationManager.setPopultaionSize(geneticAlgorithmService.populationSize);
            geneticAlgorithmService.populationManager.setDimensions(geneticAlgorithmService.dimensions);
            geneticAlgorithmService.populationManager.init();
            return geneticAlgorithmService;
        }
    }


}
