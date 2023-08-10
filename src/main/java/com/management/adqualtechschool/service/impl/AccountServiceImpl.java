package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.common.Message;
import com.management.adqualtechschool.dto.AccountCreationDTO;
import com.management.adqualtechschool.dto.AccountDTO;
import com.management.adqualtechschool.entity.Account;
import com.management.adqualtechschool.repository.AccountRepository;
import com.management.adqualtechschool.service.AccountService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.management.adqualtechschool.common.Message.NOT_FOUND_ACCOUNT_ID;
import static com.management.adqualtechschool.common.Message.NOT_FOUND_ACCOUNT_USERNAME;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountCreationDTO getAccountCreationByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException(NOT_FOUND_ACCOUNT_USERNAME);
        }
        return modelMapper.map(account, AccountCreationDTO.class);
    }

    @Override
    public AccountDTO getAccountById(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        if (account.isPresent()) {
        return modelMapper.map(account, AccountDTO.class);
        }
        throw new EntityNotFoundException(NOT_FOUND_ACCOUNT_ID);
    }

    @Override
    public AccountDTO getAccountByUsername(String username) {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new EntityNotFoundException(NOT_FOUND_ACCOUNT_USERNAME + username);
        }
        return modelMapper.map(account, AccountDTO.class);
    }

    @Override
    public List<AccountDTO> getAllTeacherAdminAccount() {
        List<Account> accountList = accountRepository.findAllTeacherManager();
        return accountList.stream()
                .map(account -> modelMapper.map(account, AccountDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public String changePassword(AccountCreationDTO account, String currentPassword, String newPassword) {
        Optional<Account> accountOptional = accountRepository.findById(account.getId());
        if (!accountOptional.isPresent()) {
            throw new EntityNotFoundException(NOT_FOUND_ACCOUNT_USERNAME);
        }
        Account accountSave = accountOptional.orElse(null);
        if (!encoder.matches(currentPassword, accountSave.getPassword())) {
            return Message.NOT_MATCH;
        }
        accountSave.setPassword(encoder.encode(newPassword));
        accountRepository.save(accountSave);
        return Message.CHANGE_SUCCESS;
    }
}
