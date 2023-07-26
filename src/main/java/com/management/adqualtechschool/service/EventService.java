package com.management.adqualtechschool.service;

import com.management.adqualtechschool.dto.EventDTO;
import java.util.List;

public interface EventService {
    EventDTO getEventById(Long id);
    List<EventDTO> getAllEvent();
    List<EventDTO> getEventsByClassName(String className);
    List<EventDTO> getEventsByGradeName(String gradeName);
    List<EventDTO> getEventsBySchoolWide();
    List<EventDTO> getEventsByStudentAccount(Long id);
}
