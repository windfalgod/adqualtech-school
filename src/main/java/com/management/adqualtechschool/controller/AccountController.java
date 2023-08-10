package com.management.adqualtechschool.controller;

import com.management.adqualtechschool.common.Message;
import com.management.adqualtechschool.dto.AccountCreationDTO;
import com.management.adqualtechschool.service.AccountService;
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
    public String postChangePwd(@RequestParam("currentPassword") String currentPassword,
                                @RequestParam("newPassword") String newPassword,
                                @ModelAttribute("account") AccountCreationDTO account,
                                BindingResult result,
                                Authentication auth,
                                RedirectAttributes attr) {

        if (result.hasErrors()) {
            return "change-password";
        }
        try {
            String status = accountService.changePassword(account, currentPassword, newPassword);
            if (status.equals(Message.CHANGE_PWD_SUCCESS)) {
                attr.addFlashAttribute(Message.SUCCESS, Message.CHANGE_PWD_SUCCESS);
            } else {
                attr.addFlashAttribute(Message.FAILED, Message.PASSWORD_NOT_MATCH);
                return "redirect:/change-password";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            attr.addFlashAttribute(Message.FAILED, Message.CHANGE_PWD_FAILED);
            return "redirect:/change-password";
        }
        if (auth.getName().startsWith(MANAGER_USERNAME)) {
            return "redirect:/admin";
        }
        return "redirect:/";
    }
}
