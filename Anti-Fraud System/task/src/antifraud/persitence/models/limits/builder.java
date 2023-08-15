package antifraud.persitence.models.limits;

import java.util.function.BinaryOperator;

public interface builder {

    amountParameters.builder setManualProcessingAmount(long manualProcessingAmount);

    amountParameters.builder setProhibitedAmount(long prohibitedAmount);

    amountParameters.builder setIncreaseFunc(BinaryOperator<Long> increase);

    amountParameters.builder setDecreaseFunc(BinaryOperator<Long> decrease);

    amountParameters build();
}
