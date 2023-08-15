package antifraud.business.user;


import antifraud.exceptions.transaction.badRequestException;
import antifraud.exceptions.persistence.notFoundException;
import antifraud.exceptions.persistence.alreadyExistException;
import antifraud.models.enums.roles;
import antifraud.models.requests.registerRequest;
import antifraud.models.requests.unlockLockRequest;
import antifraud.models.responses.registerResponse;
import antifraud.models.responses.unlockLockResponse;
import antifraud.persitence.models.user.role;
import antifraud.persitence.models.user.user;
import antifraud.persitence.repos.user.roleRepository;
import antifraud.persitence.repos.user.userRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class userService {

    private final userRepository userRepository;
    private final roleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(userService.class);
    private boolean firstUser;

    @Autowired
    userService(userRepository userRepository, roleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        firstUser = userRepository.findFirstByOrderById().isEmpty();
    }

    @Transactional
    public registerResponse registerUser(registerRequest request) {
        String name = request.name();
        String username = request.username();
        String password = request.password();

        if (name == null || username == null || password == null || name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            throw new badRequestException("nulls or empty values are not allowed");
        }

        //legacy : remplaceable par les annotations validator de spring

        try {
            role role;
            String roleName;
            user newUser;

            if (firstUser) {
                role adminRole = roleRepository.findByRoleName(roles.ADMIN.getRoleName());
                newUser = new user(name, username, password, adminRole, false);
                roleName = roles.ADMIN.getRoleName();
                firstUser = false;

            } else {
                role = roleRepository.findByRoleName(roles.MERCHANT.getRoleName());
                newUser = new user(name, username, password, role, true);
                roleName = roles.MERCHANT.getRoleName();

                //new user + set role + save => table users actualité + hashset de role actualisé

                //Hashset de role + new user  => hashset actualisé + users table actualisé (car persist cascade) mais pas de role (sauf mis manuellement)

                //user existant + set role = table user actualisé + hashset de role actualisé

                //hashset de role + user existant => pas d'actualisation de role de user

                //MON CAS :
                // (hashset de role) + user existant + changement set role => actualisation de tout
            }

            userRepository.save(newUser);

            long id = newUser.getId();
            return new registerResponse(id, name, username, roleName);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new alreadyExistException("user already exists");
        }

    }

    @Transactional
    public Map<String, String> deleteUser(String username) {
        long count = userRepository.deleteByUsername(username);

        if (count == 0)
            throw new notFoundException("user not found");

        return Map.of("username", username, "status", "Deleted successfully!");
    }

    @Transactional
    public unlockLockResponse setAccess(unlockLockRequest request) {
        String username = request.username();

        switch (request.operation().toLowerCase()) {
            case "lock" -> lockUser(username);
            case "unlock" -> unlockUser(username);
            default -> throw new badRequestException("action not found");
        }
        return new unlockLockResponse("User " + username + " " + request.operation().toLowerCase() + "ed!");
    }

    @Transactional
    public void lockUser(String username) {
        user user = userRepository.findByUsername(username).orElseThrow(() -> new notFoundException("user not found"));
        user.setLocked(true);
    }

    @Transactional
    public void unlockUser(String username) {
        user user = userRepository.findByUsername(username).orElseThrow(() -> new notFoundException("user not found"));
        user.setLocked(false);
    }

    public List<user> findAll() {
        return (List<user>) userRepository.findAll();
    }

}
