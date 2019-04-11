package manager;

import choose.Chooser;
import model.Entity;

import java.util.List;

public interface PopulationManager {

    List<? extends Entity> mutateAndSelect(double mutationPossibility, double selectionPossibility);

    int lastPopulationIndex();

    List<? extends Entity> initialPopulation();

    void setChooser(Chooser chooser);

    void setPopultaionSize(int populationSize);

    void setDimensions(List<Integer> dimensions);

    void init();
}
