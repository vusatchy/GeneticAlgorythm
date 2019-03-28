import choose.SimpleMaxOfRandomTwoChooser;
import draw.Draw;
import evaluation.Evaluator;
import evaluation.KPowerEvaluatorDecorator;
import evaluation.SimpleEvaluator;
import evaluation.SystemSolverEvaluatorDecorator;
import manager.ExpectedElitarModelPopulationManager;
import manager.SimplePopulationManager;
import model.GeneticResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.GeneticAlgorithmService;
import service.GeneticAlgorythm;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class App {


    private static final String FORMAT = "%.3f";

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        Evaluator evaluator = new SimpleEvaluator(Exersises.linear());
        Evaluator decorated = new SystemSolverEvaluatorDecorator(new SimpleEvaluator(), Exersises.snar);
        GeneticAlgorythm geneticAlgorithm = GeneticAlgorithmService.newAlgorithmBuilder()
                .withPopulationSize(300) //8000
                .wihtMutationPossibility(0.05)
                .withSelectionPossibility(0.9)
                .withRanges(Exersises.snarRanges)
                .withSplittingSize((long) Math.pow(10, 9))
                .withTimesWithoutChanges(500) //50
                .maxIterations(2000) //50
                .withEvaluator(decorated)
                .withChooser(new SimpleMaxOfRandomTwoChooser(decorated))
                .withPopulationManager(new SimplePopulationManager())
                .build();

        GeneticResult results = geneticAlgorithm.findBestSolution();
        LOGGER.info("Best result: {} on population № {} with value [{}]",
                String.format(FORMAT, results.getBestSolution()),
                results.getBestPopulation(),
                format(geneticAlgorithm.convert(results.getBitEntity())));

        GeneticAlgorythm geneticAlgorithmModern = GeneticAlgorithmService.newAlgorithmBuilder()
                .withPopulationSize(300) //8000
                .wihtMutationPossibility(0.05)
                .withSelectionPossibility(0.9)
                .withRanges(Exersises.snarRanges)
                .withSplittingSize((long) Math.pow(10, 9))
                .withTimesWithoutChanges(500) //50
                .maxIterations(2000) //50
                .withEvaluator(new KPowerEvaluatorDecorator(decorated, 1))
                .withChooser(new SimpleMaxOfRandomTwoChooser(decorated))
                .withPopulationManager(new ExpectedElitarModelPopulationManager(0.10))
                .build();


        GeneticResult resultsModern = geneticAlgorithmModern.findBestSolution();
        LOGGER.info("Best result: {} on population № {} with value [{}]",
                String.format(FORMAT, resultsModern.getBestSolution()),
                resultsModern.getBestPopulation(),
                format(geneticAlgorithmModern.convert(resultsModern.getBitEntity())));

        Draw.drawLine(results.getGenerationResult(), Color.red);

        Draw.drawLine(resultsModern.getGenerationResult(), Color.blue);

    }


    private static String format(List<Double> values) {
        List<String> result = new ArrayList<>();
        for (Double value : values) {
            result.add(String.format(FORMAT, value));
        }
        return StringUtils.join(result, " , ");
    }
}
