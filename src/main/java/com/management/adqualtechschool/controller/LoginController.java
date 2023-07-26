package com.management.adqualtechschool.controller;

import com.management.adqualtechschool.dto.AccountCreationDTO;
import com.management.adqualtechschool.dto.EventDTO;
import com.management.adqualtechschool.dto.NotifyDTO;
import com.management.adqualtechschool.service.AccountService;
import com.management.adqualtechschool.service.EventService;
import com.management.adqualtechschool.service.NotifyService;
import com.management.adqualtechschool.service.impl.AccountDetailsImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private final static String ROLE_ADMIN = "ROLE_ADMIN";
    private final static String ROLE_STUDENT = "ROLE_STUDENT";

    @Autowired
    private AccountService accountService;

    @Autowired
    private EventService eventService;

    @Autowired
    private NotifyService notifyService;

    @GetMapping("/login")
    public String login(Model model, Authentication auth) {
        model.addAttribute("accountCreationDTO", new AccountCreationDTO());
        if(auth == null || auth instanceof AnonymousAuthenticationToken){
            return "login";
        }
        if (auth.getAuthorities().iterator().next()
                .getAuthority().equals(ROLE_ADMIN)) {
            return "redirect:./admin";
        }
        return "redirect:./";
    }

    @GetMapping("/")
    public String showHomePage(Model model, Authentication auth) {
        AccountDetailsImpl accountDetails = (AccountDetailsImpl) auth.getPrincipal();
        List<EventDTO> eventDTOList;
        List<NotifyDTO> notifyDTOList;
        if (auth.getAuthorities().iterator().next()
                .getAuthority().equals(ROLE_STUDENT)) {
             eventDTOList = eventService.getEventsByStudentAccount(accountDetails.getId());
             notifyDTOList = notifyService.getNotifiesByStudentAccount(accountDetails.getId());
        } else {
            eventDTOList = eventService.getAllEvent();
            notifyDTOList = notifyService.getAllNotify();
        }
        model.addAttribute("eventList", eventDTOList);
        model.addAttribute("notifyList", notifyDTOList);
        return "pages/home";
    }

    @GetMapping("/admin")
    public String showAdminPage() {
        return "admin";
    }

    @GetMapping("/forbidden-page")
    public String showForbiddenPage() {
        return "forbidden-page";
    }
}
