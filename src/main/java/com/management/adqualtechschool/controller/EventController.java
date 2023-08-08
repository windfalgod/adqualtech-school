package com.management.adqualtechschool.controller;

import com.management.adqualtechschool.dto.AccountDTO;
import com.management.adqualtechschool.dto.EventDTO;
import com.management.adqualtechschool.dto.ScopeDTO;
import com.management.adqualtechschool.service.AccountService;
import com.management.adqualtechschool.service.EventService;
import com.management.adqualtechschool.service.ScopeService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.ACCOUNT_LIST;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.CREATED_AT;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.CREATOR_NAME;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.CURRENT_PAGE;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.END_AT;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.FILTER;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.LIST;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.PAGE_NUMBERS;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SCOPE_LIST;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SCOPE_NAME;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SEARCH;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.START_AT;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.TYPE;
import static com.management.adqualtechschool.common.Message.CREATE_EVENT_FAILED;
import static com.management.adqualtechschool.common.Message.CREATE_EVENT_SUCCESS;
import static com.management.adqualtechschool.common.Message.DELETE_EVENT_FAILED;
import static com.management.adqualtechschool.common.Message.DELETE_EVENT_SUCCESS;
import static com.management.adqualtechschool.common.Message.FAILED;
import static com.management.adqualtechschool.common.Message.SUCCESS;
import static com.management.adqualtechschool.common.Message.UPDATE_EVENT_FAILED;
import static com.management.adqualtechschool.common.Message.UPDATE_EVENT_SUCCESS;

