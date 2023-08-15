package antifraud.business.security;

import antifraud.exceptions.persistence.notFoundException;

import antifraud.business.security.interfaces.securityService;
import antifraud.exceptions.persistence.alreadyExistException;
import antifraud.persitence.models.security.suspiciousIP;
import antifraud.persitence.repos.security.suspiciousIpRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//TODO: Implement
@Service
public class suspiciousIpService extends securityService<suspiciousIP, suspiciousIpRepository> {


    @Autowired
    public suspiciousIpService(suspiciousIpRepository repo){
        super(repo);
    }

    @Override
    @Transactional
    public suspiciousIP add(suspiciousIP suspiciousIP) {
        try {
            return repo.save(suspiciousIP);
        }catch (Exception e){
            throw new alreadyExistException("IP already exists");
        }

    }

    @Override
    @Transactional
    public boolean delete(String suspiciousIP) {
        if(repo.deleteByIp(suspiciousIP) == 0){
            throw new notFoundException("IP not found");
        }
        return true;
    }

    @Override
    public List<suspiciousIP> getAll() {
        return (List<suspiciousIP>) repo.findAll();
    }

    @Override
    public boolean isBlocked(String suspiciousIP) {
        return repo.existsByIp(suspiciousIP);
    }
}
