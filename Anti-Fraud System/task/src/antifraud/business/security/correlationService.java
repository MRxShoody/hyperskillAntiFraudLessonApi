package antifraud.business.security;

import antifraud.models.enums.regions;
import antifraud.models.enums.transactionResultTypes;
import antifraud.models.requests.transactionRequest;
import antifraud.persitence.models.transaction.transaction;
import antifraud.persitence.models.user.user;
import antifraud.persitence.repos.transaction.transactionRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class correlationService {
    transactionRepo repo;

    @Autowired
    public correlationService(transactionRepo repo){
        this.repo = repo;
    }

    @Transactional
    public Map<String,transactionResultTypes> validate(transactionRequest request, user user){


        Map<String,transactionResultTypes> response = new HashMap<>();
        response.put("ip",transactionResultTypes.ALLOWED);
        response.put("region",transactionResultTypes.ALLOWED);

        regions region = request.region();
        long id = user.getId();
        String number = request.number();

        LocalDateTime t2 = request.date();
        LocalDateTime t1 = t2.minusHours(1);

        int regionCount = repo.countDistinctByRegionIsNotAndNumberAndTransactionDateBetween(region,number,t1,t2);
        int ipCount = repo.checkIP(t1,t2,number);

        if(ipCount == 3){
            response.put("ip",transactionResultTypes.MANUAL_PROCESSING);
        }
        if (regionCount == 3) {
            response.put("region",transactionResultTypes.MANUAL_PROCESSING);
        }

        if(ipCount > 3){
            response.put("ip",transactionResultTypes.PROHIBITED);
        }
        if(regionCount > 3){
            response.put("region",transactionResultTypes.PROHIBITED);
        }

        System.out.println(response);

        System.out.println("ipCount: " + ipCount);
        System.out.println("regionCount: " + regionCount);

        return response;
    }

}
