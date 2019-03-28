package evaluation;

import model.BitEntity;
import org.apache.commons.lang3.Range;

import java.util.List;
import java.util.function.Function;

public class KPowerEvaluatorDecorator implements Evaluator {


    private Evaluator evaluator;

    private double k;

    public KPowerEvaluatorDecorator(Evaluator evaluator, double k) {
        this.evaluator = evaluator;
        this.k = k;
    }

    @Override
    public void setFunction(Function<double[], Double> function) {
        {
            evaluator.setFunction(function);
        }
    }

    @Override
    public List<Double> convert(BitEntity bitEntity, List<Range<Double>> ranges) {
        return evaluator.convert(bitEntity, ranges);
    }

    @Override
    public double evaluate(BitEntity bitEntity, List<Range<Double>> range) {
        return Math.pow(evaluator.evaluate(bitEntity, range), k);
    }
}
