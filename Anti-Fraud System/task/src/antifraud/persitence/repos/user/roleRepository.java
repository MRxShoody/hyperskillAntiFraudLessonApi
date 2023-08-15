package antifraud.persitence.repos.user;

import antifraud.persitence.models.user.role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface roleRepository extends JpaRepository<role, Long> {
    boolean existsByRoleName(String roleName);

    role findByRoleName(String roleName);

}
