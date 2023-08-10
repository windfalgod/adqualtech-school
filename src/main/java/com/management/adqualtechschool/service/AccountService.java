package com.management.adqualtechschool.service;

import com.management.adqualtechschool.dto.AccountCreationDTO;
import com.management.adqualtechschool.dto.AccountDTO;
import java.util.List;

public interface AccountService {
    AccountCreationDTO getAccountCreationByUsername(String username);
    AccountDTO getAccountById(Long id);
    AccountDTO getAccountByUsername(String username);
    List<AccountDTO> getAllTeacherAdminAccount();
    String changePassword(AccountCreationDTO account, String currentPassword, String newPassword);
}
