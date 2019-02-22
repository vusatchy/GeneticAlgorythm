package choose;

import model.BitEntity;
import org.apache.commons.lang3.Range;

import java.util.List;

public interface Chooser {

    BitEntity choose(List<BitEntity> population);

    void setRanges(List<Range<Double>> ranges);
}
