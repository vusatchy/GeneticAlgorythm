package manager;

import model.BitEntity;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.math3.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class SimplePopulationManager implements PopulationManager {

    private Map<Integer, List<BitEntity>> populations = new HashMap<>();
    private int populationSize;
    private int n;

    public SimplePopulationManager(int populationSize, int n) {
        this.populationSize = populationSize;
        this.n = n;
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
        BitEntity[] newPopulation = new BitEntity[populationSize];
        int step = this.populationSize / 2;
        for (int i = 0; i < step; i++) {
            BitEntity parent1 = lastPopulation.get(i);
            BitEntity parent2 = lastPopulation.get(i + step);
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

            newPopulation[i] = child1;
            newPopulation[i + step] = child2;
        }
        populations.put(lastPopulationIndex + 1, Arrays.asList(newPopulation));
        return Arrays.asList(newPopulation);
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

    private static Pair<BitEntity, BitEntity> mergeEntities(BitEntity parent1, BitEntity parent2, int borderIndex) {
        int[] child1 = new int[0];
        int[] child2 = new int[0];
        child1 = ArrayUtils.addAll(child1, ArrayUtils.subarray(parent1.getBits(), 0, borderIndex));
        child1 = ArrayUtils.addAll(child1, ArrayUtils.subarray(parent2.getBits(), borderIndex, parent1.getBits().length));


        child2 = ArrayUtils.addAll(child2, ArrayUtils.subarray(parent1.getBits(), borderIndex, parent1.getBits().length));
        child2 = ArrayUtils.addAll(child2, ArrayUtils.subarray(parent2.getBits(), 0, borderIndex));

        return Pair.create(new BitEntity(child1), new BitEntity(child2));
    }

    public static void main(String[] args) {
        BitEntity parent1 = new BitEntity(new int[]{1, 2, 3, 4, 5});
        BitEntity parent2 = new BitEntity(new int[]{6, 7, 8, 9, 0});
        int borderIndex = 2;
        Pair<BitEntity, BitEntity> crossed = mergeEntities(parent1, parent2, borderIndex);
        System.out.println(Arrays.stream(crossed.getFirst().getBits()).boxed().map(Object::toString).collect(Collectors.joining()));
        System.out.println(Arrays.stream(crossed.getSecond().getBits()).boxed().map(Object::toString).collect(Collectors.joining()));
    }
}
