package evaluation;

import model.BitEntity;
import org.apache.commons.lang3.Range;

import java.util.List;

public interface Evaluator {

    List<Double> convert(BitEntity bitEntity, List<Range<Double>> ranges);

    double evaluate(BitEntity bitEntity, List<Range<Double>> range);
}
