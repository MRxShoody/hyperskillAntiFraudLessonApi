package antifraud.configuration.authentication;

import antifraud.exceptions.exceptionProducer;
import antifraud.persitence.models.user.user;
import antifraud.persitence.repos.user.userRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class userDetailsServiceImpl implements UserDetailsService {

    private final userRepository userRepository;

    Logger logger = LoggerFactory.getLogger(userDetailsServiceImpl.class);

    @Autowired
    public userDetailsServiceImpl(userRepository ur) {
        this.userRepository = ur;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        user user = userRepository.findByUsername(username).orElseThrow(exceptionProducer::userNotFoundExceptionException);

        userDetailsImpl u = new userDetailsImpl(user);

        logger.info("User '{}' logged in. Role {}", username, u.getAuthorities());
        return u;
    }
}
