package antifraud.persitence.repos.security;

import antifraud.persitence.models.security.suspiciousIP;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface suspiciousIpRepository extends CrudRepository<suspiciousIP, Long> {
    Optional<suspiciousIP> findByIp(String ip);

    boolean existsByIp(String ip);

    Integer deleteByIp(String ip);
}

