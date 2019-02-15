package evaluation;

import model.BitEntity;

public interface Evaluator {

    double evaluate(BitEntity bitEntity, double k, double b);
}
