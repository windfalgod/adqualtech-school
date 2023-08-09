package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.dto.AccountCreationDTO;
import com.management.adqualtechschool.dto.AccountDTO;
import com.management.adqualtechschool.entity.Account;
import com.management.adqualtechschool.repository.AccountRepository;
import com.management.adqualtechschool.service.AccountService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.management.adqualtechschool.common.Message.NOT_FOUND_ACCOUNT_ID;
import static com.management.adqualtechschool.common.Message.NOT_FOUND_ACCOUNT_USERNAME;
import static com.management.adqualtechschool.common.RoleType.TEACHER_POSITION;

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
    public List<AccountDTO> getAllTeacherAccount(String position) {
        List<Account> teacherList = accountRepository.findAllByPositionLikeOrderByCreatedAtDesc(position);
        return teacherList.stream()
                .map(teacher -> modelMapper.map(teacher, AccountDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<AccountDTO> getListTeacherPaginated(Pageable pageable) {
        List<AccountDTO> teacherList = getAllTeacherAccount(TEACHER_POSITION);
        return paginate(pageable, teacherList);
    }

    private Page<AccountDTO> paginate(Pageable pageable, List<AccountDTO> accountDTOList) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<AccountDTO> accountDTOPage;

        if (accountDTOList.size() < startItem) {
            accountDTOPage = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, accountDTOList.size());
            accountDTOPage = accountDTOList.subList(startItem, toIndex);
        }
        return new PageImpl<>(accountDTOPage, PageRequest.of(currentPage, pageSize), accountDTOList.size());
    }
}
