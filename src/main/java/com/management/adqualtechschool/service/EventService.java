package com.management.adqualtechschool.service;

import com.management.adqualtechschool.dto.AccountDTO;
import com.management.adqualtechschool.dto.EventDTO;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface EventService {
    EventDTO getEventById(Long id);
    void deleteById(Long id);
    void saveOrUpdateEvent(EventDTO event, String username, MultipartFile imageUpload);
    List<EventDTO> getAllEvent();
    List<EventDTO> getEventsByClassName(String className);
    List<EventDTO> getEventsByGradeName(String gradeName);
    List<EventDTO> getEventsBySchoolWide();
    List<EventDTO> getEventsByStudentAccount(Long id);
    Page<EventDTO> getListEventsPaginated(Pageable pageable, Authentication auth);
    Page<EventDTO> filterEventsPaginated(Pageable pageable, Authentication auth,
                                         LocalDate startAt, LocalDate endAt, String createdAt,
                                         String scopeName, String creatorName);
    Page<EventDTO> searchEventsPaginated(Pageable pageable, Authentication auth,
                                         String search);
    List<AccountDTO> getAllCreator();
}
