package manager;

import choose.Chooser;
import model.BitEntity;

import java.util.List;

public interface PopulationManager {
    List<BitEntity> mutateAndSelect(double mutationPossibility, double selectionPossibility);

    int lastPopulationIndex();

    List<BitEntity> initialPopulation();

    void setChooser(Chooser chooser);

    void setPopultaionSize(int populationSize);

    void setDimensions(List<Integer> dimensions);

    void init();
}
