package antifraud.business.user;

import antifraud.exceptions.exceptionProducer;
import antifraud.exceptions.transaction.badRequestException;
import antifraud.exceptions.user.alreadyHasRoleException;
import antifraud.models.enums.roles;
import antifraud.models.requests.changeRoleRequest;
import antifraud.models.responses.changeRoleResponse;
import antifraud.persitence.models.user.role;
import antifraud.persitence.models.user.user;
import antifraud.persitence.repos.user.roleRepository;
import antifraud.persitence.repos.user.userRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class roleService {

    private final roleRepository roleRepository;
    private final userRepository userRepository;

    Logger logger = LoggerFactory.getLogger(roleService.class);

    @Autowired
    roleService(roleRepository roleRepository, userRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        for (roles role : roles.values()) {
            insertRoleIfNotExists(role);
        }
    }

    @Transactional
    public void insertRoleIfNotExists(roles role) {
        String roleName = role.getRoleName();
        if (!roleRepository.existsByRoleName(roleName)) {
            roleRepository.save(role.getRole());
            logger.info("Role '{}' inserted", roleName);
        } else {
            logger.info("Role '{}' already exists", roleName);
        }
    }

    @Transactional
    public void setAdminRole(String username) {
        user user = userRepository.findByUsername(username).orElseThrow(exceptionProducer::userNotFoundExceptionException);
        role role = roleRepository.findByRoleName(roles.ADMIN.getRoleName());

        user.setRole(role);
    }

    @Transactional
    public void setSupportRole(String username) {
        user user = userRepository.findByUsername(username).orElseThrow(exceptionProducer::userNotFoundExceptionException);
        role role = roleRepository.findByRoleName(roles.SUPPORT.getRoleName());

        user.setRole(role);
    }

    @Transactional
    public void setMerchant(String username) {
        user user = userRepository.findByUsername(username).orElseThrow(exceptionProducer::userNotFoundExceptionException);
        role role = roleRepository.findByRoleName(roles.MERCHANT.getRoleName());

        user.setRole(role);
    }

    @Transactional
    public changeRoleResponse setRole(changeRoleRequest request) {
        roles role;
        try {
            role = roles.valueOf(request.role());
        } catch (IllegalArgumentException e) {
            throw new badRequestException("Role not found");
        }
        if (request.role().equals("ADMINISTRATOR")) {
            throw new badRequestException("You can't change the role of an administrator");
        }

        user user = userRepository.findByUsername(request.username()).orElseThrow(exceptionProducer::userNotFoundExceptionException);

        if (user.getRole().equals(request.role())) {
            throw new alreadyHasRoleException("User already has this role");
        }
        role roleToSet = roleRepository.findByRoleName(role.getRoleName());

        user.setRole(roleToSet);
        return new changeRoleResponse(user.getId(), user.getName(), user.getUsername(), user.getRole());

    }

}



