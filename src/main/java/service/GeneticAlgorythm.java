package service;

import model.BitEntity;
import model.Entity;
import model.GeneticResult;

import java.util.List;

public interface GeneticAlgorythm {

    GeneticResult findBestSolution();

    List<Double> convert(Entity bitEntity);
}
