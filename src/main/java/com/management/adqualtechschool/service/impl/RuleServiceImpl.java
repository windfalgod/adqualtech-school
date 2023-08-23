package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.common.Message;
import com.management.adqualtechschool.config.CustomRuleDTOFilterChain;
import com.management.adqualtechschool.dto.AccountDTO;
import com.management.adqualtechschool.dto.RuleDTO;
import com.management.adqualtechschool.dto.ScopeDTO;
import com.management.adqualtechschool.entity.Account;
import com.management.adqualtechschool.entity.Rule;
import com.management.adqualtechschool.entity.Scope;
import com.management.adqualtechschool.repository.RuleRepository;
import com.management.adqualtechschool.service.AccountService;
import com.management.adqualtechschool.service.RuleService;
import com.management.adqualtechschool.service.ScopeService;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.CLASS_NAME_DEFAULT;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.CREATED_AT;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.GRADE_NAME_DEFAULT;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SCHOOL_WIDE;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SCOPE;
import static com.management.adqualtechschool.common.Message.SEARCH_EMPTY;
import static com.management.adqualtechschool.common.RoleType.PUPIL_ROLE;

@Service
public class RuleServiceImpl implements RuleService {
    
    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ScopeService scopeService;

    @Override
    public RuleDTO getRuleById(Long id) {
        Optional<Rule> rule = ruleRepository.findById(id);
        if (rule.isPresent()) {
            return modelMapper.map(rule, RuleDTO.class);
        }
        throw new EntityNotFoundException(Message.NOT_FOUND_RULE);
    }

    @Override
    public void deleteById(Long id) {
        ruleRepository.deleteById(id);
    }

    @Override
    public void saveOrUpdateRule(RuleDTO rule, String username) {
        AccountDTO accountDTO = accountService.getAccountByUsername(username);
        rule.setCreator(modelMapper.map(accountDTO, Account.class));

        ScopeDTO scopeDTO;
        if (rule.getScope().getTitle().equals(SCOPE)) {
            scopeDTO = scopeService.getScopeByTitle(SCHOOL_WIDE);
        } else {
            scopeDTO = scopeService.getScopeByTitle(rule.getScope().getTitle());
        }
        rule.setScope(modelMapper.map(scopeDTO, Scope.class));

        if (rule.getId() == null) {
            rule.setCreatedAt(LocalDateTime.now());
        } else {
            ruleRepository.findById(rule.getId())
                    .ifPresent(ruleSaved -> rule.setCreatedAt(ruleSaved.getCreatedAt()));
        }
        rule.setUpdatedAt(LocalDateTime.now());

        if (rule.getStartAt().isAfter(rule.getEndAt())) {
            throw new DateTimeException(Message.START_BEFORE_END_RULE);
        }
        if (rule.getStartAt().isBefore(rule.getCreatedAt())) {
            throw new DateTimeException(Message.CREATE_BEFORE_START_RULE);
        }
        ruleRepository.save(modelMapper.map(rule, Rule.class));
    }

