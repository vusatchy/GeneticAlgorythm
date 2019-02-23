package evaluation;

import model.BitEntity;
import org.apache.commons.lang3.Range;

import java.util.List;
import java.util.function.Function;

public interface Evaluator {

    void setFunction(Function<double[], Double> function);

    List<Double> convert(BitEntity bitEntity, List<Range<Double>> ranges);

    double evaluate(BitEntity bitEntity, List<Range<Double>> range);
}
