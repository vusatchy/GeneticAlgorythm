import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.Range;

import java.util.List;
import java.util.function.Function;

public class Exersises {

    public static Function<double[], Double> paraboloid() {
        return (x) -> 2 * x[0] * x[0] + 2 * x[1] * x[1];
    }

    public static Function<double[], Double> linear() {
        return (x) -> 2 * x[0]  - 2 * x[1] ;
    }

    public static List<Range<Double>> ranges = ImmutableList.of(
            Range.between(-5d, 5d),
            Range.between(-5d, 5d)
    );


    public static List<Function<double[], Double>> slar = ImmutableList.of(
            (x) -> x[0] + x[1] + x[2],
            (x) -> 8 * x[0] + 4 * x[1] + 6 * x[2] - 8,
            (x) -> 15 * x[0] + 3 * x[1] + 5 * x[2]
    );


    public static List<Range<Double>> slarRanges = ImmutableList.of(
            Range.between(-2d, 2d),
            Range.between(-7d, -2d),
            Range.between(5d, 10d)
    );


    public static List<Function<double[], Double>> snar = ImmutableList.of(
            (x) -> 3 * x[0] * x[0] + 17 * x[0] * x[1] + 10 * x[1] * x[1],
            (x) -> x[0] * x[0] + 4 * x[0] * x[1] + 20
    );

    public static List<Range<Double>> snarRanges = ImmutableList.of(
            Range.between(-2d, 5d),
            Range.between(-5d, 1d)
    );

}
