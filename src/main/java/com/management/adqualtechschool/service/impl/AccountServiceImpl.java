package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.common.Message;
import com.management.adqualtechschool.common.RoleType;
import com.management.adqualtechschool.dto.AccountCreationDTO;
import com.management.adqualtechschool.dto.AccountDTO;
import com.management.adqualtechschool.dto.SubjectDTO;
import com.management.adqualtechschool.entity.Account;
import com.management.adqualtechschool.entity.Role;
import com.management.adqualtechschool.repository.AccountRepository;
import com.management.adqualtechschool.repository.RoleRepository;
import com.management.adqualtechschool.service.AccountService;
import com.management.adqualtechschool.service.SubjectService;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.management.adqualtechschool.common.Message.BIRTHDAY_NOT_VALID;
import static com.management.adqualtechschool.common.Message.GENERAL_TEACHER_SUBJECT;
import static com.management.adqualtechschool.common.Message.NOT_CONTAIN_ROLE;
import static com.management.adqualtechschool.common.Message.NOT_FOUND_ACCOUNT_ID;
import static com.management.adqualtechschool.common.Message.NOT_FOUND_ACCOUNT_USERNAME;
import static com.management.adqualtechschool.common.Message.SEARCH_EMPTY;
import static com.management.adqualtechschool.common.Message.TEACHER_PREFIX;
import static com.management.adqualtechschool.common.RoleType.TEACHER_POSITION;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private RoleRepository roleRepository;

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
    public void deleteById(Long id) {
        accountRepository.deleteById(id);
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

    @Override
    public List<AccountDTO> getAllTeacherAccount() {
        List<Account> teacherList = accountRepository.findAllTeacherByPosition(TEACHER_POSITION);
        return teacherList.stream()
                .map(teacher -> modelMapper.map(teacher, AccountDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<AccountDTO> getListTeacherPaginated(Pageable pageable) {
        List<AccountDTO> teacherList = getAllTeacherAccount();
        return paginate(pageable, teacherList);
    }

    @Override
    public Page<AccountDTO> filterTeacherPaginatedBySubjectName(Pageable pageable, String subjectName) {
        if (subjectName.equals(GENERAL_TEACHER_SUBJECT)) {
            return getListTeacherPaginated(pageable);
        }
        SubjectDTO subjectDTO = subjectService.getSubjectByName(subjectName);
        List<Account> teacherList = subjectDTO.getAccounts();
        List<AccountDTO> teacherListDTO = teacherList
                .stream()
                .map(teacher -> modelMapper.map(teacher, AccountDTO.class))
                .collect(Collectors.toList());
        return paginate(pageable, teacherListDTO);
    }

    @Override
    public Page<AccountDTO> searchTeachersPaginated(Pageable pageable, String search) {
        List<Account> accountList = accountRepository.searchTeachers(search);
        if (search.equals(SEARCH_EMPTY)) {
            return paginate(pageable, getAllTeacherAccount());
        }
        List<AccountDTO> accountDTOList = accountList.stream()
                .map(account -> modelMapper.map(account, AccountDTO.class))
                .collect(Collectors.toList());
        return paginate(pageable, accountDTOList);
    }

    @Override
    public void upgradeToManagerRole(Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            throw new EntityNotFoundException(NOT_FOUND_ACCOUNT_ID);
        }

        Set<Role> roleSet = account.getRoles();
        Role roleTeacher = roleRepository.findRoleByName(RoleType.TEACHER);
        Role roleManager = roleRepository.findRoleByName(RoleType.MANAGER);

        if (!roleSet.contains(roleTeacher)) {
            throw new NoSuchElementException(NOT_CONTAIN_ROLE);
        }

        // Modify roles
        roleManager.getAccounts().add(account);
        roleTeacher.getAccounts().remove(account);
        roleSet.add(roleManager);
        roleSet.remove(roleTeacher);

        // Update roles and account
        roleRepository.saveAll(List.of(roleTeacher, roleManager));
        account.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(account);
    }

    @Override
    public AccountCreationDTO saveTeacher(AccountCreationDTO accountDTO) {

        // set default username and password
        Long currentId = accountRepository.findMaxId();
        String defaultUsername = TEACHER_PREFIX + LocalDateTime.now().getYear() + (currentId+1);
        String defaultPassword = TEACHER_PREFIX  + LocalDateTime.now().getYear() + (currentId+1) + "@TE";
        Account account = modelMapper.map(accountDTO, Account.class);
        account.setUsername(defaultUsername);
        account.setPassword(encoder.encode(defaultPassword));

        if (LocalDate.now().minusYears(200).isAfter(accountDTO.getBirthday())) {
            throw new DateTimeException(BIRTHDAY_NOT_VALID);
        }

        if (LocalDate.now().minusYears(10).isBefore(accountDTO.getBirthday())) {
            throw new DateTimeException(BIRTHDAY_NOT_VALID);
        }
        account.setBirthday(accountDTO.getBirthday().atStartOfDay());
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        account.setPosition(TEACHER_POSITION);

        // set role for account
        Role roleTeacher = roleRepository.findRoleByName(RoleType.TEACHER);
        account.setRoles(Set.of(roleTeacher));
        accountRepository.save(account);

        roleTeacher.getAccounts().add(account);
        roleRepository.save(roleTeacher);

        // return account to get username, password of account teacher
        accountDTO.setUsername(defaultUsername);
        accountDTO.setPassword(defaultPassword);
        return accountDTO;
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
