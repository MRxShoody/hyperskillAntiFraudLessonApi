package antifraud.persitence.models.limits;

import antifraud.persitence.models.transaction.transaction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;
import java.util.function.BinaryOperator;

@Entity
public class amountParameters implements Cloneable{

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Transient
    private BinaryOperator<Long> increaseFunc;
    @Transient
    private BinaryOperator<Long> decreaseFunc;
    private long manualProcessingAmount;
    private long prohibitedAmount;

    @JsonIgnore
    @OneToMany(mappedBy = "amountParameters")
    private Set<transaction> transactions;


    public amountParameters(long manualProcessingAmount, long prohibitedAmount, BinaryOperator<Long> increase, BinaryOperator<Long> decrease) {
        this.manualProcessingAmount = manualProcessingAmount;
        this.prohibitedAmount = prohibitedAmount;
        this.increaseFunc = increase;
        this.decreaseFunc = decrease;

    }

    public amountParameters() {
    }

    public long getManualProcessingAmount() {
        return manualProcessingAmount;
    }

    public long getProhibitedAmount() {
        return prohibitedAmount;
    }

    public amountParameters increaseManual(long transactionAmount) {
        manualProcessingAmount =  increaseFunc.apply(manualProcessingAmount, transactionAmount);
        return this;
    }

    public amountParameters decreaseManual(long transactionAmount) {
        manualProcessingAmount =  decreaseFunc.apply(manualProcessingAmount, transactionAmount);
        return this;
    }

    public amountParameters increaseProhibited(long transactionAmount) {
        prohibitedAmount =  increaseFunc.apply(prohibitedAmount, transactionAmount);
        return this;
    }

    public amountParameters decreaseProhibited(long transactionAmount) {
        prohibitedAmount =  decreaseFunc.apply(prohibitedAmount, transactionAmount);
        return this;
    }



    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setDecreaseFunc(BinaryOperator<Long> decreaseFunc) {
        this.decreaseFunc = decreaseFunc;
    }

    public void setIncreaseFunc(BinaryOperator<Long> increaseFunc) {
        this.increaseFunc = increaseFunc;
    }

    public void setProhibitedAmount(long prohibitedAmount) {
        this.prohibitedAmount = prohibitedAmount;
    }

    public void setManualProcessingAmount(long manualProcessingAmount) {
        this.manualProcessingAmount = manualProcessingAmount;
    }

    public BinaryOperator<Long> getIncrease() {
        return increaseFunc;
    }

    public BinaryOperator<Long> getDecrease() {
        return decreaseFunc;
    }

    @Override
    public amountParameters clone() {
        try {
            amountParameters clone = (amountParameters) super.clone();
            clone.setIncreaseFunc(this.increaseFunc);
            clone.setDecreaseFunc(this.decreaseFunc);
            clone.setManualProcessingAmount(this.manualProcessingAmount);
            clone.setProhibitedAmount(this.prohibitedAmount);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static class builder implements antifraud.persitence.models.limits.builder {
        private long manualProcessingAmount;
        private long prohibitedAmount;
        private BinaryOperator<Long> increase;
        private BinaryOperator<Long> decrease;

        @Override
        public builder setManualProcessingAmount(long manualProcessingAmount) {
            this.manualProcessingAmount = manualProcessingAmount;
            return this;
        }
        @Override
        public builder setProhibitedAmount(long prohibitedAmount) {
            this.prohibitedAmount = prohibitedAmount;
            return this;
        }
        @Override
        public builder setIncreaseFunc(BinaryOperator<Long> increase) {
            this.increase = increase;
            return this;
        }
        @Override
        public builder setDecreaseFunc(BinaryOperator<Long> decrease) {
            this.decrease = decrease;
            return this;
        }

        public amountParameters build() {
            return new amountParameters(manualProcessingAmount, prohibitedAmount, increase, decrease);
        }

    }

}
