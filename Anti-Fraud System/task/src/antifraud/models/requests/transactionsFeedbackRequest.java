package antifraud.models.requests;

import antifraud.annonations.enumsValidator.ValueOfEnum;
import antifraud.models.enums.transactionResultTypes;


public class transactionsFeedbackRequest {
    private  long transactionId;

    @ValueOfEnum(enumClass = transactionResultTypes.class, message = "Invalid transaction feedback")
    private String feedback;
    public transactionsFeedbackRequest() {
    }
    public transactionsFeedbackRequest(long transactionId, String feedback) {
        this.transactionId = transactionId;
        this.feedback = feedback;
    }



    public long getTransactionId() {
        return transactionId;
    }

    public String getFeedback() {
        return feedback;
    }

}
