package com.management.adqualtechschool.controller;

import com.management.adqualtechschool.dto.AccountDTO;
import com.management.adqualtechschool.dto.PrepareExamDTO;
import com.management.adqualtechschool.dto.ScopeDTO;
import com.management.adqualtechschool.dto.SubjectDTO;
import com.management.adqualtechschool.service.AccountService;
import com.management.adqualtechschool.service.PrepareExamService;
import com.management.adqualtechschool.service.ScopeService;
import com.management.adqualtechschool.service.SubjectService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.ACCOUNT_LIST;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.CURRENT_PAGE;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.LIST;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.PAGE_NUMBERS;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SCOPE_LIST;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SUBJECT_LIST;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.TYPE;

@Controller
@RequestMapping("/exam-library")
public class PrepareExamController {

    @Autowired
    private PrepareExamService prepareExamService;

    @Autowired
    private ScopeService scopeService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SubjectService subjectService;

    private final static int PAGE_SIZE = 30;

    @GetMapping("")
    public String showExamLibrary(Model model, Authentication auth,
                                  @RequestParam("page") Optional<Integer> page) {
        int currentPage = page.orElse(1);

        Page<PrepareExamDTO> examLibraryPage = prepareExamService
                .getListExamsPaginated(PageRequest.of(currentPage - 1, PAGE_SIZE), auth);

        model.addAttribute(CURRENT_PAGE, examLibraryPage);
        int totalPages = examLibraryPage.getTotalPages();

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute(PAGE_NUMBERS, pageNumbers);
        }

        List<ScopeDTO> scopeList = scopeService.getAllScope();
        List<AccountDTO> accountDTOList = accountService.getAllTeacherAdminAccount();
        List<SubjectDTO> subjectDTOList = subjectService.getAllSubject();
        model.addAttribute(TYPE,LIST);
        model.addAttribute(SCOPE_LIST, scopeList);
        model.addAttribute(ACCOUNT_LIST, accountDTOList);
        model.addAttribute(SUBJECT_LIST, subjectDTOList);
        return "pages/event/list";
    }


}
