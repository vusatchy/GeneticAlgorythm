package evaluation;

import model.BitEntity;

import java.util.function.Function;

public class SimpleEvaluator implements Evaluator {

    private Function<Double, Double> function;

    public SimpleEvaluator(Function<Double, Double> function) {
        this.function = function;
    }

    @Override
    public double evaluate(BitEntity bitEntity, double k, double b) {
        double x = bitEntity.getNumericValue();
        x = k * x + b;
        return function.apply(x);
    }
}
