package com.management.adqualtechschool.service;

import com.management.adqualtechschool.dto.AccountCreationDTO;

public interface AccountService {
    AccountCreationDTO getAccountCreationByUsername(String username);
}
