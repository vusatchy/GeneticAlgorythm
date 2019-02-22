package service;

import model.BitEntity;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;

public interface GeneticAlgorythm {

    Triple<Integer, Double, BitEntity> findBestSolution();

    List<Double> convert(BitEntity bitEntity);
}
