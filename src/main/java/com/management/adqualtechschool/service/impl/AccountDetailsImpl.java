package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.dto.AccountCreationDTO;
import com.management.adqualtechschool.entity.Role;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AccountDetailsImpl implements UserDetails {

    private final AccountCreationDTO accountCreationDTO;

    public AccountDetailsImpl(AccountCreationDTO accountCreationDTO) {
        this.accountCreationDTO = accountCreationDTO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = accountCreationDTO.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return accountCreationDTO.getPassword();
    }

    @Override
    public String getUsername() {
        return accountCreationDTO.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
        return accountCreationDTO.getId();
    }

    public String getName() {
        return accountCreationDTO.getLastName() + " " + accountCreationDTO.getFirstName();
    }

    public String getImage() {
        return accountCreationDTO.getImage();
    }
}
