package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.common.Message;
import com.management.adqualtechschool.dto.AccountDTO;
import com.management.adqualtechschool.dto.EventDTO;
import com.management.adqualtechschool.config.CustomEventDTOFilterChain;
import com.management.adqualtechschool.dto.ScopeDTO;
import com.management.adqualtechschool.entity.Account;
import com.management.adqualtechschool.entity.Event;
import com.management.adqualtechschool.entity.Scope;
import com.management.adqualtechschool.repository.EventRepository;
import com.management.adqualtechschool.service.AccountService;
import com.management.adqualtechschool.service.EventService;
import com.management.adqualtechschool.service.ScopeService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
import org.springframework.web.multipart.MultipartFile;

import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.CLASS_NAME_DEFAULT;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.CREATED_AT;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.GRADE_NAME_DEFAULT;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SCHOOL_WIDE;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SCOPE;
import static com.management.adqualtechschool.common.Message.SEARCH_EMPTY;
import static com.management.adqualtechschool.common.RoleType.PUPIL_ROLE;
import static com.management.adqualtechschool.common.SaveFileDir.EVENT_IMAGE_DIR;
import static com.management.adqualtechschool.common.SaveFileDir.STATIC_DIR;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ScopeService scopeService;

    @Override
    public EventDTO getEventById(Long id) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isPresent()) {
            return modelMapper.map(event, EventDTO.class);
        }
        throw new EntityNotFoundException(Message.NOT_FOUND_EVENT);
    }

    @Override
    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public void saveOrUpdateEvent(EventDTO event, String username, MultipartFile imageUpload) {
        AccountDTO accountDTO = accountService.getAccountByUsername(username);
        event.setCreator(modelMapper.map(accountDTO, Account.class));

        ScopeDTO scopeDTO;
        if (event.getScope().getTitle().equals(SCOPE)) {
            scopeDTO = scopeService.getScopeByTitle(SCHOOL_WIDE);
        } else {
            scopeDTO = scopeService.getScopeByTitle(event.getScope().getTitle());
        }
        event.setScope(modelMapper.map(scopeDTO, Scope.class));

        if (event.getId() == null) {
            event.setCreatedAt(LocalDateTime.now());
        } else {
            eventRepository.findById(event.getId())
                    .ifPresent(eventSaved -> event.setCreatedAt(eventSaved.getCreatedAt()));
        }
        event.setUpdatedAt(LocalDateTime.now());

        if (event.getStartAt().isAfter(event.getEndAt())) {
            throw new DateTimeException(Message.START_BEFORE_END_EVENT);
        }

        if (event.getEndAt().isBefore(event.getCreatedAt())) {
            throw new DateTimeException(Message.CREATE_BEFORE_END_EVENT);
        }

        String imageName = String.valueOf(imageUpload.getOriginalFilename());
        if (imageName.equals("")) {
            throw new NoSuchElementException(Message.NO_IMAGE);
        }
        try {
            byte[] imageBytes = imageUpload.getBytes();
            if (!(EVENT_IMAGE_DIR + imageName).equals(event.getImage())) {
                Path imagePath = Path.of(STATIC_DIR + EVENT_IMAGE_DIR + imageName);
                Files.write(imagePath, imageBytes);
            }
            event.setImage(EVENT_IMAGE_DIR + imageName);
            eventRepository.save(modelMapper.map(event, Event.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        @Override
    public List<EventDTO> getAllEvent() {
        List<Event> eventList = eventRepository.findAll(Sort.by(Sort.Direction.DESC, CREATED_AT));
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
    public List<EventDTO> getEventsByPupilAccount(Long id) {
        AccountDTO account = accountService.getAccountById(id);
        if (account == null) {
            throw new NoSuchElementException(Message.NOT_FOUND_ACCOUNT_ID);
        }
        String nameClass = account.getClassRoom().getName();
        List<EventDTO> classEvents = getEventsByClassName(nameClass);
        String gradeName = nameClass.replace(CLASS_NAME_DEFAULT, GRADE_NAME_DEFAULT).substring(0, nameClass.length() - 1);
        List<EventDTO> gradeEvents = getEventsByGradeName(gradeName);
        List<EventDTO> schoolEvents = getEventsBySchoolWide();
        classEvents.addAll(gradeEvents);
        classEvents.addAll(schoolEvents);
        classEvents.sort(Comparator.comparing(EventDTO::getCreatedAt).reversed());
        return classEvents;
    }

    @Override
    public Page<EventDTO> getListEventsPaginated(Pageable pageable, Authentication auth) {
        List<EventDTO> eventDTOList = getEventsFollowAuth(auth);
        return paginate(pageable, eventDTOList);
    }

    public Page<EventDTO> filterEventsPaginated(Pageable pageable, Authentication auth,
                                                LocalDate startAt, LocalDate endAt, String createdAt,
                                                String scopeName, String creatorName) {
        List<EventDTO> eventDTOList = getEventsFollowAuth(auth);
        LocalDateTime startAtDateTime = null;
        LocalDateTime endAtDateTime = null;

        if (startAt != null) {
            startAtDateTime = startAt.atStartOfDay();
        }
        if (endAt != null) {
            endAtDateTime = endAt.atStartOfDay();
        }
        eventDTOList = filterEvents(eventDTOList, startAtDateTime, endAtDateTime,
                createdAt, scopeName, creatorName);
        return paginate(pageable, eventDTOList);
    }

    @Override
    public Page<EventDTO> searchEventsPaginated(Pageable pageable, Authentication auth, String search) {
        List<EventDTO> eventDTOList = getEventsFollowAuth(auth);
        if (search.equals(SEARCH_EMPTY)) {
            return paginate(pageable, eventDTOList);
        }
        String searchString = search.toLowerCase().trim();
        eventDTOList = eventDTOList.stream()
                .filter(eventDTO -> eventDTO.getTitle().toLowerCase().contains(searchString)
                        || (eventDTO.getCreator().getLastName()
                        + " "
                        + eventDTO.getCreator().getFirstName())
                        .toLowerCase().contains(searchString))
                .collect(Collectors.toList());
        return paginate(pageable, eventDTOList);
    }

    @Override
    public List<AccountDTO> getAllCreator() {
        List<Account> creatorList = eventRepository.findAllEventCreator();
        return creatorList.stream()
                .map(creator -> modelMapper.map(creator, AccountDTO.class))
                .collect(Collectors.toList());
    }

    private List<EventDTO> filterEvents(List<EventDTO> eventDTOList, LocalDateTime startAt,
                                        LocalDateTime endAt, String createdAt,
                                        String scopeName, String creatorName) {
        return new CustomEventDTOFilterChain(eventDTOList)
                .filterByStartAt(startAt)
                .filterByEndAt(endAt)
                .filterByCreatedAt(createdAt)
                .filterByScopeName(scopeName)
                .filterByCreatorName(creatorName)
                .getEventDTOList();
    }

    private List<EventDTO> getEventsFollowAuth(Authentication auth) {
        AccountDetailsImpl accountDetails = (AccountDetailsImpl) auth.getPrincipal();
        Long accountId = accountDetails.getId();
        String role = accountDetails.getAuthorities().iterator().next().getAuthority();

        List<EventDTO> eventDTOList;

        if (role.equals(PUPIL_ROLE)) {
            eventDTOList = getEventsByPupilAccount(accountId);
        } else {
            eventDTOList = getAllEvent();
        }
        return eventDTOList;
    }

    private Page<EventDTO> paginate(Pageable pageable, List<EventDTO> eventDTOList) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<EventDTO> eventDTOPage;

        if (eventDTOList.size() < startItem) {
            eventDTOPage = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, eventDTOList.size());
            eventDTOPage = eventDTOList.subList(startItem, toIndex);
        }
        return new PageImpl<>(eventDTOPage, PageRequest.of(currentPage, pageSize), eventDTOList.size());
    }
}
