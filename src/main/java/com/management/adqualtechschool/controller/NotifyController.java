package com.management.adqualtechschool.controller;

import com.management.adqualtechschool.dto.AccountDTO;
import com.management.adqualtechschool.dto.NotifyDTO;
import com.management.adqualtechschool.dto.ScopeDTO;
import com.management.adqualtechschool.service.NotifyService;
import com.management.adqualtechschool.service.ScopeService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.FILTER;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.LIST;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.PAGE_NUMBERS;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SCOPE_LIST;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SCOPE_NAME;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SEARCH;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.TYPE;
import static com.management.adqualtechschool.common.Message.CREATE_NOTIFY_FAILED;
import static com.management.adqualtechschool.common.Message.CREATE_NOTIFY_SUCCESS;
import static com.management.adqualtechschool.common.Message.DELETE_NOTIFY_FAILED;
import static com.management.adqualtechschool.common.Message.DELETE_NOTIFY_SUCCESS;
import static com.management.adqualtechschool.common.Message.FAILED;
import static com.management.adqualtechschool.common.Message.SUCCESS;
import static com.management.adqualtechschool.common.Message.UPDATE_NOTIFY_FAILED;
import static com.management.adqualtechschool.common.Message.UPDATE_NOTIFY_SUCCESS;

@Controller
@RequestMapping(value = "/notifies")
public class NotifyController {

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private ScopeService scopeService;

    private final static String NOTIFY = "notify";
    private final static int PAGE_SIZE = 30;

    @GetMapping("")
    public String showNotifiesPage(Model model, Authentication auth,
                                   @RequestParam("page") Optional<Integer> page) {
        int currentPage = page.orElse(1);

        Page<NotifyDTO> notifyDTOPage = notifyService
                .getListNotifyPaginated(PageRequest.of(currentPage - 1, PAGE_SIZE), auth);

        definedCurrentPageAndAddAttrToModel(model, notifyDTOPage);
        addAttrScopeListAndCreatorListToModel(model);
        model.addAttribute(TYPE,LIST);
        return "pages/notify/list";
    }

    @GetMapping("/filter")
    public String filterNotifies(Model model, Authentication auth,
                               @RequestParam("page") Optional<Integer> page,
                               @RequestParam(value = "createdAt", required = false) String createdAt,
                               @RequestParam(value = "scopeName", required = false) String scopeName,
                               @RequestParam(value = "creatorName", required = false) String creatorName) {
        int currentPage = page.orElse(1);

        Page<NotifyDTO> notifyDTOPage = notifyService
                .filterNotifiesPaginated(PageRequest.of(currentPage - 1, PAGE_SIZE),
                        auth, createdAt, scopeName, creatorName);

        definedCurrentPageAndAddAttrToModel(model, notifyDTOPage);
        addAttrScopeListAndCreatorListToModel(model);
        model.addAttribute(TYPE,FILTER);
        model.addAttribute(CREATED_AT, createdAt);
        model.addAttribute(SCOPE_NAME,scopeName);
        model.addAttribute(CREATOR_NAME, creatorName);
        return "pages/notify/list";
    }

    @GetMapping("/search")
    public String searchNotifies(Model model, Authentication auth,
                               @RequestParam("page") Optional<Integer> page,
                               @RequestParam("search") String search) {
        int currentPage = page.orElse(1);
        Page<NotifyDTO> notifyDTOPage = notifyService
                .searchNotifiesPaginated(PageRequest.of(currentPage - 1, PAGE_SIZE), auth, search);

        definedCurrentPageAndAddAttrToModel(model, notifyDTOPage);
        addAttrScopeListAndCreatorListToModel(model);
        model.addAttribute(TYPE, SEARCH);
        model.addAttribute(SEARCH, search);
        return "pages/notify/list";
    }

    @GetMapping("/{id}")
    public String detailNotify(@PathVariable("id") Long id, Model model) {
        NotifyDTO NotifyDTO = notifyService.getNotifyById(id);
        model.addAttribute(NOTIFY, NotifyDTO);
        return "pages/notify/detail";
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_TEACHER')")
    public String createNotify(Model model) {
        List<ScopeDTO> scopeList = scopeService.getAllScope();
        model.addAttribute(SCOPE_LIST, scopeList);
        model.addAttribute(NOTIFY, new NotifyDTO());
        return "pages/notify/create";
    }

    @PostMapping("/create-processing")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_TEACHER')")
    public String postCreateNotify(@Valid @ModelAttribute("notify") NotifyDTO notify,
                                   BindingResult result, Authentication auth,
                                   Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            List<ScopeDTO> scopeList = scopeService.getAllScope();
            model.addAttribute(SCOPE_LIST, scopeList);
            return "pages/notify/create";
        }
        try {
            notifyService.saveOrUpdateNotify(notify, auth.getName());
            attr.addFlashAttribute(SUCCESS, CREATE_NOTIFY_SUCCESS);
        } catch (Exception ex) {
            ex.printStackTrace();
            attr.addFlashAttribute(FAILED, CREATE_NOTIFY_FAILED);
            return "redirect:/notifies/create";
        }
        return "redirect:/notifies";
    }

    @GetMapping("/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_TEACHER')")
    public String editNotify(@RequestParam("id") Long id, Model model) {
        List<ScopeDTO> scopeList = scopeService.getAllScope();
        model.addAttribute(SCOPE_LIST, scopeList);
        NotifyDTO NotifyDTO = notifyService.getNotifyById(id);
        model.addAttribute(NOTIFY, NotifyDTO);
        return "pages/notify/edit";
    }


    @PostMapping("/edit-processing")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_TEACHER')")
    public String postEditNotify(@Valid @ModelAttribute("notify") NotifyDTO notify,
                                 BindingResult result, Authentication auth,
                                 Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            List<ScopeDTO> scopeList = scopeService.getAllScope();
            model.addAttribute(SCOPE_LIST, scopeList);
            return "pages/notify/edit";
        }
        try {
            notifyService.saveOrUpdateNotify(notify, auth.getName());
            attr.addFlashAttribute(SUCCESS, UPDATE_NOTIFY_SUCCESS);
        } catch (Exception ex) {
            ex.printStackTrace();
            attr.addFlashAttribute(FAILED, UPDATE_NOTIFY_FAILED);
            attr.addAttribute("id", notify.getId());
            return "redirect:/notifies/edit?id={id}";
        }
        return "redirect:/notifies";
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_TEACHER')")
    public String deleteNotify(@RequestParam("id") Long id, RedirectAttributes attr) {
        try {
            notifyService.deleteById(id);
            attr.addFlashAttribute(SUCCESS, DELETE_NOTIFY_SUCCESS);
        } catch (Exception ex) {
            ex.printStackTrace();
            attr.addFlashAttribute(FAILED, DELETE_NOTIFY_FAILED);
        }
        return "redirect:/notifies";
    }

    private void definedCurrentPageAndAddAttrToModel(Model model, Page<NotifyDTO> notifyDTOPage) {
        model.addAttribute(CURRENT_PAGE, notifyDTOPage);
        int totalPages = notifyDTOPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute(PAGE_NUMBERS, pageNumbers);
        }
    }

    private void addAttrScopeListAndCreatorListToModel(Model model) {
        List<ScopeDTO> scopeList = scopeService.getAllScope();
        List<AccountDTO> accountDTOList = notifyService.getAllCreator();
        model.addAttribute(SCOPE_LIST, scopeList);
        model.addAttribute(ACCOUNT_LIST, accountDTOList);
    }
}
