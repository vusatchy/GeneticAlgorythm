package choose;

import model.BitEntity;

import java.util.List;

public interface Chooser {

    BitEntity choose(List<BitEntity> population);
}