@Controller
@RequestMapping(value = "/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ScopeService scopeService;

    @Autowired
    private AccountService accountService;

    private final static String EVENT = "event";
    private final static int PAGE_SIZE = 12;

    @GetMapping("")
    public String showEventsPage(Model model, Authentication auth,
                                 @RequestParam("page") Optional<Integer> page) {
            int currentPage = page.orElse(1);

            Page<EventDTO> eventDTOPage = eventService
                    .getListEventsPaginated(PageRequest.of(currentPage - 1, PAGE_SIZE), auth);

            model.addAttribute(CURRENT_PAGE, eventDTOPage);
            int totalPages = eventDTOPage.getTotalPages();

            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute(PAGE_NUMBERS, pageNumbers);
            }

            List<ScopeDTO> scopeList = scopeService.getAllScope();
            List<AccountDTO> accountDTOList = accountService.getAllTeacherAdminAccount();
            model.addAttribute(TYPE,LIST);
            model.addAttribute(SCOPE_LIST, scopeList);
            model.addAttribute(ACCOUNT_LIST, accountDTOList);
        return "pages/event/list";
    }

    @GetMapping("/filter")
    public String filterEvents(Model model, Authentication auth,
                               @RequestParam("page") Optional<Integer> page,
                               @RequestParam(value = "startAt", required = false)
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startAt,
                               @RequestParam(value = "endAt", required = false)
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endAt,
                               @RequestParam(value = "createdAt", required = false) String createdAt,
                               @RequestParam(value = "scopeName", required = false) String scopeName,
                               @RequestParam(value = "creatorName", required = false) String creatorName) {
        int currentPage = page.orElse(1);

        Page<EventDTO> eventDTOPage = eventService
                .filterEventsPaginated(PageRequest.of(currentPage - 1, PAGE_SIZE),
                        auth, startAt, endAt, createdAt, scopeName, creatorName);
        model.addAttribute(CURRENT_PAGE, eventDTOPage);

        int totalPages = eventDTOPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute(PAGE_NUMBERS, pageNumbers);
        }

        List<ScopeDTO> scopeList = scopeService.getAllScope();
        List<AccountDTO> accountDTOList = accountService.getAllTeacherAdminAccount();
        model.addAttribute(TYPE,FILTER);
        model.addAttribute(SCOPE_LIST, scopeList);
        model.addAttribute(ACCOUNT_LIST, accountDTOList);
        model.addAttribute(START_AT, startAt);
        model.addAttribute(END_AT, endAt);
        model.addAttribute(CREATED_AT, createdAt);
        model.addAttribute(SCOPE_NAME,scopeName);
        model.addAttribute(CREATOR_NAME, creatorName);
        return "pages/event/list";
    }

    @GetMapping("/search")
    public String searchEvents(Model model, Authentication auth,
                               @RequestParam("page") Optional<Integer> page,
                               @RequestParam("search") String search) {
        int currentPage = page.orElse(1);
        Page<EventDTO> eventDTOPage = eventService
                .searchEventsPaginated(PageRequest.of(currentPage - 1, PAGE_SIZE), auth, search);
        model.addAttribute(CURRENT_PAGE, eventDTOPage);

        int totalPages = eventDTOPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute(PAGE_NUMBERS, pageNumbers);
        }
        List<ScopeDTO> scopeList = scopeService.getAllScope();
        List<AccountDTO> accountDTOList = accountService.getAllTeacherAdminAccount();
        model.addAttribute(TYPE, SEARCH);
        model.addAttribute(SEARCH, search);
        model.addAttribute(SCOPE_LIST, scopeList);
        model.addAttribute(ACCOUNT_LIST, accountDTOList);
        return "pages/event/list";
    }

    @GetMapping("/{id}")
    public String detailEvent(@PathVariable("id") Long id,Model model) {
        EventDTO eventDTO = eventService.getEventById(id);
        model.addAttribute(EVENT, eventDTO);
        return "pages/event/detail";
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    public String createEvent(Model model) {
        List<ScopeDTO> scopeList = scopeService.getAllScope();
        model.addAttribute(SCOPE_LIST, scopeList);
        model.addAttribute(EVENT, new EventDTO());
        return "pages/event/create";
    }

    @PostMapping("/create-processing")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    public String postCreateEvent(@Valid @ModelAttribute("event") EventDTO event,
                                  BindingResult result, Authentication auth,
                                  Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            List<ScopeDTO> scopeList = scopeService.getAllScope();
            model.addAttribute(SCOPE_LIST, scopeList);
            return "pages/event/create";
        }
        MultipartFile imageUpload = event.getMultipartFile();
        try {
            eventService.saveOrUpdateEvent(event, auth.getName(), imageUpload);
            attr.addFlashAttribute(SUCCESS, CREATE_EVENT_SUCCESS);
        } catch (Exception ex) {
            ex.printStackTrace();
            attr.addFlashAttribute(FAILED, CREATE_EVENT_FAILED);
            return "redirect:/events/create";
        }
        return "redirect:/events";
    }

    @GetMapping("/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    public String editEvent(@RequestParam("id") Long id, Model model) {
        List<ScopeDTO> scopeList = scopeService.getAllScope();
        model.addAttribute(SCOPE_LIST, scopeList);
        EventDTO eventDTO = eventService.getEventById(id);
        model.addAttribute(EVENT, eventDTO);
        return "pages/event/edit";
    }


    @PostMapping("/edit-processing")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    public String postEditEvent(@Valid @ModelAttribute("event") EventDTO event,
                                BindingResult result, Authentication auth,
                                Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            List<ScopeDTO> scopeList = scopeService.getAllScope();
            model.addAttribute(SCOPE_LIST, scopeList);
            return "pages/event/edit";
        }
        MultipartFile imageUpload = event.getMultipartFile();
        try {
            eventService.saveOrUpdateEvent(event, auth.getName(), imageUpload);
            attr.addFlashAttribute(SUCCESS, UPDATE_EVENT_SUCCESS);
        } catch (Exception ex) {
            ex.printStackTrace();
            attr.addFlashAttribute(FAILED, UPDATE_EVENT_FAILED);
            attr.addAttribute("id", event.getId());
            return "redirect:/events/edit?id={id}";
        }
        return "redirect:/events";
    }


    @PostMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    public String deleteEvent(@RequestParam("id") Long id, RedirectAttributes attr) {
        try {
            eventService.deleteById(id);
            attr.addFlashAttribute(SUCCESS, DELETE_EVENT_SUCCESS);
        } catch (Exception ex) {
            ex.printStackTrace();
            attr.addFlashAttribute(FAILED, DELETE_EVENT_FAILED);
        }
        return "redirect:/events";
    }
}
