package evaluation;

import com.google.common.primitives.Doubles;
import model.BitEntity;
import model.Entity;
import org.apache.commons.lang3.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SimpleEvaluator implements Evaluator {

    private Function<double[], Double> function;

    public SimpleEvaluator(Function<double[], Double> function) {
        this.function = function;
    }

    public SimpleEvaluator() {

    }

    @Override
    public void setFunction(Function<double[], Double> function) {
        this.function = function;
    }

    @Override
    public List<Double> convert(Entity bitEntity, List<Range<Double>> ranges) {
        List<Double> results = new ArrayList<>();
        if (bitEntity instanceof BitEntity) {
            for (int i = 0; i < ranges.size(); i++) {
                Range<Double> range = ranges.get(i);
                double numeric = bitEntity.getNumericValue().get(i);
                int m = ((BitEntity) bitEntity).getBitsHolder().get(i).size();
                double
                    value =
                    range.getMinimum() + numeric * (range.getMaximum() - range.getMinimum()) / (Math.pow(2, m) - 1);
                results.add(value);
            }
        } else {
            results.addAll(bitEntity.getNumericValue());
        }
        return results;
    }

    //check formula
    @Override
    public double evaluate(Entity bitEntity, List<Range<Double>> ranges) {
        return (-1) * function.apply(Doubles.toArray(convert(bitEntity, ranges)));
    }
}
