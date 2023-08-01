package com.management.adqualtechschool.controller;

import com.management.adqualtechschool.common.Message;
import com.management.adqualtechschool.dto.AccountCreationDTO;
import com.management.adqualtechschool.service.AccountService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    private final static String ACCOUNT = "account";
    private final static String MANAGER_USERNAME = "ma";

    @GetMapping("/change-password")
    public String showChangePwdPage(Model model, Authentication auth) {
        AccountCreationDTO account = accountService.getAccountCreationByUsername(auth.getName());
        model.addAttribute(ACCOUNT, account);
        return "change-password";
    }

    @PostMapping("doing-change")
    public String postChangePwd(@RequestParam("passwordNew") String passwordNew,
                                @Valid @ModelAttribute("account") AccountCreationDTO account,
                                BindingResult result,
                                Authentication auth,
                                RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "change-password";
        }
        try {
            accountService.changePassword(account, passwordNew);
            attr.addFlashAttribute(Message.SUCCESS, Message.CHANGE_PWD_SUCCESS);
        } catch (Exception ex) {
            ex.printStackTrace();
            attr.addFlashAttribute(Message.FAILED, Message.CHANGE_PWD_FAILED);
        }
        if (auth.getName().startsWith(MANAGER_USERNAME)) {
            return "redirect:/admin";
        }
        return "redirect:/";
    }
}
