package choose;

import model.BitEntity;
import model.Entity;
import org.apache.commons.lang3.Range;

import java.util.List;

public interface Chooser {

    Entity choose(List<? extends Entity> population);

    void setRanges(List<Range<Double>> ranges);

    double evaluate(Entity bitEntity);
}
