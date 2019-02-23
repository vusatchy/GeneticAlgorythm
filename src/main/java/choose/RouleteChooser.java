package choose;

import evaluation.Evaluator;
import model.BitEntity;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.Range;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RouleteChooser implements Chooser {

    private List<Range<Double>> ranges;

    private Evaluator evaluator;

    public RouleteChooser(Evaluator evaluator) {
        this.ranges = ranges;
        this.evaluator = evaluator;
    }

    @Override
    public BitEntity choose(List<BitEntity> population) {
        Map<Range<Double>, BitEntity> entityMap = new LinkedHashMap<>();

        List<Double> values = population.stream()
                .map(ent -> evaluator.evaluate(ent, ranges))
                .collect(Collectors.toList());

        double max = values.stream()
                .mapToDouble(ent -> ent)
                .max().getAsDouble();

        double sum = values.stream()
                .mapToDouble(ent -> ent + max)
                .sum();

        double prevProb = 0;
        for (int i = 0; i < values.size(); i++) {
            double f = -values.get(i) + max;
            double prob = f / sum;
            entityMap.put(Range.between(prevProb, prevProb + prob), population.get(i));
            prevProb += prob;
        }

        double pos = Math.random();

        return entityMap.entrySet().stream()
                .filter(e -> e.getKey().contains(pos))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(population.get(population.size() - 1));

    }

    @Override
    public void setRanges(List<Range<Double>> ranges) {
        this.ranges = ranges;
    }
}
