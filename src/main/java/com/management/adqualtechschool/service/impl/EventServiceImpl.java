package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.dto.EventDTO;
import com.management.adqualtechschool.entity.Account;
import com.management.adqualtechschool.entity.Event;
import com.management.adqualtechschool.repository.EventRepository;
import com.management.adqualtechschool.service.AccountService;
import com.management.adqualtechschool.service.EventService;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AccountService accountService;

    @Override
    public EventDTO getEventById(Long id) {
        Optional<Event> event = eventRepository.findById(id);
        return modelMapper.map(event, EventDTO.class);
    }

    @Override
    public List<EventDTO> getAllEvent() {
        List<Event> eventList = eventRepository.findAll();
        return eventList.stream()
                .map(event -> modelMapper.map(event, EventDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getEventsByClassName(String className) {
        List<Event> eventList = eventRepository.findEventsByClassNameOrderByCreatedAtDesc(className.toLowerCase());
        return eventList.stream()
                .map(event -> modelMapper.map(event, EventDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getEventsByGradeName(String gradeName) {
        List<Event> eventList = eventRepository.findEventsByGradeNameOrderByCreatedAtDesc(gradeName.toLowerCase());
        return eventList.stream()
                .map(event -> modelMapper.map(event, EventDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getEventsBySchoolWide() {
        List<Event> eventList = eventRepository.findEventsBySchoolWideOrderByCreatedAtDesc();
        return eventList.stream()
                .map(event -> modelMapper.map(event, EventDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getEventsByStudentAccount(Long id) {
        Optional<Account> account = accountService.getAccountById(id);
        if (!account.isPresent()) {
            throw new NoSuchElementException("Not found account by id");
        }
        String nameClass = account.get().getClassRoom().getName();
        List<EventDTO> classEvents = getEventsByClassName(nameClass);
        List<EventDTO> gradeEvents = getEventsByGradeName(nameClass.substring(0, nameClass.length() - 1));
        List<EventDTO> schoolEvents = getEventsBySchoolWide();
        classEvents.addAll(gradeEvents);
        classEvents.addAll(schoolEvents);
        classEvents.sort(Comparator.comparing(EventDTO::getCreatedAt).reversed());
        return classEvents;
    }
}
