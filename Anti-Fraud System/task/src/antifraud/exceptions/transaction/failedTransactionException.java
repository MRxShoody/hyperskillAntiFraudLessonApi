package antifraud.exceptions.transaction;

import antifraud.models.enums.transactionResultTypes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class failedTransactionException extends RuntimeException {

    public String info;
    public failedTransactionException(transactionResultTypes rs, String info) {
        super(rs.name());
        this.info = info;
    }

}
