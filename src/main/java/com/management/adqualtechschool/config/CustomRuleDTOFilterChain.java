package com.management.adqualtechschool.config;

import com.management.adqualtechschool.dto.RuleDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.CLASS_NAME_DEFAULT;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.CREATOR;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.GRADE_NAME_DEFAULT;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.MONTH_AGO;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SCOPE;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.TODAY;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.WEEK_AGO;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.YEAR_AGO;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.YESTERDAY;

public class CustomRuleDTOFilterChain {

    List<RuleDTO> ruleDTOList;

    public CustomRuleDTOFilterChain(List<RuleDTO> listValue) {
        this.ruleDTOList = listValue;
    }

    public CustomRuleDTOFilterChain filterByStartAt(LocalDateTime startAt) {
        if (startAt == null) {
            return new CustomRuleDTOFilterChain(ruleDTOList);
        }
        ruleDTOList = ruleDTOList.stream()
                .filter(ruleDTO -> ruleDTO.getStartAt().isAfter(startAt))
                .collect(Collectors.toList());
        return new CustomRuleDTOFilterChain(ruleDTOList);
    }

    public CustomRuleDTOFilterChain filterByEndAt(LocalDateTime endAt) {
        if (endAt == null) {
            return new CustomRuleDTOFilterChain(ruleDTOList);
        }
        ruleDTOList = ruleDTOList.stream()
                .filter(ruleDTO -> ruleDTO.getEndAt().isBefore(endAt))
                .collect(Collectors.toList());
        return new CustomRuleDTOFilterChain(ruleDTOList);
    }

    public CustomRuleDTOFilterChain filterByCreatedAt(String createdAt) {
        if (createdAt == null) {
            return new CustomRuleDTOFilterChain(ruleDTOList);
        }
        ruleDTOList = ruleDTOList.stream()
                .filter(ruleDTO -> {
                    switch (createdAt) {
                        case TODAY: return ruleDTO.getCreatedAt()
                                .isAfter(LocalDateTime.now()
                                        .minusHours(LocalDateTime.now().getHour())
                                        .minusMinutes(LocalDateTime.now().getMinute())
                                        .minusSeconds(LocalDateTime.now().getSecond())
                                );
                        case YESTERDAY: return ruleDTO.getCreatedAt()
                                .isAfter(LocalDateTime.now().minusDays(1)
                                        .minusHours(LocalDateTime.now().getHour())
                                        .minusMinutes(LocalDateTime.now().getMinute())
                                        .minusSeconds(LocalDateTime.now().getSecond())
                                );
                        case WEEK_AGO: return ruleDTO.getCreatedAt()
                                .isAfter(LocalDateTime.now().minusWeeks(1));
                        case MONTH_AGO: return ruleDTO.getCreatedAt()
                                .isAfter(LocalDateTime.now().minusMonths(1));
                        case YEAR_AGO:return ruleDTO.getCreatedAt()
                                .isAfter(LocalDateTime.now().minusYears(1));
                        default: return true;
                    }
                })
                .collect(Collectors.toList());
        return new CustomRuleDTOFilterChain(ruleDTOList);
    }

    public CustomRuleDTOFilterChain filterByScopeName(String scopeName) {
        if(scopeName == null) {
            return new CustomRuleDTOFilterChain(ruleDTOList);
        }

        if (scopeName.equals(SCOPE)) {
            return new CustomRuleDTOFilterChain(ruleDTOList);
        }

        ruleDTOList = ruleDTOList.stream()
                .filter(ruleDTO -> {
                    return ruleDTO.getScope().getTitle().equals(scopeName)
                            || ruleDTO.getScope().getTitle()
                            .startsWith(scopeName.replace(GRADE_NAME_DEFAULT, CLASS_NAME_DEFAULT));
                })
                .collect(Collectors.toList());
        return new CustomRuleDTOFilterChain(ruleDTOList);
    }

    public CustomRuleDTOFilterChain filterByCreatorName(String creatorName) {
        if (creatorName == null) {
            return new CustomRuleDTOFilterChain(ruleDTOList);
        }
        if (creatorName.equals(CREATOR)) {
            return new CustomRuleDTOFilterChain(ruleDTOList);
        }
        ruleDTOList = ruleDTOList.stream()
                .filter(ruleDTO -> (ruleDTO.getCreator().getLastName()
                        + " "
                        + ruleDTO.getCreator().getFirstName())
                        .equals(creatorName))
                .collect(Collectors.toList());
        return new CustomRuleDTOFilterChain(ruleDTOList);
    }

    public List<RuleDTO> getRuleDTOList() {
        return ruleDTOList;
    }
}
