package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.dto.AccountCreationDTO;
import com.management.adqualtechschool.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.management.adqualtechschool.common.Message.NOT_FOUND_ACCOUNT_USERNAME;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountCreationDTO accountDTO = accountService.getAccountCreationByUsername(username);
        if (accountDTO == null) {
            throw new UsernameNotFoundException(NOT_FOUND_ACCOUNT_USERNAME);
        }
        return new AccountDetailsImpl(accountDTO);
    }
}
