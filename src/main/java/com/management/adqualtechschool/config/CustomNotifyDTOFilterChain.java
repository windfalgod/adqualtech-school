package com.management.adqualtechschool.config;

import com.management.adqualtechschool.dto.NotifyDTO;
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

public class CustomNotifyDTOFilterChain {

    List<NotifyDTO> notifyDTOList;

    public CustomNotifyDTOFilterChain(List<NotifyDTO> listValue) {
        this.notifyDTOList = listValue;
    }

    public CustomNotifyDTOFilterChain filterByCreatedAt(String createdAt) {
        if (createdAt == null) {
            return new CustomNotifyDTOFilterChain(notifyDTOList);
        }
        notifyDTOList = notifyDTOList.stream()
                .filter(notifyDTO -> {
                    switch (createdAt) {
                        case TODAY: return notifyDTO.getCreatedAt()
                                .isAfter(LocalDateTime.now()
                                        .minusHours(LocalDateTime.now().getHour())
                                        .minusMinutes(LocalDateTime.now().getMinute())
                                        .minusSeconds(LocalDateTime.now().getSecond())
                                );
                        case YESTERDAY: return notifyDTO.getCreatedAt()
                                .isAfter(LocalDateTime.now().minusDays(1)
                                        .minusHours(LocalDateTime.now().getHour())
                                        .minusMinutes(LocalDateTime.now().getMinute())
                                        .minusSeconds(LocalDateTime.now().getSecond())
                                );
                        case WEEK_AGO: return notifyDTO.getCreatedAt()
                                .isAfter(LocalDateTime.now().minusWeeks(1));
                        case MONTH_AGO: return notifyDTO.getCreatedAt()
                                .isAfter(LocalDateTime.now().minusMonths(1));
                        case YEAR_AGO:return notifyDTO.getCreatedAt()
                                .isAfter(LocalDateTime.now().minusYears(1));
                        default: return true;
                    }
                })
                .collect(Collectors.toList());
        return new CustomNotifyDTOFilterChain(notifyDTOList);
    }

    public CustomNotifyDTOFilterChain filterByScopeName(String scopeName) {
        if (scopeName == null) {
            return new CustomNotifyDTOFilterChain(notifyDTOList);
        }

        if (scopeName.equals(SCOPE)) {
            return new CustomNotifyDTOFilterChain(notifyDTOList);
        }

        notifyDTOList = notifyDTOList.stream()
                .filter(notifyDTO -> {
                    return notifyDTO.getScope().getTitle().equals(scopeName)
                            || notifyDTO.getScope().getTitle()
                            .startsWith(scopeName.replace(GRADE_NAME_DEFAULT, CLASS_NAME_DEFAULT));
                })
                .collect(Collectors.toList());
        return new CustomNotifyDTOFilterChain(notifyDTOList);
    }

    public CustomNotifyDTOFilterChain filterByCreatorName(String creatorName) {
        if (creatorName == null) {
            return new CustomNotifyDTOFilterChain(notifyDTOList);
        }
        if (creatorName.equals(CREATOR)) {
            return new CustomNotifyDTOFilterChain(notifyDTOList);
        }
        notifyDTOList = notifyDTOList.stream()
                .filter(notifyDTO -> (notifyDTO.getCreator().getLastName()
                        + " "
                        + notifyDTO.getCreator().getFirstName())
                        .equals(creatorName))
                .collect(Collectors.toList());
        return new CustomNotifyDTOFilterChain(notifyDTOList);
    }

    public List<NotifyDTO> getNotifyDTOList() {
        return notifyDTOList;
    }
}
