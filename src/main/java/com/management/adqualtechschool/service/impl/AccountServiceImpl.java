package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.common.Message;
import com.management.adqualtechschool.common.RoleType;
import com.management.adqualtechschool.dto.AccountCreationDTO;
import com.management.adqualtechschool.dto.AccountDTO;
import com.management.adqualtechschool.dto.SubjectDTO;
import com.management.adqualtechschool.entity.Account;
import com.management.adqualtechschool.entity.Classroom;
import com.management.adqualtechschool.entity.Role;
import com.management.adqualtechschool.entity.Subject;
import com.management.adqualtechschool.entity.TeachSubject;
import com.management.adqualtechschool.repository.AccountRepository;
import com.management.adqualtechschool.repository.ClassroomRepository;
import com.management.adqualtechschool.repository.RoleRepository;
import com.management.adqualtechschool.repository.ScopeRepository;
import com.management.adqualtechschool.repository.SubjectRepository;
import com.management.adqualtechschool.repository.TeachSubjectRepository;
import com.management.adqualtechschool.service.AccountService;
import com.management.adqualtechschool.service.SubjectService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.CLASS_NAME_DEFAULT;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.GRADE_NAME_DEFAULT;
import static com.management.adqualtechschool.common.Message.BIRTHDAY_NOT_VALID;
import static com.management.adqualtechschool.common.Message.GENERAL_TEACHER_SUBJECT;
import static com.management.adqualtechschool.common.Message.NOT_CONTAIN_ROLE;
import static com.management.adqualtechschool.common.Message.NOT_FOUND_ACCOUNT_ID;
import static com.management.adqualtechschool.common.Message.NOT_FOUND_ACCOUNT_USERNAME;
import static com.management.adqualtechschool.common.Message.PUPIL_PREFIX;
import static com.management.adqualtechschool.common.Message.SEARCH_EMPTY;
import static com.management.adqualtechschool.common.Message.TEACHER_PREFIX;
import static com.management.adqualtechschool.common.RoleType.TEACHER_POSITION;
import static com.management.adqualtechschool.common.SaveFileDir.ACCOUNT_IMAGE_DIR;
import static com.management.adqualtechschool.common.SaveFileDir.STATIC_DIR;

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

    @Autowired
    private ClassroomRepository classroomRepository;

    private static final String DEFAULT_ADDRESS = " -  -  - ";
    private final SubjectRepository subjectRepository;
    private final ScopeRepository scopeRepository;
    private final TeachSubjectRepository teachSubjectRepository;

    public AccountServiceImpl(AccountRepository accountRepository,
                              SubjectRepository subjectRepository,
                              ScopeRepository scopeRepository,
                              TeachSubjectRepository teachSubjectRepository) {
        this.accountRepository = accountRepository;
        this.subjectRepository = subjectRepository;
        this.scopeRepository = scopeRepository;
        this.teachSubjectRepository = teachSubjectRepository;
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
        AccountDTO accountDTO = modelMapper.map(account, AccountDTO.class);
        accountDTO.setBirthday(LocalDate.from(account.getBirthday()));
        return accountDTO;
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

        List<Account> teacherList = subjectDTO.getTeachSubjectList()
                .stream().map(TeachSubject::getTeacher)
                .collect(Collectors.toList());
        Set<AccountDTO> teacherListDTO = teacherList
                .stream()
                .map(teacher -> modelMapper.map(teacher, AccountDTO.class))
                .collect(Collectors.toSet());
        return paginate(pageable, new ArrayList<>(teacherListDTO));
    }

    @Override
    public Page<AccountDTO> searchTeachersPaginated(Pageable pageable, String search) {
        List<Account> accountList = accountRepository.searchTeachers(search.toLowerCase());
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
        Role roleTeacher = roleRepository.findRoleByName(RoleType.TEACHER_ROLE);
        Role roleManager = roleRepository.findRoleByName(RoleType.MANAGER_ROLE);

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
        Role roleTeacher = roleRepository.findRoleByName(RoleType.TEACHER_ROLE);
        account.setRoles(Set.of(roleTeacher));
        accountRepository.save(account);

        roleTeacher.getAccounts().add(account);
        roleRepository.save(roleTeacher);

        // return account to get username, password of account teacher
        accountDTO.setUsername(defaultUsername);
        accountDTO.setPassword(defaultPassword);
        return accountDTO;
    }

    @Override
    public List<AccountDTO> getAllPupilAccount() {
        List<Account> pupilList = accountRepository.findAllPupil();
        return pupilList.stream()
                .map(pupil -> modelMapper.map(pupil, AccountDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<AccountDTO> getListPupilPaginated(Pageable pageable) {
        List<AccountDTO> pupilDTOList = getAllPupilAccount();
        return paginate(pageable, pupilDTOList);
    }

    @Override
    public Page<AccountDTO> filterPupilPaginated(Pageable pageable, String gradeName, String className) {
        List<Account> pupilList;
        // if gradeName not equal default gradeName and className equal default classname in view
        if (!gradeName.equals(GRADE_NAME_DEFAULT) && className.equals(CLASS_NAME_DEFAULT)) {
            pupilList = accountRepository.findAllPupilByGradeName(gradeName.replace(GRADE_NAME_DEFAULT, CLASS_NAME_DEFAULT));
            List<AccountDTO> pupilDTOList = pupilList.stream()
                    .map(pupil -> modelMapper.map(pupil, AccountDTO.class))
                    .collect(Collectors.toList());
            return paginate(pageable, pupilDTOList);
        }
        // if gradeName equal default gradeName and className not equal default classname in view
        if (gradeName.equals(GRADE_NAME_DEFAULT) && !className.equals(CLASS_NAME_DEFAULT)) {
            pupilList = accountRepository.findAllPupilByClassroomName(className);
            List<AccountDTO> pupilDTOList = pupilList.stream()
                .map(pupil -> modelMapper.map(pupil, AccountDTO.class))
                .collect(Collectors.toList());
            return paginate(pageable, pupilDTOList);
        }
        return paginate(pageable, getAllPupilAccount());
    }

    @Override
    public Page<AccountDTO> searchPupilPaginated(Pageable pageable, String search) {
        List<Account> accountList = accountRepository.searchPupil(search.toLowerCase());
        List<AccountDTO> accountDTOList = accountList.stream()
                .map(account -> modelMapper.map(account, AccountDTO.class))
                .collect(Collectors.toList());
        return paginate(pageable, accountDTOList);
    }

    @Override
    public AccountCreationDTO savePupil(AccountCreationDTO accountDTO, String className) {
        // set default username and password
        Long currentId = accountRepository.findMaxId();
        String defaultUsername = PUPIL_PREFIX + LocalDateTime.now().getYear() + (currentId+1);
        String defaultPassword = PUPIL_PREFIX  + LocalDateTime.now().getYear() + (currentId+1) + "@PU";
        Account account = modelMapper.map(accountDTO, Account.class);
        account.setUsername(defaultUsername);
        account.setPassword(encoder.encode(defaultPassword));

        if (LocalDate.now().minusYears(100).isAfter(accountDTO.getBirthday())) {
            throw new DateTimeException(BIRTHDAY_NOT_VALID);
        }

        if (LocalDate.now().isBefore(accountDTO.getBirthday())) {
            throw new DateTimeException(BIRTHDAY_NOT_VALID);
        }
        account.setBirthday(accountDTO.getBirthday().atStartOfDay());
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        // set role for account
        Role rolePupil = roleRepository.findRoleByName(RoleType.PUPIL_ROLE);
        account.setRoles(Set.of(rolePupil));
        rolePupil.getAccounts().add(account);
        roleRepository.save(rolePupil);

        // set classroom for account
        Classroom classroom = classroomRepository.findClassroomByName(className);
        classroom.getPupils().add(account);
        classroomRepository.save(classroom);

        // save account
        account.setClassroom(classroom);
        accountRepository.save(account);

        // return username and password
        accountDTO.setUsername(defaultUsername);
        accountDTO.setPassword(defaultPassword);
        return accountDTO;
    }

    @Override
    public void updateAccount(AccountDTO account, Authentication auth, String address,
                              List<Long> subjectList, Long classId, Long classInChargedId) {
        Account accountSave = accountRepository.findById(account.getId()).orElse(null);
        if (accountSave == null) {
            throw new NoSuchElementException(NOT_FOUND_ACCOUNT_ID);
        }

        if (account.getBirthday().isAfter(LocalDate.now())) {
            throw new DateTimeException(BIRTHDAY_NOT_VALID);
        }

        accountSave.setGender(account.getGender());
        accountSave.setBirthday(account.getBirthday().atStartOfDay());
        accountSave.setPosition(account.getPosition());
        accountSave.setLevel(account.getLevel());
        accountSave.setRank(account.getRank());
        accountSave.setPhone(account.getPhone());
        accountSave.setEmail(account.getEmail());
        accountSave.setUpdatedAt(LocalDateTime.now());

        // resolve classroom
        if (classId != null) {
            Classroom classroom = classroomRepository.findById(classId).orElse(null);
            if (classroom != null) {
                accountSave.setClassroom(classroom);
            }
        }

        // resolve class in charged
        if (classInChargedId != null) {
            Classroom classInCharged = classroomRepository.findById(classInChargedId).orElse(null);
            if (classInCharged != null) {
                accountSave.setClassInCharged(classInCharged);
            }
        } else {
            accountSave.setClassInCharged(null);
        }
        // resolve address
        if (address.equals(DEFAULT_ADDRESS)) {
            accountSave.setAddress(null);
        }
        accountSave.setAddress(address);

        List<TeachSubject> teachSubjectBefore = teachSubjectRepository.findAllByTeacherUsername(auth.getName());
        teachSubjectRepository.deleteAll(teachSubjectBefore);

        // save list subject
        if (subjectList != null) {
            List<TeachSubject> teachSubjectList = new ArrayList<>();

            for (Long subjectId : subjectList) {
                Subject subject = subjectRepository.findById(subjectId).orElse(null);
                // valid teachSubject exist or not
                if (subject != null) {
                    TeachSubject teachSubject = new TeachSubject();
                    teachSubject.setSubject(subject);
                    teachSubject.setTeacher(accountSave);
                    teachSubject.setCreatedAt(LocalDateTime.now());
                    teachSubject.setUpdatedAt(LocalDateTime.now());
                    teachSubjectRepository.save(teachSubject);
                    teachSubjectList.add(teachSubject);
                }
            }
            accountSave.setTeachSubjectList(teachSubjectList);
        } else {
            accountSave.setTeachSubjectList(null);
        }

        // update image
        String imageName;
        if (account.getMultipartFile() != null) {
            imageName = String.valueOf(account.getMultipartFile().getOriginalFilename());
            if (!imageName.equals("")) {
                try {
                    byte[] imageBytes = account.getMultipartFile().getBytes();
                    if (!(ACCOUNT_IMAGE_DIR + imageName).equals(account.getImage())) {
                        Path imagePath = Path.of(STATIC_DIR + ACCOUNT_IMAGE_DIR + imageName);
                        Files.write(imagePath, imageBytes);
                    }
                    accountSave.setImage(ACCOUNT_IMAGE_DIR + imageName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        accountRepository.save(accountSave);
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
