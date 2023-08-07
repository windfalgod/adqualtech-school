package com.management.adqualtechschool.service;

import com.management.adqualtechschool.dto.EventDTO;
import com.management.adqualtechschool.dto.PrepareExamDTO;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface PrepareExamService {
    List<PrepareExamDTO> getAllPrepareExam();
    PrepareExamDTO getPrepareExamById(Long id);
    void deletePrepareExamById(Long id);
    void saveOrUpdatePrepareExam(PrepareExamDTO exam, String username, MultipartFile examUpload);
    Page<PrepareExamDTO> getListExamsPaginated(Pageable pageable, Authentication auth);
    List<PrepareExamDTO> filterPrepareExam(Pageable pageable, Authentication auth,
                                           LocalDate created_at, Long subjectId,
                                           String scopeName, String creatorName);
    Page<PrepareExamDTO> searchExamsPaginated(Pageable pageable, Authentication auth,
                                         String search);
}
