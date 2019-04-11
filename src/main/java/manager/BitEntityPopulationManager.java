package manager;

import choose.Chooser;
import com.google.common.primitives.Ints;
import model.BitEntity;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.util.Pair;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.CompletableFuture.runAsync;

public class BitEntityPopulationManager implements PopulationManager {

    protected Map<Integer, List<BitEntity>> populations = new HashMap<>();
    protected Chooser chooser;
    protected int populationSize;
    protected List<Integer> dimensions;
    protected ExecutorService executorService = Executors.newCachedThreadPool();

    public BitEntityPopulationManager(Chooser chooser, int populationSize, List<Integer> dimensions) {
        this.populationSize = populationSize;
        this.dimensions = dimensions;
        this.chooser = chooser;
        init();
    }

    public BitEntityPopulationManager() {
        init();
    }

    @Override
    public void init() {
        List<BitEntity> entities = new ArrayList<>();
        for (int i = 0; i < this.populationSize; i++) {
            entities.add(new BitEntity(dimensions));
        }
        this.populations.put(0, entities);
    }

    @Override
    public List<BitEntity> mutateAndSelect(double mutationPossibility, double selectionPossibility) {
        int lastPopulationIndex = populations.keySet().stream().mapToInt(x -> x).max().getAsInt();
        List<BitEntity> lastPopulation = copyDeep(populations.get(lastPopulationIndex));
        List<BitEntity> newPopulation = new CopyOnWriteArrayList<>();

        CompletableFuture[] tasks = new CompletableFuture[populationSize / 2];
        for (int i = 0; i < populationSize / 2; i++) {
            tasks[i] = runAsync(() -> {
                BitEntity parent1 = BitEntity.of(chooser.choose(lastPopulation));
                BitEntity parent2 = BitEntity.of(chooser.choose(lastPopulation));
                BitEntity child1;
                BitEntity child2;

                //merge
                if (selectionPossibility < Math.random()) {
                    Pair<BitEntity, BitEntity> merged = mergeEntities(parent1, parent2);
                    child1 = merged.getFirst();
                    child2 = merged.getSecond();

                } else {
                    child1 = BitEntity.of(parent1.getBitsHolder());
                    child2 = BitEntity.of(parent2.getBitsHolder());
                }

                //mutate
                if (mutationPossibility < Math.random()) {
                    child2.mutate();
                }
                if (mutationPossibility < Math.random()) {
                    child1.mutate();
                }

                newPopulation.add(child1);
                newPopulation.add(child2);
            }, executorService);
        }
        CompletableFuture.allOf(tasks).join();
        populations.put(lastPopulationIndex + 1, newPopulation);
        return newPopulation;
    }

    protected List<BitEntity> copyDeep(List<BitEntity> bitEntities) {
        List<BitEntity> result = new ArrayList<>();
        for (BitEntity bitEntity : bitEntities) {
            result.add(BitEntity.of(bitEntity.getBitsHolder()));
        }
        return result;
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
        this.dimensions = dimensions;
    }

    protected Pair<BitEntity, BitEntity> mergeEntities(BitEntity parent1, BitEntity parent2) {
        List<List<Integer>> bitsHolder1 = parent1.getBitsHolder();
        List<List<Integer>> bitsHolder2 = parent2.getBitsHolder();

        List<List<Integer>> bitsHolderResult1 = new CopyOnWriteArrayList<>();
        List<List<Integer>> bitsHolderResult2 = new CopyOnWriteArrayList<>();

        CompletableFuture[] tasks = new CompletableFuture[bitsHolder1.size()];
        for (int i = 0; i < bitsHolder1.size(); i++) {
            int finalI = i;
            tasks[i] = runAsync(() -> {
                        List<Integer> bits1 = bitsHolder1.get(finalI);
                        List<Integer> bits2 = bitsHolder2.get(finalI);

                        Pair<List<Integer>, List<Integer>> pair = mergeEntities(bits1, bits2);

                        bitsHolderResult1.add(pair.getFirst());
                        bitsHolderResult2.add(pair.getSecond());
                    } , executorService
            );
        }
        CompletableFuture.allOf(tasks).join();
        return Pair.create(BitEntity.of(BitEntity.of(bitsHolderResult1)), BitEntity.of(bitsHolderResult2));
    }


    Pair<List<Integer>, List<Integer>> mergeEntities(List<Integer> parent1, List<Integer> parent2) {
        int[] child1 = new int[0];
        int[] child2 = new int[0];
        int borderIndex = new Random().nextInt(parent1.size());
        child1 = ArrayUtils.addAll(child1, ArrayUtils.subarray(Ints.toArray(parent1), 0, borderIndex));
        child1 = ArrayUtils.addAll(child1, ArrayUtils.subarray(Ints.toArray(parent2), borderIndex, Ints.toArray(parent1).length));

        child2 = ArrayUtils.addAll(child2, ArrayUtils.subarray(Ints.toArray(parent1), borderIndex, Ints.toArray(parent1).length));
        child2 = ArrayUtils.addAll(child2, ArrayUtils.subarray(Ints.toArray(parent2), 0, borderIndex));

        return Pair.create(Ints.asList(child1), Ints.asList(child2));
    }

}
