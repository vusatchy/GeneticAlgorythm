package util;

import org.apache.commons.lang3.Range;

public class Util {

    private Util() {

    }

    public static Double random(Double val1, Double val2) {
        double max = Math.max(val1, val2);
        double min = Math.min(val1, val2);
        return max - Math.random() * (max - min);
    }

    public static Double random(Range<Double> range) {
        return random(range.getMaximum(), range.getMinimum());
    }
}
