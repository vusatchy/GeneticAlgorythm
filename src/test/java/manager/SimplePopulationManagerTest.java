package manager;

import choose.SimpleMaxOfRandomTwoChooser;
import model.BitEntity;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class SimplePopulationManagerTest {

    private SimplePopulationManager populationManager = new SimplePopulationManager(new SimpleMaxOfRandomTwoChooser(), 100, 5);


    @Test
    public void testEntitiesMerging() {
        BitEntity parent1 = new BitEntity(new int[]{1, 2, 3, 4, 5});
        BitEntity parent2 = new BitEntity(new int[]{6, 7, 8, 9, 0});
        int borderIndex = 2;
        Pair<BitEntity, BitEntity> crossed = populationManager.mergeEntities(parent1, parent2, borderIndex);
        assertArrayEquals(crossed.getFirst().getBits(), new int[]{1, 2, 8, 9, 0});
        assertArrayEquals(crossed.getSecond().getBits(), new int[]{3, 4, 5, 6, 7});
    }
}