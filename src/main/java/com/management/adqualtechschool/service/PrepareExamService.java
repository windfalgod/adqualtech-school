package com.management.adqualtechschool.service;

import com.management.adqualtechschool.dto.AccountDTO;
import com.management.adqualtechschool.dto.PrepareExamDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface PrepareExamService {
    List<PrepareExamDTO> getAllPrepareExam();
    PrepareExamDTO getPrepareExamById(Long id);
    void deletePrepareExamById(Long id);
    void saveOrUpdatePrepareExam(PrepareExamDTO exam, String username, MultipartFile examUpload);
    Page<PrepareExamDTO> getListExamsPaginated(Pageable pageable);
    Page<PrepareExamDTO> filterPrepareExam(Pageable pageable, String createdAt, String subjectName,
                                           String scopeName, String creatorName);
    Page<PrepareExamDTO> searchExamsPaginated(Pageable pageable, String search);
    List<AccountDTO> getAllCreator();
}
