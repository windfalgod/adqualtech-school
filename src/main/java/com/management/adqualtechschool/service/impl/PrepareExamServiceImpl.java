package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.common.Message;
import com.management.adqualtechschool.config.CustomExamDTOFilterChain;
import com.management.adqualtechschool.dto.PrepareExamDTO;
import com.management.adqualtechschool.entity.PrepareExam;
import com.management.adqualtechschool.repository.PrepareExamRepository;
import com.management.adqualtechschool.service.PrepareExamService;
import java.util.Collections;
import java.util.List;
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

@Service
public class PrepareExamServiceImpl implements PrepareExamService {

    @Autowired
    private PrepareExamRepository prepareExamRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<PrepareExamDTO> getAllPrepareExam() {
        List<PrepareExam> examList = prepareExamRepository.findAll();
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

    }

    @Override
    public Page<PrepareExamDTO> getListExamsPaginated(Pageable pageable) {
        List<PrepareExamDTO> examDTOList = getAllPrepareExam();
        return paginate(pageable, examDTOList);
    }

    @Override
    public Page<PrepareExamDTO> filterPrepareExam(Pageable pageable, String createdAt, String subjectName,
                                                  String scopeName, String creatorName) {
        List<PrepareExamDTO> examDTOList = getAllPrepareExam();
        examDTOList = filterEvents(examDTOList, createdAt, subjectName, scopeName, creatorName);
        return paginate(pageable, examDTOList);
    }

    @Override
    public Page<PrepareExamDTO> searchExamsPaginated(Pageable pageable, String search) {
        List<PrepareExamDTO> examDTOList = getAllPrepareExam();
        if (search.equals("")) {
            return paginate(pageable, examDTOList);
        }
        String searchString = search.toLowerCase().trim();
        examDTOList = examDTOList.stream()
                .filter(examDTO -> examDTO.getTitle().toLowerCase().contains(searchString)
                        || (examDTO.getCreator().getLastName()
                        + " "
                        + examDTO.getCreator().getFirstName())
                        .toLowerCase().contains(searchString))
                .collect(Collectors.toList());
        return paginate(pageable, examDTOList);
    }


    private List<PrepareExamDTO> filterEvents(List<PrepareExamDTO> examDTOList, String createdAt,
                                        String subjectName, String scopeName, String creatorName) {
        return new CustomExamDTOFilterChain(examDTOList)
                .filterBySubjectName(subjectName)
                .filterByCreatedAt(createdAt)
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
