package manager;

import model.BitEntity;

import java.util.List;

public interface PopulationManager {
    List<BitEntity> mutateAndSelect(double mutationPossibility, double selectionPossibility);

    int lastPopulationIndex();

    List<BitEntity> initialPopulation();
}
