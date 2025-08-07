package com.example.bookstore.security;

import com.example.bookstore.entity.Customer;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import java.util.Collection;
import java.util.Map;

/**
 * A wrapper class that acts as the principal in Spring Security.
 * It combines UserDetails (for form login) and OidcUser (for OAuth/OIDC login)
 * by holding a reference to your application's Customer entity and the
 * original OIDC/OAuth2 attributes.
 */
public class CustomOAuth2User implements OidcUser, UserDetails {

    // --- Custom method to get your actual Customer entity ---
    @Getter
    private final Customer customer; // Your application's Customer entity
    private final OidcUser oidcUser; // The original OidcUser from Spring Security

    public CustomOAuth2User(Customer customer, OidcUser oidcUser) {
        this.customer = customer;
        this.oidcUser = oidcUser;
    }

    // --- OidcUser methods (delegating to the wrapped oidcUser) ---
    @Override
    public Map<String, Object> getAttributes() {
        return oidcUser.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // You can combine authorities from your Customer entity and OIDC customer if needed
        // For simplicity, let's prioritize authorities from your internal Customer entity
        // or delegate to oidcUser.getAuthorities() if your Customer doesn't manage roles
        return customer.getAuthorities(); // Assuming Customer entity has roles/authorities
        // Alternatively, if roles are only from OIDC: return oidcUser.getAuthorities();
    }

    @Override
    public String getName() {
        // This is typically the 'sub' claim or a preferred username from the OIDC provider
        return oidcUser.getName();
    }

    @Override
    public Map<String, Object> getClaims() {
        return oidcUser.getClaims();
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcUser.getIdToken();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return oidcUser.getUserInfo();
    }


    // --- UserDetails methods (delegating to your Customer entity) ---
    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public String getUsername() {
        return customer.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return customer.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return customer.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return customer.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return customer.isEnabled();
    }

}