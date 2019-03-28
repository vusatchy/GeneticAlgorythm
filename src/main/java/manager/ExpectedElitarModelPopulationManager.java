package manager;

import com.google.common.collect.ImmutableList;
import model.BitEntity;
import org.apache.commons.math3.util.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.runAsync;

public class ExpectedElitarModelPopulationManager extends SimplePopulationManager {

    private double percentOfElitar;

    public ExpectedElitarModelPopulationManager(double percentOfElitar) {
        this.percentOfElitar = percentOfElitar;
    }

    @Override
    public List<BitEntity> mutateAndSelect(double mutationPossibility, double selectionPossibility) {

        Comparator<BitEntity> comparator  = Comparator.comparingDouble(entity -> chooser.evaluate(entity));
        int lastPopulationIndex = populations.keySet().stream().mapToInt(x -> x).max().getAsInt();
        int amount = (int) (percentOfElitar * populationSize);
        int times = populationSize - amount;

        List<BitEntity> lastPopulation = copyDeep(populations.get(lastPopulationIndex));
        List<BitEntity> newPopulation = new CopyOnWriteArrayList<>();
        List<BitEntity> elit = lastPopulation.stream()
                .sorted(comparator.reversed())
                .limit(amount)
                .collect(Collectors.toList());

        newPopulation.addAll(elit);

        CompletableFuture[] tasks = new CompletableFuture[times];
        for (int i = 0; i < times; i++) {
            tasks[i] = runAsync(() -> {
                BitEntity parent1 = BitEntity.of(chooser.choose(lastPopulation).getBitsHolder());
                BitEntity parent2 = BitEntity.of(chooser.choose(lastPopulation).getBitsHolder());
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

                if (newPopulation.size() != populationSize) {
                    newPopulation.add(child1);
                }

                if (newPopulation.size() != populationSize) {
                    newPopulation.add(child2);
                }

            }, executorService);
        }
        CompletableFuture.allOf(tasks).join();
        populations.put(lastPopulationIndex + 1, newPopulation);
        return newPopulation;
    }

}
