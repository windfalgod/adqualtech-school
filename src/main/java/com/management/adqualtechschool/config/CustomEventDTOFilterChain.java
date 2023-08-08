package com.management.adqualtechschool.config;

import com.management.adqualtechschool.dto.EventDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.CREATOR;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.MONTH_AGO;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SCOPE;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.TODAY;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.WEEK_AGO;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.YEAR_AGO;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.YESTERDAY;

public class CustomEventDTOFilterChain {

    List<EventDTO> eventDTOList;

    public CustomEventDTOFilterChain(List<EventDTO> listValue) {
        this.eventDTOList = listValue;
    }

    public CustomEventDTOFilterChain filterByStartAt(LocalDateTime startAt) {
        if (startAt == null) {
            return new CustomEventDTOFilterChain(eventDTOList);
        }
        eventDTOList = eventDTOList.stream()
                .filter(eventDTO -> eventDTO.getStartAt().isAfter(startAt))
                .collect(Collectors.toList());
       return new CustomEventDTOFilterChain(eventDTOList);
    }

    public CustomEventDTOFilterChain filterByEndAt(LocalDateTime endAt) {
        if (endAt == null) {
            return new CustomEventDTOFilterChain(eventDTOList);
        }
        eventDTOList = eventDTOList.stream()
                .filter(eventDTO -> eventDTO.getEndAt().isBefore(endAt))
                .collect(Collectors.toList());
        return new CustomEventDTOFilterChain(eventDTOList);
    }

    public CustomEventDTOFilterChain filterByCreatedAt(String createdAt) {
        if (createdAt == null) {
            return new CustomEventDTOFilterChain(eventDTOList);
        }
        eventDTOList = eventDTOList.stream()
                .filter(eventDTO -> {
                    switch (createdAt) {
                        case TODAY: return eventDTO.getCreatedAt()
                                .isAfter(LocalDateTime.now()
                                        .minusHours(LocalDateTime.now().getHour())
                                        .minusMinutes(LocalDateTime.now().getMinute())
                                        .minusSeconds(LocalDateTime.now().getSecond())
                                );
                        case YESTERDAY: return eventDTO.getCreatedAt()
                                .isAfter(LocalDateTime.now().minusDays(1)
                                        .minusHours(LocalDateTime.now().getHour())
                                        .minusMinutes(LocalDateTime.now().getMinute())
                                        .minusSeconds(LocalDateTime.now().getSecond())
                                );
                        case WEEK_AGO: return eventDTO.getCreatedAt()
                                .isAfter(LocalDateTime.now().minusWeeks(1));
                        case MONTH_AGO: return eventDTO.getCreatedAt()
                                .isAfter(LocalDateTime.now().minusMonths(1));
                        case YEAR_AGO:return eventDTO.getCreatedAt()
                                .isAfter(LocalDateTime.now().minusYears(1));
                        default: return true;
                    }
                })
                .collect(Collectors.toList());
        return new CustomEventDTOFilterChain(eventDTOList);
    }

    public CustomEventDTOFilterChain filterByScopeName(String scopeName) {
        if(scopeName == null) {
            return new CustomEventDTOFilterChain(eventDTOList);
        }

        if (scopeName.equals(SCOPE)) {
            return new CustomEventDTOFilterChain(eventDTOList);
        }

        eventDTOList = eventDTOList.stream()
                .filter(eventDTO -> eventDTO.getScope().getTitle().equals(scopeName))
                .collect(Collectors.toList());
        return new CustomEventDTOFilterChain(eventDTOList);
    }

    public CustomEventDTOFilterChain filterByCreatorName(String creatorName) {
        if (creatorName == null) {
            return new CustomEventDTOFilterChain(eventDTOList);
        }
        if (creatorName.equals(CREATOR)) {
            return new CustomEventDTOFilterChain(eventDTOList);
        }
        eventDTOList = eventDTOList.stream()
                .filter(eventDTO -> (eventDTO.getCreator().getLastName()
                        + " "
                        + eventDTO.getCreator().getFirstName())
                        .equals(creatorName))
                .collect(Collectors.toList());
        return new CustomEventDTOFilterChain(eventDTOList);
    }

    public List<EventDTO> getEventDTOList() {
        return eventDTOList;
    }
}
