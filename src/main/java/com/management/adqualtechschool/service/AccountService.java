package com.management.adqualtechschool.service;

import com.management.adqualtechschool.dto.AccountCreationDTO;
import com.management.adqualtechschool.dto.AccountDTO;
import java.util.List;

public interface AccountService {
    AccountCreationDTO getAccountCreationByUsername(String username);
    AccountDTO getAccountById(Long id);
    AccountDTO getAccountByUsername(String username);
    List<AccountDTO> getAllTeacherAdminAccount();
    void changePassword(AccountCreationDTO account,String oldPassword, String newPassword);
}
