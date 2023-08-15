package antifraud.configuration.authorization;

import antifraud.configuration.authentication.userDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class AuthorizationConfig {
    private final userDetailsServiceImpl userDetailsServiceImpl;
    private final PasswordEncoder encoder;

    @Autowired
    public AuthorizationConfig(PasswordEncoder encoder, userDetailsServiceImpl userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.encoder = encoder;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsServiceImpl)
                .passwordEncoder(encoder)
                .and().build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(requests -> requests

                        .requestMatchers("/actuator/shutdown").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/antifraud/suspicious-ip").hasAuthority("SUPPORT")
                        .requestMatchers(HttpMethod.GET, "/api/antifraud/stolencard", "/api/antifraud/suspicious-ip").hasAuthority("SUPPORT")
                        .requestMatchers(HttpMethod.POST, "/api/antifraud/stolencard", "/api/antifraud/suspicious-ip").hasAuthority("SUPPORT")
                        .requestMatchers(HttpMethod.DELETE, "/api/antifraud/stolencard/*", "/api/antifraud/suspicious-ip/*").hasAuthority("SUPPORT")
                        .requestMatchers(HttpMethod.POST, "/api/auth/user").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auth/list").hasAnyAuthority("SUPPORT", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/auth/user/*").hasAuthority("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/api/antifraud/transaction").hasAuthority("MERCHANT")
                        .requestMatchers(HttpMethod.PUT, "/api/auth/role").hasAuthority("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PUT, "/api/auth/access").hasAuthority("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/api/antifraud/history").hasAuthority("SUPPORT")
                        .requestMatchers(HttpMethod.GET, "/api/antifraud/history/*").hasAuthority("SUPPORT")
                        .requestMatchers(HttpMethod.PUT, "/api/antifraud/transaction").hasAuthority("SUPPORT")

                        .anyRequest().denyAll()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf().disable()
                .headers(headers -> headers.frameOptions().disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                )
                .build();
    }
}