package antifraud.persitence.repos.transaction;

import antifraud.persitence.models.limits.amountParameters;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface amountParametersRepo extends CrudRepository<amountParameters, Long> {



    Optional<amountParameters> findTopByOrderByIdDesc();

    Optional<amountParameters> findByTransactions_Number(String number);
}
