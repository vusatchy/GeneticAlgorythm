package evaluation;

import model.BitEntity;
import org.apache.commons.lang3.Range;

import java.util.List;
import java.util.function.Function;

public class SystemSolverEvaluatorDecorator implements Evaluator {

    private Evaluator evaluator;

    public SystemSolverEvaluatorDecorator(Evaluator evaluator, List<Function<double[], Double>> functions) {
        this.evaluator = evaluator;
        evaluator.setFunction(convert(functions));
    }

    @Override
        public void setFunction(Function<double[], Double> function) {
            evaluator.setFunction(function);
    }

    @Override
    public List<Double> convert(BitEntity bitEntity, List<Range<Double>> ranges) {
        return evaluator.convert(bitEntity, ranges);
    }

    @Override
    public double evaluate(BitEntity bitEntity, List<Range<Double>> range) {
        return evaluator.evaluate(bitEntity, range);
    }

    private Function<double[], Double> convert(List<Function<double[], Double>> functions) {
        return (values) -> {
            double sum = 0;
            for (Function<double[], Double> function : functions) {
                sum += Math.abs(function.apply(values));
            }
            return sum;
        };
    }
}
