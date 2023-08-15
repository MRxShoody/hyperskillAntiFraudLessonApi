package antifraud.models.requests;

import antifraud.annonations.phoneValidator.validCardNumber;
import antifraud.annonations.ipValidator.validIP;
import antifraud.models.enums.regions;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

public record transactionRequest(long amount,
                                 @validIP String ip,
                                 @validCardNumber String number,
                                 regions region,
                                 @NotNull @PastOrPresent LocalDateTime date) {
}
