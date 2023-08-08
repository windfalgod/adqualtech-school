package com.management.adqualtechschool.controller;

import com.management.adqualtechschool.dto.AccountDTO;
import com.management.adqualtechschool.dto.PrepareExamDTO;
import com.management.adqualtechschool.dto.ScopeDTO;
import com.management.adqualtechschool.dto.SubjectDTO;
import com.management.adqualtechschool.service.AccountService;
import com.management.adqualtechschool.service.PrepareExamService;
import com.management.adqualtechschool.service.ScopeService;
import com.management.adqualtechschool.service.SubjectService;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.ACCOUNT_LIST;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.CREATOR_NAME;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.CURRENT_PAGE;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.FILTER;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.LIST;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.PAGE_NUMBERS;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SCOPE_LIST;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SCOPE_NAME;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SEARCH;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SUBJECT_LIST;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SUBJECT_NAME;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.TYPE;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.UPDATED_AT;
import static com.management.adqualtechschool.common.Message.CREATE_EXAM_FAILED;
import static com.management.adqualtechschool.common.Message.CREATE_EXAM_SUCCESS;
import static com.management.adqualtechschool.common.Message.DELETE_EXAM_FAILED;
import static com.management.adqualtechschool.common.Message.DELETE_EXAM_SUCCESS;
import static com.management.adqualtechschool.common.Message.FAILED;
import static com.management.adqualtechschool.common.Message.SUCCESS;
import static com.management.adqualtechschool.common.Message.UPDATE_EXAM_FAILED;
import static com.management.adqualtechschool.common.Message.UPDATE_EXAM_SUCCESS;
import static com.management.adqualtechschool.common.SaveFileDir.STATIC_DIR;

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
    private final static String EXAM = "exam";

    @GetMapping("")
    public String showExamLibrary(Model model, @RequestParam("page") Optional<Integer> page) {
        int currentPage = page.orElse(1);

        Page<PrepareExamDTO> examLibraryPage = prepareExamService
                .getListExamsPaginated(PageRequest.of(currentPage - 1, PAGE_SIZE));

        model.addAttribute(CURRENT_PAGE, examLibraryPage);
        int totalPages = examLibraryPage.getTotalPages();

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute(PAGE_NUMBERS, pageNumbers);
        }

        addAttrScopeListAndSubjectList(model);
        model.addAttribute(TYPE,LIST);
        List<AccountDTO> accountDTOList = accountService.getAllTeacherAdminAccount();
        model.addAttribute(ACCOUNT_LIST, accountDTOList);
        return "pages/exam/list";
    }

    @GetMapping("/filter")
    public String filterExams(Model model,
                               @RequestParam("page") Optional<Integer> page,
                               @RequestParam(value = "updatedAt", required = false) String updatedAt,
                               @RequestParam(value = "subjectName", required = false) String subjectName,
                               @RequestParam(value = "scopeName", required = false) String scopeName,
                               @RequestParam(value = "creatorName", required = false) String creatorName) {
        int currentPage = page.orElse(1);

        Page<PrepareExamDTO> examDTOPage = prepareExamService
                .filterPrepareExam(PageRequest.of(currentPage - 1, PAGE_SIZE),
                        updatedAt, subjectName, scopeName, creatorName);
        model.addAttribute(CURRENT_PAGE, examDTOPage);

        int totalPages = examDTOPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute(PAGE_NUMBERS, pageNumbers);
        }

        List<AccountDTO> accountDTOList = accountService.getAllTeacherAdminAccount();
        addAttrScopeListAndSubjectList(model);
        model.addAttribute(TYPE,FILTER);
        model.addAttribute(ACCOUNT_LIST, accountDTOList);
        model.addAttribute(UPDATED_AT, updatedAt);
        model.addAttribute(SUBJECT_NAME, subjectName);
        model.addAttribute(SCOPE_NAME,scopeName);
        model.addAttribute(CREATOR_NAME, creatorName);
        return "pages/exam/list";
    }

    @GetMapping("/search")
    public String searchExams(Model model,
                               @RequestParam("page") Optional<Integer> page,
                               @RequestParam("search") String search) {
        int currentPage = page.orElse(1);
        Page<PrepareExamDTO> examDTOPage = prepareExamService
                .searchExamsPaginated(PageRequest.of(currentPage - 1, PAGE_SIZE), search);
        model.addAttribute(CURRENT_PAGE, examDTOPage);

        int totalPages = examDTOPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute(PAGE_NUMBERS, pageNumbers);
        }
        List<AccountDTO> accountDTOList = accountService.getAllTeacherAdminAccount();
        addAttrScopeListAndSubjectList(model);
        model.addAttribute(TYPE, SEARCH);
        model.addAttribute(SEARCH, search);
        model.addAttribute(ACCOUNT_LIST, accountDTOList);
        return "pages/exam/list";
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    public String createExam(Model model) {
        addAttrScopeListAndSubjectList(model);
        model.addAttribute(EXAM, new PrepareExamDTO());
        return "pages/exam/create";
    }

    @PostMapping("/create-processing")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    public String postCreateExam(@Valid @ModelAttribute("exam") PrepareExamDTO exam,
                                  BindingResult result, Authentication auth,
                                  Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            addAttrScopeListAndSubjectList(model);
            return "pages/exam/create";
        }
        MultipartFile fileUpload = exam.getMultipartFile();
        try {
            prepareExamService.saveOrUpdatePrepareExam(exam, auth.getName(), fileUpload);
            attr.addFlashAttribute(SUCCESS, CREATE_EXAM_SUCCESS);
        } catch (Exception ex) {
            ex.printStackTrace();
            attr.addFlashAttribute(FAILED, CREATE_EXAM_FAILED);
            return "redirect:/exam-library/create";
        }
        return "redirect:/exam-library";
    }

    @GetMapping("/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    public String editExam(@RequestParam("id") Long id, Model model) {
        addAttrScopeListAndSubjectList(model);
        PrepareExamDTO examDTO = prepareExamService.getPrepareExamById(id);
        model.addAttribute(EXAM, examDTO);
        return "pages/exam/edit";
    }


    @PostMapping("/edit-processing")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    public String postEditExam(@Valid @ModelAttribute("exam") PrepareExamDTO exam,
                                BindingResult result, Authentication auth,
                                Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            addAttrScopeListAndSubjectList(model);
            return "pages/exam/edit";
        }
        MultipartFile imageUpload = exam.getMultipartFile();
        try {
            prepareExamService.saveOrUpdatePrepareExam(exam, auth.getName(), imageUpload);
            attr.addFlashAttribute(SUCCESS, UPDATE_EXAM_SUCCESS);
        } catch (Exception ex) {
            ex.printStackTrace();
            attr.addFlashAttribute(FAILED, UPDATE_EXAM_FAILED);
            attr.addAttribute("id", exam.getId());
            return "redirect:/exam-library/edit?id={id}";
        }
        return "redirect:/exam-library";
    }


    @PostMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    public String deleteExam(@RequestParam("id") Long id, RedirectAttributes attr) {
        try {
            prepareExamService.deletePrepareExamById(id);
            attr.addFlashAttribute(SUCCESS, DELETE_EXAM_SUCCESS);
        } catch (Exception ex) {
            ex.printStackTrace();
            attr.addFlashAttribute(FAILED, DELETE_EXAM_FAILED);
        }
        return "redirect:/exam-library";
    }

    @GetMapping("/download/")
    public ResponseEntity<Resource> downloadFile(@Param(value="id") Long id) {
        PrepareExamDTO prepareExamDTO = prepareExamService.getPrepareExamById(id);
        File file = new File(STATIC_DIR + prepareExamDTO.getContent());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        Resource resource = new FileSystemResource(file);

        // Explicitly set the Content-Disposition header with the desired file name
        HttpHeaders headers = new HttpHeaders();
        String encodedFileName = UriUtils.encode(prepareExamDTO.getContent(), StandardCharsets.UTF_8);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }


    // add attribute scopeList and subjectList to model
    private void addAttrScopeListAndSubjectList(Model model) {
        List<ScopeDTO> scopeList = scopeService.getAllGradeScope();
        model.addAttribute(SCOPE_LIST, scopeList);
        List<SubjectDTO> subjectList = subjectService.getAllSubject();
        model.addAttribute(SUBJECT_LIST, subjectList);
    }

}
