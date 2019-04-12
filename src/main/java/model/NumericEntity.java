package model;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.Range;
import util.Util;

import java.util.ArrayList;
import java.util.List;

public class NumericEntity implements Cloneable, Entity {

    private List<Double> values = new ArrayList<>();

    public NumericEntity(List<Range<Double>> ranges) {
        ranges.forEach(range -> {
            values.add(Util.random(range));
        });
    }

    public NumericEntity() {

    }

    public static NumericEntity of(NumericEntity of) {
        NumericEntity numericEntity = new NumericEntity();
        numericEntity.values = of.values;
        return numericEntity;
    }

    public static NumericEntity of(Entity of) {
        NumericEntity numericEntity = new NumericEntity();
        numericEntity.values = ((NumericEntity) of).values;
        return numericEntity;
    }

    public static NumericEntity of(List<Double> values) {
        NumericEntity numericEntity = new NumericEntity();
        numericEntity.values = values;
        return numericEntity;
    }

    @Override
    public List<Double> getNumericValue() {
        return values;
    }

    public void mutate(List<Range<Double>> ranges) {
        int i = RandomUtils.nextInt(0, values.size());
        values.set(i, Util.random(ranges.get(i)));
    }

    @Override
    public void mutate() {

    }

}
