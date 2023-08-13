package com.management.adqualtechschool.service;

import com.management.adqualtechschool.dto.AccountDTO;
import com.management.adqualtechschool.dto.NotifyDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface NotifyService {
    NotifyDTO getNotifyById(Long id);
    void deleteById(Long id);
    List<NotifyDTO> getAllNotify();
    void saveOrUpdateNotify(NotifyDTO notify, String username);
    List<NotifyDTO> getNotifiesByClassName(String className);
    List<NotifyDTO> getNotifiesByGradeName(String gradeName);
    List<NotifyDTO> getNotifiesBySchoolWide();
    List<NotifyDTO> getNotifiesByPupilAccount(Long id);
    Page<NotifyDTO> getListNotifyPaginated(Pageable pageable, Authentication auth);
    Page<NotifyDTO> filterNotifiesPaginated(Pageable pageable, Authentication auth, String createdAt,
                                         String scopeName, String creatorName);
    Page<NotifyDTO> searchNotifiesPaginated(Pageable pageable, Authentication auth,
                                           String search);
    List<AccountDTO> getAllCreator();
}
