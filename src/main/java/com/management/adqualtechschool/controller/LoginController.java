package com.management.adqualtechschool.controller;

import com.management.adqualtechschool.dto.AccountCreationDTO;
import com.management.adqualtechschool.dto.EventDTO;
import com.management.adqualtechschool.service.EventService;
import com.management.adqualtechschool.service.impl.AccountDetailsImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.management.adqualtechschool.common.RoleType.ADMIN_ROLE;
import static com.management.adqualtechschool.common.RoleType.PUPIL_ROLE;

@Controller
public class LoginController {
    @Autowired
    private EventService eventService;

    private final static String EVENT_LIST = "eventList";

    @GetMapping("/login")
    public String login(Model model, Authentication auth) {
        model.addAttribute("accountCreationDTO", new AccountCreationDTO());
        if(auth == null || auth instanceof AnonymousAuthenticationToken){
            return "login";
        }
        if (auth.getAuthorities().iterator().next()
                .getAuthority().equals(ADMIN_ROLE)) {
            return "redirect:./admin";
        }
        return "redirect:./home";
    }

    @GetMapping("/")
    public String showPublicHomePage(Model model) {
        return "public-page";
    }

    @GetMapping("/home")
    public String showHomePage(Model model, Authentication auth) {
        AccountDetailsImpl accountDetails = (AccountDetailsImpl) auth.getPrincipal();
        List<EventDTO> eventDTOList;
        if (auth.getAuthorities().iterator().next()
                .getAuthority().equals(PUPIL_ROLE)) {
            eventDTOList = eventService.getEventsByPupilAccount(accountDetails.getId());
        } else {
            eventDTOList = eventService.getAllEvent();
        }
        if (eventDTOList.size() > 6) {
            eventDTOList = eventDTOList.subList(0,6);
        }
        model.addAttribute(EVENT_LIST, eventDTOList);
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
