package ru.skypro.lessons.springweb.weblibrary2.security;

import org.springframework.security.core.GrantedAuthority;

public class SecurityGrantedAuthorities implements GrantedAuthority {
    private String role;

    public SecurityGrantedAuthorities(Authority authority) {
        this.role = authority.getRole();
    }

    @Override
    public String getAuthority() {
        return this.role;
    }
}
