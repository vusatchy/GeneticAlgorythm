package service;

import evaluation.Evaluator;
import manager.PopulationManager;
import manager.SimplePopulationManager;
import model.BitEntity;
import org.apache.commons.lang3.Range;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;
import java.util.Objects;

public class GeneticAlgorithmService implements GeneticAlgorythm {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneticAlgorithmService.class);

    private int populationSize = 100;

    private double mutationPossibility = 0.01;

    private double selectionPossibility = 0.8;

    private long splittingSize = (long) (3 * Math.pow(10, 6));

    private int timesWithoutChanges = 20;

    private Range<Double> range = Range.between(0d, 1d);

    private Range<Double> recalculatedRange;

    private Evaluator evaluator;

    private PopulationManager populationManager;

    private int n;

    private double k;

    private double b;


    public GeneticAlgorithmService() {

    }

    private void recalculateRange() {
        double rightBorder = FastMath.log(2, splittingSize);
        rightBorder = FastMath.floor(rightBorder) + 1;
        this.n = (int) rightBorder;
        this.recalculatedRange = Range.between(0d, FastMath.pow(2, rightBorder) - 1);
    }

    private void calculateTransformationCoefficients() {
        RealMatrix coefficients = new Array2DRowRealMatrix(new double[][]{
                {recalculatedRange.getMinimum(), 1},
                {recalculatedRange.getMaximum(), 1}},
                false);

        DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();

        RealVector constants = new ArrayRealVector(new double[]{
                range.getMinimum(),
                range.getMaximum()},
                false);

        RealVector solution = solver.solve(constants);

        this.k = solution.getEntry(0);
        this.b = solution.getEntry(1);
    }

    @Override
    public double findBestSolution() {
        double bestSolution;
        int count = 0;
        List<BitEntity> population = populationManager.initialPopulation();
        bestSolution = bestSolution(population);
        while (true) {
            population = populationManager.mutateAndSelect(mutationPossibility, selectionPossibility);
            double tempResult = bestSolution(population);

            LOGGER.info("Population № {} best population result: {}", populationManager.lastPopulationIndex(), tempResult);

            if (tempResult > bestSolution) {
                count = 0;
                bestSolution = tempResult;
            } else if (count == timesWithoutChanges) {
                break;
            } else {
                count++;
            }
        }
        return bestSolution;
    }

    private double bestSolution(List<BitEntity> population) {
        return population.stream()
                .mapToDouble(entity -> evaluator.evaluate(entity, k, b))
                .max()
                .getAsDouble();
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

        public AlgorithmBuilder withMutationPossility(double mutationPossiblity) {
            GeneticAlgorithmService.this.mutationPossibility = mutationPossiblity;
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

        public AlgorithmBuilder withRange(double a, double b) {
            GeneticAlgorithmService.this.range = Range.between(a, b);
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

        public GeneticAlgorithmService build() {
            GeneticAlgorithmService geneticAlgorithmService = GeneticAlgorithmService.this;
            if (geneticAlgorithmService.evaluator == null) {
                throw new RuntimeException("Evaluator not selected");
            }
            geneticAlgorithmService.recalculateRange();
            geneticAlgorithmService.calculateTransformationCoefficients();
            geneticAlgorithmService.populationManager = new SimplePopulationManager(geneticAlgorithmService.populationSize, geneticAlgorithmService.n);
            return geneticAlgorithmService;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneticAlgorithmService that = (GeneticAlgorithmService) o;
        return populationSize == that.populationSize &&
                Double.compare(that.mutationPossibility, mutationPossibility) == 0 &&
                Double.compare(that.selectionPossibility, selectionPossibility) == 0 &&
                splittingSize == that.splittingSize &&
                Objects.equals(range, that.range) &&
                Objects.equals(recalculatedRange, that.recalculatedRange);
    }

    @Override
    public int hashCode() {

        return Objects.hash(populationSize, mutationPossibility, selectionPossibility, splittingSize, range, recalculatedRange);
    }
}
