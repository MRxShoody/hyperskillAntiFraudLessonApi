package antifraud.persitence.models.limits;

public class director {
    public static void defaultAmountParameters(builder builder){
        builder.setManualProcessingAmount(0);
        builder.setProhibitedAmount(0);
        builder.setIncreaseFunc((a, b) -> a);
        builder.setDecreaseFunc((a, b) -> a);
    }
}
