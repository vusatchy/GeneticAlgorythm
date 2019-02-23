import choose.SimpleMaxOfRandomTwoChooser;
import com.google.common.collect.ImmutableList;
import evaluation.Evaluator;
import evaluation.SimpleEvaluator;
import evaluation.SystemSolverEvaluatorDecorator;
import model.BitEntity;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.GeneticAlgorithmService;
import service.GeneticAlgorythm;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class App {


    private static final String FORMAT = "%.3f";

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private static Function<double[], Double> function() {
        return (x) -> 2 * x[0] + 2 * x[1] + 2 * x[2];
    }

    private static List<Range<Double>> ranges = ImmutableList.of(
            Range.between(-5d, 5d),
            Range.between(-5d, 5d),
            Range.between(-5d, 5d)
    );


    private static List<Function<double[], Double>> slar = ImmutableList.of(
            (x) -> x[0] + x[1] + x[2],
            (x) -> 8 * x[0] + 4 * x[1] + 6 * x[2] - 8,
            (x) -> 15 * x[0] + 3 * x[1] + 5 * x[2]
    );


    private static List<Range<Double>> slarRanges = ImmutableList.of(
            Range.between(-2d, 2d),
            Range.between(-7d, -2d),
            Range.between(5d, 10d)
    );


    public static void main(String[] args) {
        Evaluator evaluator = new SimpleEvaluator(function());
        Evaluator decorated = new SystemSolverEvaluatorDecorator(evaluator, slar);

        GeneticAlgorythm geneticAlgorithm = GeneticAlgorithmService.newAlgorithmBuilder()
                .withPopulationSize(100)
                .wihtMutationPossibility(0.01)
                .withSelectionPossibility(0.9)
                .withRanges(slarRanges)
                .withSplittingSize((long) Math.pow(10, 6))
                .withTimesWithoutChanges(1_000)
                .withEvaluator(decorated)
                .withChooser(new SimpleMaxOfRandomTwoChooser(function(), decorated))
                .build();

        Triple<Integer, Double, BitEntity> results = geneticAlgorithm.findBestSolution();
        LOGGER.info("Best result: {} on population № {} with value [{}]", String.format(FORMAT, results.getMiddle()),
                results.getLeft(), format(geneticAlgorithm.convert(results.getRight())));
    }


    private static String format(List<Double> values) {
        List<String> result = new ArrayList<>();
        for (Double value : values) {
            result.add(String.format(FORMAT, value));
        }
        return StringUtils.join(result, " , ");
    }
}
