package antifraud.persitence.repos.security;

import antifraud.persitence.models.security.stolenCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface stolenCardRepository extends CrudRepository<stolenCard, Long> {
    Optional<stolenCard> findByNumber(String cardNumber);

    boolean existsByNumber(String Number);

    Integer deleteByNumber(String Number);
}
