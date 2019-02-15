package model;

import org.apache.commons.lang3.RandomUtils;

import java.util.Arrays;

public class BitEntity implements Cloneable {

    private int[] bits;

    public BitEntity(int size) {
        this.bits = new int[size];
        populate();
    }

    public BitEntity(int[] bits) {
        this.bits = bits;
    }

    private void populate() {
        for (int i = 0; i < bits.length; i++) {
            bits[i] = RandomUtils.nextBoolean() ? 1 : 0;
        }
    }

    public int[] getBits() {
        return bits;
    }

    public double getNumericValue() {
        double sum = 0;
        for (int i = 0; i < bits.length; i++) {
            sum += bits[i] * Math.pow(2, i);
        }
        return sum;
    }

    public void mutate() {
        int i = RandomUtils.nextInt(0, bits.length);
        bits[i] = bits[i] == 1 ? 0 : 1;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BitEntity bitEntity = (BitEntity) o;
        return Arrays.equals(bits, bitEntity.bits);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bits);
    }
}
