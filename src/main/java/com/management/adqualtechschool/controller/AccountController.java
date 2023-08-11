package com.management.adqualtechschool.controller;

import com.management.adqualtechschool.dto.AccountDTO;
import com.management.adqualtechschool.dto.SubjectDTO;
import com.management.adqualtechschool.service.AccountService;
import com.management.adqualtechschool.service.SubjectService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.management.adqualtechschool.common.Message;
import com.management.adqualtechschool.dto.AccountCreationDTO;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.CURRENT_PAGE;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.FILTER;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.LIST;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.PAGE_NUMBERS;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SEARCH;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.TYPE;
import static com.management.adqualtechschool.common.Message.DELETE_TEACHER_FAILED;
import static com.management.adqualtechschool.common.Message.DELETE_TEACHER_SUCCESS;
import static com.management.adqualtechschool.common.Message.FAILED;
import static com.management.adqualtechschool.common.Message.SUBJECT_LIST;
import static com.management.adqualtechschool.common.Message.SUBJECT_NAME;
import static com.management.adqualtechschool.common.Message.SUCCESS;
import static com.management.adqualtechschool.common.Message.UPGRADE_TEACHER_ROLE_FAILED;
import static com.management.adqualtechschool.common.Message.UPGRADE_TEACHER_ROLE_SUCCESS;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    private final static String ACCOUNT = "account";
    private final static String MANAGER_USERNAME = "ma";

    @Autowired
    private SubjectService subjectService;

    private static final int PAGE_SIZE = 30;

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
            if (status.equals(Message.CHANGE_SUCCESS)) {
                attr.addFlashAttribute(Message.SUCCESS, Message.CHANGE_SUCCESS);
            } else {
                attr.addFlashAttribute(Message.FAILED, Message.NOT_MATCH);
                return "redirect:/change-password";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            attr.addFlashAttribute(Message.FAILED, Message.CHANGE_FAILED);
            return "redirect:/change-password";
        }
        if (auth.getName().startsWith(MANAGER_USERNAME)) {
            return "redirect:/admin";
        }
        return "redirect:/";
    }

    @GetMapping("/teachers")
    public String showListTeacher(Model model, @RequestParam("page") Optional<Integer> page) {
        int currentPage = page.orElse(1);
        Page<AccountDTO> accountDTOPage = accountService
                .getListTeacherPaginated(PageRequest.of(currentPage - 1, PAGE_SIZE));

        definedCurrentPageAndAddAttrToModel(model, accountDTOPage);
        List<SubjectDTO> subjectDTOList = subjectService.getAllSubject();
        model.addAttribute(SUBJECT_LIST, subjectDTOList);
        model.addAttribute(TYPE,LIST);
        return "pages/teacher/list";
    }

    @GetMapping("/teachers/filter")
    public String filterEvents(Model model,
                               @RequestParam("page") Optional<Integer> page,
                               @RequestParam(value = "subjectName", required = false) String subjectName) {
        int currentPage = page.orElse(1);

        Page<AccountDTO> accountDTOPage = accountService.filterTeacherPaginatedBySubjectName
                (PageRequest.of(currentPage - 1, PAGE_SIZE), subjectName);

        definedCurrentPageAndAddAttrToModel(model, accountDTOPage);
        List<SubjectDTO> subjectDTOList = subjectService.getAllSubject();
        model.addAttribute(SUBJECT_LIST, subjectDTOList);
        model.addAttribute(SUBJECT_NAME, subjectName);
        model.addAttribute(TYPE,FILTER);
        return "pages/teacher/list";
    }

    @GetMapping("/teachers/search")
    public String searchEvents(Model model,
                               @RequestParam("page") Optional<Integer> page,
                               @RequestParam("search") String search) {
        int currentPage = page.orElse(1);
        Page<AccountDTO> accountDTOPage = accountService
                .searchTeachersPaginated(PageRequest.of(currentPage - 1, PAGE_SIZE), search);

        definedCurrentPageAndAddAttrToModel(model, accountDTOPage);
        model.addAttribute(TYPE, SEARCH);
        model.addAttribute(SEARCH, search);
        return "pages/teacher/list";
    }

    @PostMapping("/teachers/upgrading-role")
    public String upgradeRole(@RequestParam("id") Long id, RedirectAttributes attr) {
        try {
            accountService.upgradeTeacherRole(id);
            attr.addFlashAttribute(SUCCESS, UPGRADE_TEACHER_ROLE_SUCCESS);
        } catch (Exception ex) {
            ex.printStackTrace();
            attr.addFlashAttribute(FAILED, UPGRADE_TEACHER_ROLE_FAILED);
        }
        return "redirect:/teachers";
    }

    @PostMapping("/teachers/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    public String deleteTeacher(@RequestParam("id") Long id, RedirectAttributes attr) {
        try {
            accountService.deleteById(id);
            attr.addFlashAttribute(SUCCESS, DELETE_TEACHER_SUCCESS);
        } catch (Exception ex) {
            ex.printStackTrace();
            attr.addFlashAttribute(FAILED, DELETE_TEACHER_FAILED);
        }
        return "redirect:/teachers";
    }

    private void definedCurrentPageAndAddAttrToModel(Model model, Page<AccountDTO> teacherDTOPage) {
        model.addAttribute(CURRENT_PAGE, teacherDTOPage);
        int totalPages = teacherDTOPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute(PAGE_NUMBERS, pageNumbers);
        }
    }
}
