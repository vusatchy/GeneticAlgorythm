package manager;

import choose.Chooser;
import model.BitEntity;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.math3.util.Pair;

import java.util.*;

public class SimplePopulationManager implements PopulationManager {

    private Map<Integer, List<BitEntity>> populations = new HashMap<>();
    private Chooser chooser;
    private int populationSize;
    private int n;

    public SimplePopulationManager(Chooser chooser, int populationSize, int n) {
        this.populationSize = populationSize;
        this.n = n;
        this.chooser = chooser;
        init();
    }


    private void init() {
        List<BitEntity> entities = new ArrayList<>();
        for (int i = 0; i < this.populationSize; i++) {
            entities.add(new BitEntity(n));
        }
        this.populations.put(0, entities);
    }

    @Override
    public List<BitEntity> mutateAndSelect(double mutationPossibility, double selectionPossibility) {
        int lastPopulationIndex = populations.keySet().stream().mapToInt(x -> x).max().getAsInt();
        List<BitEntity> lastPopulation = populations.get(lastPopulationIndex);
        List<BitEntity> newPopulation = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            BitEntity parent1 = chooser.choose(lastPopulation);
            BitEntity parent2 = chooser.choose(lastPopulation);
            BitEntity child1;
            BitEntity child2;
            //merge
            if (selectionPossibility < Math.random()) {
                int borderIndex = RandomUtils.nextInt(1, this.n);
                Pair<BitEntity, BitEntity> merged = mergeEntities(parent1, parent2, borderIndex);
                child1 = merged.getFirst();
                child2 = merged.getSecond();

            } else {
                child1 = ObjectUtils.clone(parent1);
                child2 = ObjectUtils.clone(parent2);
            }

            //mutate
            if (mutationPossibility < Math.random()) {
                child1.mutate();
                child2.mutate();
            }

            newPopulation.add(child1);
            newPopulation.add(child2);
        }
        populations.put(lastPopulationIndex + 1, newPopulation);
        return newPopulation;
    }

    @Override
    public int lastPopulationIndex() {
        return populations.keySet().stream()
                .mapToInt(val -> val)
                .max().getAsInt();
    }

    @Override
    public List<BitEntity> initialPopulation() {
        return populations.get(0);
    }

    Pair<BitEntity, BitEntity> mergeEntities(BitEntity parent1, BitEntity parent2, int borderIndex) {
        int[] child1 = new int[0];
        int[] child2 = new int[0];
        child1 = ArrayUtils.addAll(child1, ArrayUtils.subarray(parent1.getBits(), 0, borderIndex));
        child1 = ArrayUtils.addAll(child1, ArrayUtils.subarray(parent2.getBits(), borderIndex, parent1.getBits().length));


        child2 = ArrayUtils.addAll(child2, ArrayUtils.subarray(parent1.getBits(), borderIndex, parent1.getBits().length));
        child2 = ArrayUtils.addAll(child2, ArrayUtils.subarray(parent2.getBits(), 0, borderIndex));

        return Pair.create(new BitEntity(child1), new BitEntity(child2));
    }

}
