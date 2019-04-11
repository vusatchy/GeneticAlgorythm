package choose;

import evaluation.Evaluator;
import model.BitEntity;
import model.Entity;
import org.apache.commons.lang3.Range;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class SimpleMaxOfRandomTwoChooser implements Chooser {


    private List<Range<Double>> ranges;

    private Evaluator evaluator;

    public SimpleMaxOfRandomTwoChooser(Evaluator evaluator) {
        this.evaluator = evaluator;

    }

    @Override
    public void setRanges(List<Range<Double>> ranges) {
        this.ranges = ranges;
    }

    @Override
    public Entity choose(List<? extends Entity> population) {
        Random random = new Random();
        Entity entity1 = population.get(random.nextInt(population.size()));
        Entity entity2 = population.get(random.nextInt(population.size()));
        return evaluator.evaluate(entity1, ranges) >= evaluator.evaluate(entity2, ranges)
                ? entity1 : entity2;
    }

    @Override
    public double evaluate(Entity bitEntity) {
        return evaluator.evaluate(bitEntity, ranges);
    }
}
