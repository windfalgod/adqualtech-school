package com.management.adqualtechschool.service;

import com.management.adqualtechschool.dto.NotifyDTO;
import java.util.List;

public interface NotifyService {
    NotifyDTO getNotifyById(Long id);
    List<NotifyDTO> getAllNotify();
    List<NotifyDTO> getNotifiesByClassName(String className);
    List<NotifyDTO> getNotifiesByGradeName(String gradeName);
    List<NotifyDTO> getNotifiesBySchoolWide();
    List<NotifyDTO> getNotifiesByStudentAccount(Long id);
    List<NotifyDTO> sortNotifiesByCreatedDateRecent(List<NotifyDTO> notifyDTOList);
}
