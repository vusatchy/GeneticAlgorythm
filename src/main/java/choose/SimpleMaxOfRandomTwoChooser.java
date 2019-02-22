package choose;

import evaluation.Evaluator;
import model.BitEntity;
import org.apache.commons.lang3.Range;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class SimpleMaxOfRandomTwoChooser implements Chooser {

    private Function<double[], Double> function;

    private List<Range<Double>> range;

    private Evaluator evaluator;

    public SimpleMaxOfRandomTwoChooser(Function<double[], Double> function, Evaluator evaluator) {
        this.function = function;
        this.evaluator = evaluator;

    }

    @Override
    public void setRanges(List<Range<Double>> range) {
        this.range = range;
    }

    @Override
    public BitEntity choose(List<BitEntity> population) {
        Random random = new Random();
        BitEntity entity1 = population.get(random.nextInt(population.size()));
        BitEntity entity2 = population.get(random.nextInt(population.size()));
        return evaluator.evaluate(entity1, range) <= evaluator.evaluate(entity2, range)
                ? entity1 : entity2;
    }
}
