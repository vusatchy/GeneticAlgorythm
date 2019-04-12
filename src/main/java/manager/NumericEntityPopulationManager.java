package manager;

import static java.util.concurrent.CompletableFuture.runAsync;

import choose.Chooser;
import model.Entity;
import model.NumericEntity;
import org.apache.commons.lang3.Range;
import org.apache.commons.math3.util.Pair;
import util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NumericEntityPopulationManager implements PopulationManager {


    protected Map<Integer, List<NumericEntity>> populations = new HashMap<>();
    protected Chooser chooser;
    protected int populationSize;
    protected List<Range<Double>> ranges;
    protected ExecutorService executorService = Executors.newCachedThreadPool();

    public NumericEntityPopulationManager(Chooser chooser, int populationSize, List<Range<Double>> ranges) {
        this.populationSize = populationSize;
        this.ranges = ranges;
        this.chooser = chooser;
        init();
    }

    public NumericEntityPopulationManager(List<Range<Double>> ranges) {
        this.ranges = ranges;
        init();
    }

    @Override
    public List<NumericEntity> mutateAndSelect(double mutationPossibility, double selectionPossibility) {
        int lastPopulationIndex = populations.keySet().stream().mapToInt(x -> x).max().getAsInt();
        List<NumericEntity> lastPopulation = copyDeep(populations.get(lastPopulationIndex));
        List<NumericEntity> newPopulation = new CopyOnWriteArrayList<>();

        CompletableFuture[] tasks = new CompletableFuture[populationSize / 2];
        for (int i = 0; i < populationSize / 2; i++) {
            tasks[i] = runAsync(() -> {
                NumericEntity parent1 = NumericEntity.of(chooser.choose(lastPopulation));
                NumericEntity parent2 = NumericEntity.of(chooser.choose(lastPopulation));
                NumericEntity child1;
                NumericEntity child2;

                //merge
                if (selectionPossibility < Math.random()) {
                    Pair<NumericEntity, NumericEntity> merged = mergeEntities(parent1, parent2);
                    child1 = merged.getFirst();
                    child2 = merged.getSecond();

                } else {
                    child1 = NumericEntity.of(parent1);
                    child2 = NumericEntity.of(parent2);
                }

                //mutate
                if (mutationPossibility > Math.random() ) {
                    child2.mutate(ranges);
                }
                if (mutationPossibility > Math.random()) {
                    child1.mutate(ranges);
                }

                newPopulation.add(child1);
                newPopulation.add(child2);
            }, executorService);
        }
        CompletableFuture.allOf(tasks).join();
        populations.put(lastPopulationIndex + 1, newPopulation);
        return newPopulation;
    }

    private Pair<NumericEntity, NumericEntity> mergeEntities(Entity parent1, Entity parent2) {
        List<Double> values1 = new ArrayList<>();
        List<Double> values2 = new ArrayList<>();

        List<Double> parentValues1 = parent1.getNumericValue();
        List<Double> parentValues2 = parent2.getNumericValue();
        for (int i = 0; i < parentValues1.size(); i++) {
            values1.add(Util.random(parentValues1.get(i), parentValues2.get(i)));
            values2.add(Util.random(parentValues1.get(i), parentValues2.get(i)));
        }
        return Pair.create(NumericEntity.of(values1), NumericEntity.of(values2));
    }

    private List<NumericEntity> copyDeep(List<NumericEntity> bitEntities) {
        List<NumericEntity> result = new ArrayList<>();
        bitEntities.forEach(ent -> {
            result.add(NumericEntity.of(ent));
        });
        return result;
    }

    @Override
    public int lastPopulationIndex() {
        return populations.keySet().stream()
            .mapToInt(val -> val)
            .max().getAsInt();
    }

    @Override
    public List<? extends Entity> initialPopulation() {
        return populations.get(0);
    }

    @Override
    public void setChooser(Chooser chooser) {
        this.chooser = chooser;
    }

    @Override
    public void setPopultaionSize(int populationSize) {
        this.populationSize = populationSize;
    }

    @Override
    public void setDimensions(List<Integer> dimensions) {

    }

    @Override
    public void init() {
        List<NumericEntity> entities = new ArrayList<>();
        for (int i = 0; i < this.populationSize; i++) {
            entities.add(new NumericEntity(ranges));
        }
        this.populations.put(0, entities);
    }
}
