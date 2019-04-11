package evaluation;

import model.Entity;
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
    public List<Double> convert(Entity bitEntity, List<Range<Double>> ranges) {
        return evaluator.convert(bitEntity, ranges);
    }

    @Override
    public double evaluate(Entity bitEntity, List<Range<Double>> range) {
        return pow(evaluator.evaluate(bitEntity, range), k);
    }

    private double pow(double base, double exp) {
        int aftercoma = Integer.valueOf(String.valueOf(exp).split("[.]")[1]);
        if (base < 0 && exp != 1) {
            return  Math.pow(Math.abs(base), exp) * (aftercoma % 2 == 0 ? 1 : -1);
        } else {
            return Math.pow(base, exp);
        }
    }
}
