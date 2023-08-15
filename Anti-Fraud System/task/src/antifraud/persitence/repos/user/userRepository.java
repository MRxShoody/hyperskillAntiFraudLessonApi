package antifraud.persitence.repos.user;

import antifraud.persitence.models.user.user;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface userRepository extends CrudRepository<user, Long> {

    Optional<user> findFirstByOrderById();

    Optional<user> findByUsername(String username);

    long deleteByUsername(String username);

}
