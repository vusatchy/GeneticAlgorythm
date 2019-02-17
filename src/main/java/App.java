import evaluation.SimpleEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.GeneticAlgorithmService;
import service.GeneticAlgorythm;

import java.util.function.Function;

public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static Function<Double, Double> function() {
        return (x) -> x * Math.sin(x * 10 * Math.PI) + 1;
    }

    public static void main(String[] args) {
        GeneticAlgorythm geneticAlgorithm = GeneticAlgorithmService.newAlgorithmBuilder()
                .withPopulationSize(100)
                .withMutationPossility(0.01)
                .withSelectionPossibility(0.8)
                .withRange(-1, 2)
                .withSplittingSize((long) (3 * Math.pow(10, 6)))
                .withTimesWithoutChanges(100)
                .withEvaluator(new SimpleEvaluator(function()))
                .build();

        double results = geneticAlgorithm.findBestSolution();
        LOGGER.info("Best result: {}", results);
    }
}
