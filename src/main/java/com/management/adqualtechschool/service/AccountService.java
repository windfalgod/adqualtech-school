package com.management.adqualtechschool.service;

import com.management.adqualtechschool.dto.AccountCreationDTO;
import com.management.adqualtechschool.entity.Account;
import java.util.Optional;

public interface AccountService {
    AccountCreationDTO getAccountCreationByUsername(String username);
    Optional<Account> getAccountById(Long id);
}
