package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.dto.AccountDTO;
import com.management.adqualtechschool.dto.NotifyDTO;
import com.management.adqualtechschool.entity.Notify;
import com.management.adqualtechschool.repository.NotifyRepository;
import com.management.adqualtechschool.service.AccountService;
import com.management.adqualtechschool.service.NotifyService;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.management.adqualtechschool.common.Message.NOT_FOUND_ACCOUNT_ID;

@Service
public class NotifyServiceImpl implements NotifyService {
    
    @Autowired
    private NotifyRepository notifyRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AccountService accountService;
    
    @Override
    public NotifyDTO getNotifyById(Long id) {
        Optional<Notify> Notify = notifyRepository.findById(id);
        return modelMapper.map(Notify, NotifyDTO.class);
    }

    @Override
    public List<NotifyDTO> getAllNotify() {
        List<Notify> notifyList = notifyRepository.findAll();
        return notifyList.stream()
                .map(Notify -> modelMapper.map(Notify, NotifyDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<NotifyDTO> getNotifiesByClassName(String className) {
        List<Notify> notifyList = notifyRepository.findNotifiesByClassNameOrderByCreatedAtDesc(className.toLowerCase());
        return notifyList.stream()
                .map(Notify -> modelMapper.map(Notify, NotifyDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<NotifyDTO> getNotifiesByGradeName(String gradeName) {
        List<Notify> notifyList = notifyRepository.findNotifiesByGradeNameOrderByCreatedAtDesc(gradeName.toLowerCase());
        return notifyList.stream()
                .map(Notify -> modelMapper.map(Notify, NotifyDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<NotifyDTO> getNotifiesBySchoolWide() {
        List<Notify> notifyList = notifyRepository.findNotifiesBySchoolWideOrderByCreatedAtDesc();
        return notifyList.stream()
                .map(Notify -> modelMapper.map(Notify, NotifyDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<NotifyDTO> getNotifiesByStudentAccount(Long id) {
        AccountDTO account = accountService.getAccountById(id);
        if (account == null) {
            throw new NoSuchElementException(NOT_FOUND_ACCOUNT_ID);
        }
        String nameClass = account.getClassRoom().getName();
        List<NotifyDTO> classNotifies = getNotifiesByClassName(nameClass);
        List<NotifyDTO> gradeNotifies = getNotifiesByGradeName(nameClass.substring(0, nameClass.length() - 1));
        List<NotifyDTO> schoolNotifies = getNotifiesBySchoolWide();
        classNotifies.addAll(gradeNotifies);
        classNotifies.addAll(schoolNotifies);
        classNotifies.sort(Comparator.comparing(NotifyDTO::getCreatedAt).reversed());
        return classNotifies;
    }
}
