package choose;

import model.BitEntity;

import java.util.List;
import java.util.Random;

public class SimpleMaxOfRandomTwoChooser implements Chooser {


    @Override
    public BitEntity choose(List<BitEntity> population) {
        Random random = new Random();
        BitEntity entity1 = population.get(random.nextInt(population.size()));
        BitEntity entity2 = population.get(random.nextInt(population.size()));
        return entity1.getNumericValue() >= entity2.getNumericValue() ? entity1 : entity2;
    }
}
