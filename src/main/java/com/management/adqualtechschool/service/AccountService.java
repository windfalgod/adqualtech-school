package com.management.adqualtechschool.service;

import com.management.adqualtechschool.dto.AccountCreationDTO;
import com.management.adqualtechschool.dto.AccountDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountService {
    AccountCreationDTO getAccountCreationByUsername(String username);
    AccountDTO getAccountById(Long id);
    void deleteById(Long id);
    AccountDTO getAccountByUsername(String username);
    List<AccountDTO> getAllTeacherAdminAccount();
    String changePassword(AccountCreationDTO account, String currentPassword, String newPassword);
    List<AccountDTO> getAllTeacherAccount(String position);
    Page<AccountDTO> getListTeacherPaginated(Pageable pageable);
    Page<AccountDTO> filterTeacherPaginatedBySubjectName(Pageable pageable, String subjectName);
    Page<AccountDTO> searchTeachersPaginated(Pageable pageable, String search);
    void upgradeTeacherRole(Long id);
}
