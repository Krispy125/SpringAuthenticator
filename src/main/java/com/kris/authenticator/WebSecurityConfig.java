package com.kris.authenticator;

import java.beans.Customizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    private final Application application;

    WebSecurityConfig(Application application) {
        this.application = application;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorize) -> authorize
            .anyRequest().fullyAuthenticated()
        )
        .formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .ldapAuthentication()
                .userDnPatterns("uid={0}, ou=people")
                .userSearchBase("ou-groups")
                .contextSource()
                    .url("ldap://localhost:8389/dc=springframework,dc=org")
                    .and()
                .passwordCompare()
                    .passwordEncoder(new BCryptPasswordEncoder())
                    .passwordAttribute("userPassword");
    }
}