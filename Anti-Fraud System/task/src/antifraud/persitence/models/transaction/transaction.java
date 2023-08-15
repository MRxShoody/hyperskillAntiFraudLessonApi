package antifraud.persitence.models.transaction;

import antifraud.models.enums.regions;
import antifraud.models.requests.transactionRequest;
import antifraud.persitence.models.limits.amountParameters;
import antifraud.persitence.models.user.user;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
public class transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(value = "transactionId", index = 0)
    private long id;

    private long amount;

    private String ip;

    private String number;

    @Enumerated(EnumType.STRING)
    private regions region;

    @JsonProperty("date")
    private LocalDateTime transactionDate;

    private String result;

    private String feedback;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private amountParameters amountParameters;

    public transaction(transactionRequest request, String number,
                       String result, String feedback, amountParameters amountParameters){
        this.amount = request.amount();
        this.ip = request.ip();
        this.transactionDate = request.date();
        this.region = request.region();
        this.number = number;
        this.result = result;
        this.feedback = feedback;
        this.amountParameters = amountParameters;
    }

    public transaction() {}
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public long getAmount() {
        return amount;
    }
    public String getResult() {
        return result;
    }
    public String getFeedback() {
        return feedback;
    }

    public String getNumber() {
        return number;
    }

    public String getIp(){
        return ip;
    }

    public String getRegion(){
        return region.name();
    }

    public LocalDateTime getTransactionDate(){
        return transactionDate;
    }
    public amountParameters getAmountParameters() {
        return amountParameters;
    }

}
