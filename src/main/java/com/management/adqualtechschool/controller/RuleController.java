package com.management.adqualtechschool.controller;

import com.management.adqualtechschool.dto.AccountDTO;
import com.management.adqualtechschool.dto.EventDTO;
import com.management.adqualtechschool.dto.RuleDTO;
import com.management.adqualtechschool.dto.ScopeDTO;
import com.management.adqualtechschool.service.AccountService;
import com.management.adqualtechschool.service.RuleService;
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
import static com.management.adqualtechschool.common.Message.CREATE_RULE_FAILED;
import static com.management.adqualtechschool.common.Message.CREATE_RULE_SUCCESS;
import static com.management.adqualtechschool.common.Message.DELETE_RULE_FAILED;
import static com.management.adqualtechschool.common.Message.DELETE_RULE_SUCCESS;
import static com.management.adqualtechschool.common.Message.FAILED;
import static com.management.adqualtechschool.common.Message.SUCCESS;
import static com.management.adqualtechschool.common.Message.UPDATE_RULE_FAILED;
import static com.management.adqualtechschool.common.Message.UPDATE_RULE_SUCCESS;

@Controller
@RequestMapping(value = "/rules")
public class RuleController {

    @Autowired
    private RuleService ruleService;

    @Autowired
    private ScopeService scopeService;

    @Autowired
    private AccountService accountService;

    private final static String RULE = "rule";
    private final static int PAGE_SIZE = 30;

    @GetMapping("")
    public String showRulesPage(Model model, Authentication auth,
                                 @RequestParam("page") Optional<Integer> page) {
        int currentPage = page.orElse(1);

        Page<RuleDTO> ruleDTOPage = ruleService
                .getListRulesPaginated(PageRequest.of(currentPage - 1, PAGE_SIZE), auth);

        definedCurrentPageAndAddAttrToModel(model, ruleDTOPage);
        addAttrScopeListAndCreatorListToModel(model);
        model.addAttribute(TYPE,LIST);
        return "pages/rule/list";
    }

    @GetMapping("/filter")
    public String filterRules(Model model, Authentication auth,
                               @RequestParam("page") Optional<Integer> page,
                               @RequestParam(value = "startAt", required = false)
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startAt,
                               @RequestParam(value = "endAt", required = false)
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endAt,
                               @RequestParam(value = "createdAt", required = false) String createdAt,
                               @RequestParam(value = "scopeName", required = false) String scopeName,
                               @RequestParam(value = "creatorName", required = false) String creatorName) {
        int currentPage = page.orElse(1);

        Page<RuleDTO> ruleDTOPage = ruleService
                .filterRulesPaginated(PageRequest.of(currentPage - 1, PAGE_SIZE),
                        auth, startAt, endAt, createdAt, scopeName, creatorName);

        definedCurrentPageAndAddAttrToModel(model, ruleDTOPage);
        addAttrScopeListAndCreatorListToModel(model);
        model.addAttribute(TYPE,FILTER);
        model.addAttribute(START_AT, startAt);
        model.addAttribute(END_AT, endAt);
        model.addAttribute(CREATED_AT, createdAt);
        model.addAttribute(SCOPE_NAME,scopeName);
        model.addAttribute(CREATOR_NAME, creatorName);
        return "pages/rule/list";
    }

    @GetMapping("/search")
    public String searchRules(Model model, Authentication auth,
                               @RequestParam("page") Optional<Integer> page,
                               @RequestParam("search") String search) {
        int currentPage = page.orElse(1);
        Page<RuleDTO> ruleDTOPage = ruleService
                .searchRulesPaginated(PageRequest.of(currentPage - 1, PAGE_SIZE), auth, search);
        definedCurrentPageAndAddAttrToModel(model, ruleDTOPage);
        addAttrScopeListAndCreatorListToModel(model);

        model.addAttribute(TYPE, SEARCH);
        model.addAttribute(SEARCH, search);
        return "pages/rule/list";
    }

    @GetMapping("/{id}")
    public String detailRule(@PathVariable("id") Long id, Model model) {
        RuleDTO ruleDTO = ruleService.getRuleById(id);
        model.addAttribute(RULE, ruleDTO);
        return "pages/rule/detail";
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    public String createRule(Model model) {
        List<ScopeDTO> scopeList = scopeService.getAllScope();
        model.addAttribute(SCOPE_LIST, scopeList);
        model.addAttribute(RULE, new RuleDTO());
        return "pages/rule/create";
    }

    @PostMapping("/create-processing")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    public String postCreateRule(@Valid @ModelAttribute("rule") RuleDTO rule,
                                  BindingResult result, Authentication auth,
                                  Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            List<ScopeDTO> scopeList = scopeService.getAllScope();
            model.addAttribute(SCOPE_LIST, scopeList);
            return "pages/rule/create";
        }
        try {
            ruleService.saveOrUpdateRule(rule, auth.getName());
            attr.addFlashAttribute(SUCCESS, CREATE_RULE_SUCCESS);
        } catch (Exception ex) {
            ex.printStackTrace();
            attr.addFlashAttribute(FAILED, CREATE_RULE_FAILED);
            return "redirect:/rules/create";
        }
        return "redirect:/rules";
    }

    @GetMapping("/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    public String editRule(@RequestParam("id") Long id, Model model) {
        List<ScopeDTO> scopeList = scopeService.getAllScope();
        model.addAttribute(SCOPE_LIST, scopeList);
        RuleDTO ruleDTO = ruleService.getRuleById(id);
        model.addAttribute(RULE, ruleDTO);
        return "pages/rule/edit";
    }


    @PostMapping("/edit-processing")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    public String postEditRule(@Valid @ModelAttribute("rule") RuleDTO rule,
                                BindingResult result, Authentication auth,
                                Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            List<ScopeDTO> scopeList = scopeService.getAllScope();
            model.addAttribute(SCOPE_LIST, scopeList);
            return "pages/rule/edit";
        }
        try {
            ruleService.saveOrUpdateRule(rule, auth.getName());
            attr.addFlashAttribute(SUCCESS, UPDATE_RULE_SUCCESS);
        } catch (Exception ex) {
            ex.printStackTrace();
            attr.addFlashAttribute(FAILED, UPDATE_RULE_FAILED);
            attr.addAttribute("id", rule.getId());
            return "redirect:/rules/edit?id={id}";
        }
        return "redirect:/rules";
    }


    @PostMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    public String deleteRule(@RequestParam("id") Long id, RedirectAttributes attr) {
        try {
            ruleService.deleteById(id);
            attr.addFlashAttribute(SUCCESS, DELETE_RULE_SUCCESS);
        } catch (Exception ex) {
            ex.printStackTrace();
            attr.addFlashAttribute(FAILED, DELETE_RULE_FAILED);
        }
        return "redirect:/rules";
    }

    private void definedCurrentPageAndAddAttrToModel(Model model, Page<RuleDTO> ruleDTOPage) {
        model.addAttribute(CURRENT_PAGE, ruleDTOPage);
        int totalPages = ruleDTOPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute(PAGE_NUMBERS, pageNumbers);
        }
    }

    private void addAttrScopeListAndCreatorListToModel(Model model) {
        List<ScopeDTO> scopeList = scopeService.getAllScope();
        List<AccountDTO> accountDTOList = accountService.getAllTeacherAdminAccount();
        model.addAttribute(SCOPE_LIST, scopeList);
        model.addAttribute(ACCOUNT_LIST, accountDTOList);
    }
}