    @Override
    public List<RuleDTO> getAllRule() {
        List<Rule> ruleList = ruleRepository.findAll(Sort.by(Sort.Direction.DESC, CREATED_AT));
        return ruleList.stream()
                .map(Rule -> modelMapper.map(Rule, RuleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RuleDTO> getRulesByClassName(String className) {
        List<Rule> ruleList = ruleRepository.findRulesByClassNameOrderByCreatedAtDesc(className.toLowerCase());
        return ruleList.stream()
                .map(rule -> modelMapper.map(rule, RuleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RuleDTO> getRulesByGradeName(String gradeName) {
        List<Rule> ruleList = ruleRepository.findRulesByGradeNameOrderByCreatedAtDesc(gradeName.toLowerCase());
        return ruleList.stream()
                .map(rule -> modelMapper.map(rule, RuleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RuleDTO> getRulesBySchoolWide() {
        List<Rule> ruleList = ruleRepository.findRulesBySchoolWideOrderByCreatedAtDesc();
        return ruleList.stream()
                .map(rule -> modelMapper.map(rule, RuleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RuleDTO> getRulesByStudentAccount(Long id) {
        AccountDTO account = accountService.getAccountById(id);
        if (account == null) {
            throw new NoSuchElementException(Message.NOT_FOUND_ACCOUNT_ID);
        }
        String nameClass = account.getClassroom().getName();
        List<RuleDTO> classRules = getRulesByClassName(nameClass);
        String gradeName = nameClass.replace(CLASS_NAME_DEFAULT, GRADE_NAME_DEFAULT).substring(0, nameClass.length());
        List<RuleDTO> gradeRules = getRulesByGradeName(gradeName);
        List<RuleDTO> schoolRules = getRulesBySchoolWide();
        classRules.addAll(gradeRules);
        classRules.addAll(schoolRules);
        classRules.sort(Comparator.comparing(RuleDTO::getCreatedAt).reversed());
        return classRules;
    }

    @Override
    public Page<RuleDTO> getListRulesPaginated(Pageable pageable, Authentication auth) {
        List<RuleDTO> RuleDTOList = getRulesFollowAuth(auth);
        return paginate(pageable, RuleDTOList);
    }

    // filter rule and paginate to page to display
    @Override
    public Page<RuleDTO> filterRulesPaginated(Pageable pageable, Authentication auth,
                                              LocalDate startAt, LocalDate endAt, String createdAt,
                                              String scopeName, String creatorName) {
        List<RuleDTO> RuleDTOList = getRulesFollowAuth(auth);
        LocalDateTime startAtDateTime = null;
        LocalDateTime endAtDateTime = null;

        if (startAt != null) {
            startAtDateTime = startAt.atStartOfDay();
        }
        if (endAt != null) {
            endAtDateTime = endAt.atStartOfDay();
        }
        RuleDTOList = filterRules(RuleDTOList, startAtDateTime, endAtDateTime,
                createdAt, scopeName, creatorName);
        return paginate(pageable, RuleDTOList);
    }

    // search rule follow search and paginate to page to display
    @Override
    public Page<RuleDTO> searchRulesPaginated(Pageable pageable, Authentication auth, String search) {
        List<RuleDTO> RuleDTOList = getRulesFollowAuth(auth);
        if (search.equals(SEARCH_EMPTY)) {
            return paginate(pageable, RuleDTOList);
        }
        String searchString = search.toLowerCase().trim();

        // filter by rule title or by last name and first name
        RuleDTOList = RuleDTOList.stream()
                .filter(RuleDTO -> RuleDTO.getTitle().toLowerCase().contains(searchString)
                        || (RuleDTO.getCreator().getLastName()
                        + " "
                        + RuleDTO.getCreator().getFirstName())
                        .toLowerCase().contains(searchString))
                .collect(Collectors.toList());
        return paginate(pageable, RuleDTOList);
    }

    @Override
    public List<AccountDTO> getAllCreator() {
        List<Account> creatorList = ruleRepository.findAllRuleCreator();
        return creatorList.stream()
                .map(creator -> modelMapper.map(creator, AccountDTO.class))
                .collect(Collectors.toList());
    }

    private List<RuleDTO> filterRules(List<RuleDTO> ruleDTOList, LocalDateTime startAt,
                                      LocalDateTime endAt, String createdAt,
                                      String scopeName, String creatorName) {
        return new CustomRuleDTOFilterChain(ruleDTOList)
                .filterByStartAt(startAt)
                .filterByEndAt(endAt)
                .filterByCreatedAt(createdAt)
                .filterByScopeName(scopeName)
                .filterByCreatorName(creatorName)
                .getRuleDTOList();
    }

    // get rule based on authorization
    private List<RuleDTO> getRulesFollowAuth(Authentication auth) {
        AccountDetailsImpl accountDetails = (AccountDetailsImpl) auth.getPrincipal();
        Long accountId = accountDetails.getId();
        String role = accountDetails.getAuthorities().iterator().next().getAuthority();

        List<RuleDTO> RuleDTOList;

        if (role.equals(PUPIL_ROLE)) {
            RuleDTOList = getRulesByStudentAccount(accountId);
        } else {
            RuleDTOList = getAllRule();
        }
        return RuleDTOList;
    }

    // paginate list of rule to page
    private Page<RuleDTO> paginate(Pageable pageable, List<RuleDTO> RuleDTOList) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<RuleDTO> RuleDTOPage;

        if (RuleDTOList.size() < startItem) {
            RuleDTOPage = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, RuleDTOList.size());
            RuleDTOPage = RuleDTOList.subList(startItem, toIndex);
        }
        return new PageImpl<>(RuleDTOPage, PageRequest.of(currentPage, pageSize), RuleDTOList.size());
    }
}
