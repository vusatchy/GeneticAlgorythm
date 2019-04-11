package model;

import java.util.List;

public interface Entity {

    List<Double> getNumericValue();

    void mutate();
}
