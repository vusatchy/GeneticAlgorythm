package model;

import org.apache.commons.lang3.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GeneticResult {

    private int bestPopulation;

    private double bestSolution;

    private Entity entity;

    private Map<Integer, Double> generationResult = new HashMap<>();

    public void add(int populationIndex, double tempResult) {
        generationResult.put(populationIndex, Math.abs(tempResult));
    }

    public int getBestPopulation() {
        return bestPopulation;
    }

    public void setBestPopulation(int bestPopulation) {
        this.bestPopulation = bestPopulation;
    }

    public double getBestSolution() {
        return bestSolution;
    }

    public void setBestSolution(double bestSolution) {
        this.bestSolution = bestSolution;
    }

    public Entity getBitEntity() {
        return entity;
    }

    public void setBitEntity(Entity entity) {
        this.entity = entity;
    }

    public Map<Integer, Double> getGenerationResult() {
        return generationResult;
    }

    public void setGenerationResult(Map<Integer, Double> generationResult) {
        this.generationResult = generationResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneticResult that = (GeneticResult) o;
        return bestPopulation == that.bestPopulation &&
                bestSolution == that.bestSolution &&
                Objects.equals(entity, that.entity) &&
                Objects.equals(generationResult, that.generationResult);
    }

    @Override
    public int hashCode() {

        return Objects.hash(bestPopulation, bestSolution, entity, generationResult);
    }
}
