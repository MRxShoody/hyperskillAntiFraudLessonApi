package antifraud.business.security;

import antifraud.business.security.interfaces.securityService;
import antifraud.exceptions.persistence.notFoundException;
import antifraud.exceptions.persistence.alreadyExistException;
import antifraud.persitence.models.security.stolenCard;
import antifraud.persitence.repos.security.stolenCardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//TODO: Implement
@Service
public class stolenCardService extends securityService<stolenCard, stolenCardRepository> {


    @Autowired
    public stolenCardService(stolenCardRepository repo){
        super(repo);
    }

    @Override
    @Transactional
    public stolenCard add(stolenCard stolenCard) {
        try {
          return repo.save(stolenCard);
        }catch (Exception e){
            throw new alreadyExistException("Card already exists");
        }
    }

    @Override
    @Transactional
    public boolean delete(String cardNumber) {
        if(repo.deleteByNumber(cardNumber) == 0){
            throw new notFoundException("Card not found");
        }
        return true;
    }

    @Override
    public List<stolenCard> getAll() {
        return (List<stolenCard>)repo.findAll();
    }

    @Override
    public boolean isBlocked(String number) {
        return repo.existsByNumber(number);
    }

}
