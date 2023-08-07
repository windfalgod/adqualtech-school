package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.dto.PrepareExamDTO;
import com.management.adqualtechschool.service.PrepareExamService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PrepareExamServiceImpl implements PrepareExamService {

    @Override
    public List<PrepareExamDTO> getAllPrepareExam() {
        return null;
    }

    @Override
    public PrepareExamDTO getPrepareExamById(Long id) {
        return null;
    }

    @Override
    public void deletePrepareExamById(Long id) {

    }

    @Override
    public void saveOrUpdatePrepareExam(PrepareExamDTO exam, String username, MultipartFile examUpload) {

    }

    @Override
    public Page<PrepareExamDTO> getListExamsPaginated(Pageable pageable, Authentication auth) {
        return null;
    }

    @Override
    public List<PrepareExamDTO> filterPrepareExam(Pageable pageable, Authentication auth, LocalDate created_at,
                                                  Long subjectId, String scopeName, String creatorName) {
        return null;
    }

    @Override
    public Page<PrepareExamDTO> searchExamsPaginated(Pageable pageable, Authentication auth, String search) {
        return null;
    }
}
