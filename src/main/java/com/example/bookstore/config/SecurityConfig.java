package com.example.bookstore.config;

import com.example.bookstore.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Assuming you use BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder; // Assuming you use PasswordEncoder
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    private final CustomOAuth2UserService customOAuth2UserService;



    public SecurityConfig(UserDetailsService userDetailsService, CustomOAuth2UserService customOAuth2UserService) {
        this.userDetailsService = userDetailsService;
        this.customOAuth2UserService = customOAuth2UserService;
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .authorizeHttpRequests(req -> req
                        // Permit access to H2 console (important for development)
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                        // Permit access to the registration page
                        .requestMatchers("/register/**").permitAll()
                        // Permit access to your custom login page
                        .requestMatchers("/login").permitAll()
                        // Any other request requires authentication
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Specify your custom login page URL
                        .loginProcessingUrl("/login") // This is the default, but good to be explicit
                        .defaultSuccessUrl("/", true) // Redirect to home page after successful login
                        .failureUrl("/login?error") // Redirect back to login with error param on failure
                        .permitAll() // Allow everyone to access the login page
                )
                // Configure OAuth2 login to NOT use its default login page,
                // but still enable the OAuth2 authentication flow.
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login") // Direct OAuth2 to use your custom login page
                        .defaultSuccessUrl("/", true) // Redirect to home after OAuth2 login
                        .failureUrl("/login?oauth_error")
                        .userInfoEndpoint(userInfo -> userInfo
                                // The fix is here:
                                .oidcUserService(customOAuth2UserService)
                        )
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // Default, but explicit
                        .logoutSuccessUrl("/login?logout") // Redirect to login page with logout param on success
                        .permitAll() // Allow everyone to access the logout URL
                )
                .userDetailsService(userDetailsService)
                .headers(headers -> headers
                        // Allow H2 console to be displayed in a frame (important for development)
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }
}