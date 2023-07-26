package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.dto.AccountCreationDTO;
import com.management.adqualtechschool.entity.Account;
import com.management.adqualtechschool.repository.AccountRepository;
import com.management.adqualtechschool.service.AccountService;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountCreationDTO getAccountCreationByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("Not found accountCreationDTO");
        }
        return modelMapper.map(account, AccountCreationDTO.class);
    }

    @Override
    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }
}
