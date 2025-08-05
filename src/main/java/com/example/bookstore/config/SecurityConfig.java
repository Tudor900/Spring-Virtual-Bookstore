package com.example.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//
//@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authorize -> authorize
//                        // Allow access to the home page and static resources
//                        .requestMatchers("/", "/css/**", "/js/**").permitAll()
//                        // All other requests must be authenticated
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginPage("/login") // Tell Spring to use your custom login page
//                        .permitAll()
//                )
//                .oauth2Login(oauth2 -> oauth2
//                        // Enables the default OAuth2 login page
//                        // The /login/oauth2/code/google endpoint is automatically handled
//                        .defaultSuccessUrl("/secured-page", true)
//
//                )
//                ;
//
//        return http.build();
//    }
}



