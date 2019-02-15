import evaluation.SimpleEvaluator;
import model.BitEntity;
import service.GeneticAlgorithmService;

import java.util.function.Function;

public class App {

    public static Function<Double, Double> function() {
        return (x) -> x * Math.sin(x * 10 * Math.PI) + 1;
    }

    public static void main(String[] args) {
        BitEntity bitEntity = new BitEntity(22);

        GeneticAlgorithmService geneticAlgorithm = GeneticAlgorithmService.newAlgorithmBuilder()
                .withPopulationSize(100)
                .withMutationPossility(0.01)
                .withSelectionPossibility(0.8)
                .withRange(-1, 2)
                .withSplittingSize((long) (3 * Math.pow(10, 6)))
                .withEvaluator(new SimpleEvaluator(function()))
                .build();

        geneticAlgorithm.findBestPopulation();

    }
}
