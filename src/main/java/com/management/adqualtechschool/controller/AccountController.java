package com.management.adqualtechschool.controller;

import com.management.adqualtechschool.dto.AccountDTO;
import com.management.adqualtechschool.dto.ScopeDTO;
import com.management.adqualtechschool.service.AccountService;
import com.management.adqualtechschool.service.ScopeService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.ACCOUNT_LIST;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.CURRENT_PAGE;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.LIST;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.PAGE_NUMBERS;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SCOPE_LIST;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.TYPE;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ScopeService scopeService;

    private static final int PAGE_SIZE = 30;

    @GetMapping("/teachers")
    public String showListTeacher(Model model, @RequestParam("page") Optional<Integer> page) {
        int currentPage = page.orElse(1);
        Page<AccountDTO> accountDTOPage = accountService
                .getListTeacherPaginated(PageRequest.of(currentPage - 1, PAGE_SIZE));

        definedCurrentPageAndAddAttrToModel(model, accountDTOPage);
        addAttrScopeListAndCreatorListToModel(model);
        model.addAttribute(TYPE,LIST);
        return "pages/teacher/list";
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

    private void addAttrScopeListAndCreatorListToModel(Model model) {
        List<ScopeDTO> scopeList = scopeService.getAllScope();
        model.addAttribute(SCOPE_LIST, scopeList);
    }
}
