package com.management.adqualtechschool.service;

import com.management.adqualtechschool.dto.AccountDTO;
import com.management.adqualtechschool.dto.RuleDTO;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface RuleService {
    RuleDTO getRuleById(Long id);
    void deleteById(Long id);
    void saveOrUpdateRule(RuleDTO rule, String username);
    List<RuleDTO> getAllRule();
    List<RuleDTO> getRulesByClassName(String className);
    List<RuleDTO> getRulesByGradeName(String gradeName);
    List<RuleDTO> getRulesBySchoolWide();
    List<RuleDTO> getRulesByStudentAccount(Long id);
    Page<RuleDTO> getListRulesPaginated(Pageable pageable, Authentication auth);
    Page<RuleDTO> filterRulesPaginated(Pageable pageable, Authentication auth,
                                         LocalDate startAt, LocalDate endAt, String createdAt,
                                         String scopeName, String creatorName);
    Page<RuleDTO> searchRulesPaginated(Pageable pageable, Authentication auth,
                                         String search);
    List<AccountDTO> getAllCreator();
}
