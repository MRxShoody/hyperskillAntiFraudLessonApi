package antifraud.models.enums;

public enum transactionResultTypes {

    NEGATIVE_AMOUNT("NEGATIVE_AMOUNT"),
    ALLOWED("ALLOWED"),
    MANUAL_PROCESSING("MANUAL_PROCESSING"),
    PROHIBITED("PROHIBITED");

    final String message;

    transactionResultTypes(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
