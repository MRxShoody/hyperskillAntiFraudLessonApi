package antifraud.business.transaction;


import antifraud.business.security.correlationService;
import antifraud.business.security.stolenCardService;
import antifraud.business.security.suspiciousIpService;
import antifraud.exceptions.persistence.alreadyExistException;
import antifraud.exceptions.persistence.notFoundException;
import antifraud.exceptions.transaction.failedTransactionException;
import antifraud.exceptions.transaction.unprocessableEntityException;
import antifraud.persitence.models.limits.amountParameters;
import antifraud.models.enums.transactionResultTypes;
import antifraud.models.requests.transactionRequest;
import antifraud.models.requests.transactionsFeedbackRequest;
import antifraud.models.responses.transactionResponse;
import antifraud.persitence.models.transaction.transaction;
import antifraud.persitence.models.user.user;
import antifraud.persitence.repos.transaction.amountParametersRepo;
import antifraud.persitence.repos.transaction.transactionRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class transactionService {
    private final amountParameters amountParametersBean;
    private final stolenCardService stolenCardService;
    private final antifraud.business.security.suspiciousIpService suspiciousIpService;
    private final correlationService correlationService;
    private final transactionRepo transactionRepo;

    private final amountParametersRepo amountParametersRepo;

    @Autowired
    public transactionService(amountParameters ap,
                              antifraud.business.security.stolenCardService scs,
                              suspiciousIpService sis,
                              correlationService cs,
                              transactionRepo repo,
                              amountParametersRepo apr) {

        this.amountParametersBean = ap;
        this.stolenCardService = scs;
        this.suspiciousIpService = sis;
        this.correlationService = cs;
        this.transactionRepo = repo;
        this.amountParametersRepo = apr;
    }

    @Transactional
    public transactionResponse processTransaction(transactionRequest request, user user) {

        Optional<amountParameters> amountParametersO = amountParametersRepo.findByTransactions_Number(request.number());

        amountParameters amountParameters;

        if (amountParametersO.isEmpty()) {
            System.out.println("amountParametersO is empty");
            amountParameters = amountParametersBean.clone();
            amountParametersRepo.save(amountParameters);
        } else {
            System.out.println("amountParametersO is not empty");
            amountParameters = amountParametersO.get();
        }

        transaction transaction = new transaction(request, request.number(), "", "", amountParameters);
        transactionRepo.save(transaction);


        transactionResultTypes result;

        StringBuilder info = new StringBuilder();
        info.append("amount");

        boolean manualProcessing = false;

        if (request.amount() <= 0) {

            result = transactionResultTypes.NEGATIVE_AMOUNT;

        } else if (request.amount() <= amountParameters.getManualProcessingAmount()) {

            result = transactionResultTypes.ALLOWED;
            manualProcessing = true;

        } else if (request.amount() <= amountParameters.getProhibitedAmount()) {

            result = transactionResultTypes.MANUAL_PROCESSING;
            manualProcessing = true;

        } else {

            result = transactionResultTypes.PROHIBITED;

        }

        Map<String,transactionResultTypes> map = correlationService.validate(request, user);

        if(map.get("ip") == transactionResultTypes.MANUAL_PROCESSING & result != transactionResultTypes.PROHIBITED){

            result = transactionResultTypes.MANUAL_PROCESSING;

            if(manualProcessing){

                info = new StringBuilder();
                info.append("ip-correlation");
                manualProcessing = false;

            }else {

                info.append(", ip-correlation");

            }

        }

        if(map.get("region") == transactionResultTypes.MANUAL_PROCESSING & result != transactionResultTypes.PROHIBITED){

            result = transactionResultTypes.MANUAL_PROCESSING;

            if(manualProcessing){

                info = new StringBuilder();
                info.append("region-correlation");
                manualProcessing = false;

            }else {

                info.append(", region-correlation");

            }
        }

        if(stolenCardService.isBlocked(request.number())){

            result = transactionResultTypes.PROHIBITED;

            if(manualProcessing){

                info = new StringBuilder();
                info.append("card-number");
                manualProcessing = false;

            }else{

                info.append(", card-number");

            }
        }

        if(suspiciousIpService.isBlocked(request.ip())){

            result = transactionResultTypes.PROHIBITED;

            if(manualProcessing){

                info = new StringBuilder();
                info.append("ip");
                manualProcessing = false;

            }else {

                info.append(", ip");

            }
        }

        if(map.get("ip") == transactionResultTypes.PROHIBITED){

            result = transactionResultTypes.PROHIBITED;

            if(manualProcessing){

                info = new StringBuilder();
                info.append("ip-correlation");
                manualProcessing = false;

            }else {

                info.append(", ip-correlation");

            }
        }

        if (map.get("region") == transactionResultTypes.PROHIBITED){

            result = transactionResultTypes.PROHIBITED;

            if(manualProcessing){

                info = new StringBuilder();
                info.append("region-correlation");

            }else {

                info.append(", region-correlation");

            }

        }

        transaction.setResult(result.getMessage().toUpperCase());

        switch (result) {
            case ALLOWED -> {
                return new transactionResponse(result.getMessage(), "none");
            }
            case NEGATIVE_AMOUNT -> {
                throw new failedTransactionException(result, info.toString());
            }
            default -> {
                return new transactionResponse(result.getMessage(), info.toString());
            }
        }
    }

    int i = 0;
    @Transactional
    public transaction processTransactionFeedback(transactionsFeedbackRequest request) {
        transaction t = transactionRepo.findById(request.getTransactionId()).orElseThrow(()->new notFoundException("transaction doesn't exist"));

        if(!t.getFeedback().equals(""))
            throw new alreadyExistException("feedback already exist");


        if(request.getFeedback().equals(t.getResult())){
            throw new unprocessableEntityException("can't be same");
        }

        amountParameters amountParameters = amountParametersRepo.findByTransactions_Number(t.getNumber()).orElseThrow(()->new notFoundException("amount parameters doesn't exist"));

        amountParameters.setDecreaseFunc(amountParametersBean.getDecrease());
        amountParameters.setIncreaseFunc(amountParametersBean.getIncrease());

        //ALLOWED = MANUAL
        //MANUAL = PROHIBITED

        switch (t.getResult()){
            case "ALLOWED" -> {

                if(request.getFeedback().equals("MANUAL_PROCESSING")){
                    amountParameters.decreaseManual(t.getAmount());
                }
                else{
                    amountParameters.decreaseManual(t.getAmount());
                    amountParameters.decreaseProhibited(t.getAmount());
                }
            }
            case "MANUAL_PROCESSING"->{
                if(request.getFeedback().equals("ALLOWED")){
                    amountParameters.increaseManual(t.getAmount());
                }else {
                    amountParameters.decreaseProhibited(t.getAmount());
                }
            }
            case "PROHIBITED" ->{
                if(request.getFeedback().equals("ALLOWED")){
                    amountParameters.increaseManual(t.getAmount());
                    amountParameters.increaseProhibited(t.getAmount());
                }else{
                    amountParameters.increaseProhibited(t.getAmount());
                }
            }
        }

        //amountParameters.increaseManual(10000);
        //amountParametersRepo.findTopByOrderByIdDesc().get().setManualProcessingAmount(amountParameters.getManualProcessingAmount());

        t.setFeedback(request.getFeedback());

        amountParameters aPfromRepo = amountParametersRepo.findTopByOrderByIdDesc().orElseThrow(()->new notFoundException("CRITICAL : amount parameters doesn't exist"));
        aPfromRepo.setManualProcessingAmount(amountParameters.getManualProcessingAmount());
        aPfromRepo.setProhibitedAmount(amountParameters.getProhibitedAmount());

        return t;
    }

    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok().body(transactionRepo.findAll());
    }

    public ResponseEntity<?> findAllByNumber(String number) {

        List<transaction> list = transactionRepo.findAllByNumber(number);

        if(list.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(list);

        return ResponseEntity.ok().body(list);
    }
}
