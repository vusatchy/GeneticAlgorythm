package model;

import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BitEntity implements Cloneable, Entity {

    private List<List<Integer>> bitsHolder = new ArrayList<>();


    public BitEntity() {

    }

    public BitEntity(List<Integer> dimensions) {
        populate(dimensions);
    }

    public static BitEntity of(BitEntity bitEnt) {
        BitEntity bitEntity = new BitEntity();
        bitEntity.bitsHolder = bitEnt.getBitsHolder();
        return bitEntity;
    }

    public static BitEntity of(Entity bitEnt) {
        BitEntity bitEntity = new BitEntity();
        bitEntity.bitsHolder = ((BitEntity) bitEnt).getBitsHolder();
        return bitEntity;
    }

    public static BitEntity of(List<List<Integer>> bitsHolder) {
        BitEntity bitEntity = new BitEntity();
        bitEntity.bitsHolder = bitsHolder;
        return bitEntity;
    }

    private void populate(List<Integer> dimensions) {
        for (Integer dim : dimensions) {
            List<Integer> bits = new ArrayList<>();
            for (int i = 0; i < dim; i++) {
                bits.add(RandomUtils.nextBoolean() ? 1 : 0);
            }
            bitsHolder.add(bits);
        }
    }


    @Override
    public List<Double> getNumericValue() {
        List<Double> numericValues = new ArrayList<>();
        for (List<Integer> bits : bitsHolder) {
            double sum = 0;
            for (int i = 0; i < bits.size(); i++) {
                sum += bits.get(i) * Math.pow(2, i);
            }
            numericValues.add(sum);
        }
        return numericValues;
    }

    @Override
    public void mutate() {
        List<Integer> bits = bitsHolder.get(RandomUtils.nextInt(0, bitsHolder.size()));
        int i = RandomUtils.nextInt(0, bits.size());
        bits.set(i, bits.get(i) == 1 ? 0 : 1);
    }

    public List<List<Integer>> getBitsHolder() {
        List<List<Integer>> newBits = new ArrayList<>();
        for (List<Integer> bits : bitsHolder) {
            newBits.add(new ArrayList<>(bits));
        }
        return newBits;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BitEntity bitEntity = (BitEntity) o;
        return Objects.equals(bitsHolder, bitEntity.bitsHolder);
    }

    @Override
    public int hashCode() {

        return Objects.hash(bitsHolder);
    }
}
