package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.common.Message;
import com.management.adqualtechschool.config.CustomExamDTOFilterChain;
import com.management.adqualtechschool.dto.AccountDTO;
import com.management.adqualtechschool.dto.PrepareExamDTO;
import com.management.adqualtechschool.dto.ScopeDTO;
import com.management.adqualtechschool.dto.SubjectDTO;
import com.management.adqualtechschool.entity.Account;
import com.management.adqualtechschool.entity.PrepareExam;
import com.management.adqualtechschool.entity.Scope;
import com.management.adqualtechschool.entity.Subject;
import com.management.adqualtechschool.repository.PrepareExamRepository;
import com.management.adqualtechschool.service.AccountService;
import com.management.adqualtechschool.service.PrepareExamService;
import com.management.adqualtechschool.service.ScopeService;
import com.management.adqualtechschool.service.SubjectService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.management.adqualtechschool.common.Message.SEARCH_EMPTY;
import static com.management.adqualtechschool.common.SaveFileDir.EXAM_DIR;
import static com.management.adqualtechschool.common.SaveFileDir.STATIC_DIR;

@Service
public class PrepareExamServiceImpl implements PrepareExamService {

    @Autowired
    private PrepareExamRepository prepareExamRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ScopeService scopeService;

    @Autowired
    private SubjectService subjectService;

    @Override
    public List<PrepareExamDTO> getAllPrepareExam() {
        List<PrepareExam> examList = prepareExamRepository.findAllOrderByUpdatedAtDesc();
        return examList.stream()
                .map(prepareExam -> modelMapper.map(prepareExam,PrepareExamDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PrepareExamDTO getPrepareExamById(Long id) {
        Optional<PrepareExam> exam = prepareExamRepository.findById(id);
        if (exam.isPresent()) {
            return modelMapper.map(exam, PrepareExamDTO.class);
        }
        throw new EntityNotFoundException(Message.NOT_FOUND_EXAM);
    }

    @Override
    public void deletePrepareExamById(Long id) {
        prepareExamRepository.deleteById(id);
    }

    @Override
    public void saveOrUpdatePrepareExam(PrepareExamDTO exam, String username, MultipartFile examUpload) {
        AccountDTO accountDTO = accountService.getAccountByUsername(username);
        exam.setCreator(modelMapper.map(accountDTO, Account.class));

        ScopeDTO scopeDTO = scopeService.getScopeByTitle(exam.getScope().getTitle());
        exam.setScope(modelMapper.map(scopeDTO, Scope.class));

        SubjectDTO subjectDTO = subjectService.getSubjectByName(exam.getSubject().getName());
        exam.setSubject(modelMapper.map(subjectDTO, Subject.class));

        if (exam.getId() == null) {
            exam.setCreatedAt(LocalDateTime.now());
        } else {
            prepareExamRepository.findById(exam.getId())
                    .ifPresent(examSaved -> exam.setCreatedAt(examSaved.getCreatedAt()));
        }
        exam.setUpdatedAt(LocalDateTime.now());

        String examFileName = String.valueOf(examUpload.getOriginalFilename());
        if (examFileName.equals("")) {
            throw new NoSuchElementException(Message.NOT_EXAM_NAME);
        }
        try {
            byte[] imageBytes = examUpload.getBytes();
            if (!(EXAM_DIR + examFileName).equals(exam.getContent())) {
                Path imagePath = Paths.get(STATIC_DIR + EXAM_DIR , examFileName);
                Files.write(imagePath, imageBytes);
            }
            exam.setContent(EXAM_DIR + examFileName);
            prepareExamRepository.save(modelMapper.map(exam, PrepareExam.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<PrepareExamDTO> getListExamsPaginated(Pageable pageable) {
        List<PrepareExamDTO> examDTOList = getAllPrepareExam();
        return paginate(pageable, examDTOList);
    }

    @Override
    public Page<PrepareExamDTO> filterPrepareExam(Pageable pageable, String updatedAt, String subjectName,
                                                  String scopeName, String creatorName) {
        List<PrepareExamDTO> examDTOList = getAllPrepareExam();
        examDTOList = filterEvents(examDTOList, updatedAt, subjectName, scopeName, creatorName);
        return paginate(pageable, examDTOList);
    }

    @Override
    public Page<PrepareExamDTO> searchExamsPaginated(Pageable pageable, String search) {
        List<PrepareExam> examList = prepareExamRepository.searchByTitleOrCreatorNameOrderByUpdatedAtDesc(search);
        if (search.equals(SEARCH_EMPTY)) {
            return paginate(pageable, getAllPrepareExam());
        }
        List<PrepareExamDTO> examDTOList = examList.stream().map(exam -> modelMapper.map(exam, PrepareExamDTO.class))
                .collect(Collectors.toList());
        return paginate(pageable, examDTOList);
    }

    @Override
    public List<AccountDTO> getAllCreator() {
        List<Account> creatorList = prepareExamRepository.findAllExamCreator();
        return creatorList.stream()
                .map(creator -> modelMapper.map(creator, AccountDTO.class))
                .collect(Collectors.toList());
    }


    private List<PrepareExamDTO> filterEvents(List<PrepareExamDTO> examDTOList, String updatedAt,
                                        String subjectName, String scopeName, String creatorName) {
        return new CustomExamDTOFilterChain(examDTOList)
                .filterBySubjectName(subjectName)
                .filterByUpdatedAt(updatedAt)
                .filterByScopeName(scopeName)
                .filterByCreatorName(creatorName)
                .getExamDTOList();
    }

    private Page<PrepareExamDTO> paginate(Pageable pageable, List<PrepareExamDTO> examDTOList) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<PrepareExamDTO> examDTOPage;

        if (examDTOList.size() < startItem) {
            examDTOPage = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, examDTOList.size());
            examDTOPage = examDTOList.subList(startItem, toIndex);
        }
        return new PageImpl<>(examDTOPage, PageRequest.of(currentPage, pageSize), examDTOList.size());
    }
}
