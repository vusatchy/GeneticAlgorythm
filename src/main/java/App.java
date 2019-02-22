import choose.SimpleMaxOfRandomTwoChooser;
import com.google.common.collect.ImmutableList;
import evaluation.SimpleEvaluator;
import model.BitEntity;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.math3.util.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.GeneticAlgorithmService;
import service.GeneticAlgorythm;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static Function<double[], Double> function() {
        return (x) -> 2 * x[0]  + 2 * x[1];
    }

    public static List<Range<Double>> ranges = ImmutableList.of(
            Range.between(-1d, 1d),
            Range.between(-1d, 1d)
    );


    public static void main(String[] args) {

        GeneticAlgorythm geneticAlgorithm = GeneticAlgorithmService.newAlgorithmBuilder()
                .withPopulationSize(100)
                .wihtMutationPossibility(0.01)
                .withSelectionPossibility(0.9)
                .withRanges(ranges)
                .withSplittingSize((long) Math.pow(10, 6))
                .withTimesWithoutChanges(1_000)
                .withEvaluator(new SimpleEvaluator(function()))
                .withChooser(new SimpleMaxOfRandomTwoChooser(function(), new SimpleEvaluator(function())))
                .build();

        Triple<Integer, Double, BitEntity> results = geneticAlgorithm.findBestSolution();
        LOGGER.info("Best result: {} on population № {} with value {}", String.format("%.6f", results.getMiddle()),
                results.getLeft(), ArrayUtils.toString(geneticAlgorithm.convert(results.getRight()).toArray()));
    }
}
