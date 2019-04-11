package manager;

import model.BitEntity;
import model.Entity;
import org.apache.commons.math3.util.Pair;
import util.Roulette;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.runAsync;

public class ExpectedElitarModelPopulationManager extends BitEntityPopulationManager {

    private double percentOfElitar;

    public ExpectedElitarModelPopulationManager(double percentOfElitar) {
        this.percentOfElitar = percentOfElitar;
    }

    @Override
    public List<BitEntity> mutateAndSelect(double mutationPossibility, double selectionPossibility) {
        Map<BitEntity, Integer> entityCount = new HashMap<>();
        Comparator<BitEntity> comparator = Comparator.comparingDouble(entity -> chooser.evaluate(entity));
        int lastPopulationIndex = populations.keySet().stream().mapToInt(x -> x).max().getAsInt();
        int amount = (int) (percentOfElitar * populationSize);
        List<BitEntity> lastPopulation = copyDeep(populations.get(lastPopulationIndex));
        int times = populationSize - amount;
        List<BitEntity> newPopulation = new CopyOnWriteArrayList<>();
        List<BitEntity> elit = lastPopulation.stream()
                .sorted(comparator.reversed())
                .limit(amount)
                .collect(Collectors.toList());

        newPopulation.addAll(elit);

        CompletableFuture[] tasks = new CompletableFuture[times];
        for (int i = 0; i < times; i++) {
            tasks[i] = runAsync(() -> {
                List<Double> evluated = lastPopulation.stream()
                        .map(entity -> Math.abs(chooser.evaluate(entity)))
                        .collect(Collectors.toList());
                double avg = evluated.stream().mapToDouble(m -> m).average().getAsDouble();
                Roulette roulette = new Roulette(evluated.stream()
                        .mapToDouble(v -> v / avg).toArray());
                int spin1 = roulette.spin();
                int spin2 = roulette.spin();
                BitEntity bitEntity1 = lastPopulation.get(spin1);
                BitEntity bitEntity2 = lastPopulation.get(spin2);
                while (evluated.get(spin1) <= 0 && evluated.get(spin2) <= 0) {
                    spin1 = roulette.spin();
                    spin2 = roulette.spin();
                    bitEntity1 = lastPopulation.get(spin1);
                    bitEntity2 = lastPopulation.get(spin2);
                }
                BitEntity parent1 = BitEntity.of(bitEntity1);
                BitEntity parent2 = BitEntity.of(bitEntity2);
                BitEntity child1;
                BitEntity child2;

                //merge
                if (selectionPossibility < Math.random()) {
                    Pair<BitEntity, BitEntity> merged = mergeEntities(parent1, parent2);
                    child1 = merged.getFirst();
                    child2 = merged.getSecond();
                    evluated.set(spin1, evluated.get(spin1) - 1);
                    evluated.set(spin2, evluated.get(spin2) - 1);

                } else {
                    child1 = BitEntity.of(parent1);
                    child2 = BitEntity.of(parent2);
                }

                //mutate
                if (mutationPossibility < Math.random()) {
                    child2.mutate();
                    evluated.set(spin2, evluated.get(spin2) - 0.5);
                }
                if (mutationPossibility < Math.random()) {
                    child1.mutate();
                    evluated.set(spin1, evluated.get(spin1) - 0.5);
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
