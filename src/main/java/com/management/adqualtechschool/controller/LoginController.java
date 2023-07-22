package com.management.adqualtechschool.controller;

import com.management.adqualtechschool.dto.AccountCreationDTO;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Model model, Authentication auth) {
        model.addAttribute("accountCreationDTO", new AccountCreationDTO());
        if(auth == null || auth instanceof AnonymousAuthenticationToken){
            return "login";
        }
        if (auth.getAuthorities().iterator().next()
                .getAuthority().equals("ROLE_ADMIN")) {
            return "redirect:./admin";
        }
        return "redirect:./";
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/forbidden-page")
    public String forbiddenPage() {
        return "forbidden-page";
    }
}
