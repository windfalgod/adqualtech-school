package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.common.Message;
import com.management.adqualtechschool.config.CustomNotifyDTOFilterChain;
import com.management.adqualtechschool.dto.AccountDTO;
import com.management.adqualtechschool.dto.NotifyDTO;
import com.management.adqualtechschool.dto.ScopeDTO;
import com.management.adqualtechschool.entity.Account;
import com.management.adqualtechschool.entity.Notify;
import com.management.adqualtechschool.entity.Scope;
import com.management.adqualtechschool.repository.NotifyRepository;
import com.management.adqualtechschool.service.AccountService;
import com.management.adqualtechschool.service.NotifyService;
import com.management.adqualtechschool.service.ScopeService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
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
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.CREATED_AT;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SCHOOL_WIDE;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SCOPE;
import static com.management.adqualtechschool.common.Message.NOT_FOUND_ACCOUNT_ID;
import static com.management.adqualtechschool.common.Message.SEARCH_EMPTY;
import static com.management.adqualtechschool.common.RoleType.PUPIL;

@Service
public class NotifyServiceImpl implements NotifyService {
    
    @Autowired
    private NotifyRepository notifyRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ScopeService scopeService;

    
    @Override
    public NotifyDTO getNotifyById(Long id) {
        Optional<Notify> notify = notifyRepository.findById(id);
        if (notify.isPresent()) {
            return modelMapper.map(notify, NotifyDTO.class);
        }
        throw new EntityNotFoundException(Message.NOT_FOUND_NOTIFY);
    }

    @Override
    public void deleteById(Long id) {
        notifyRepository.deleteById(id);
    }

    @Override
    public List<NotifyDTO> getAllNotify() {
        List<Notify> notifyList = notifyRepository.findAll(Sort.by(Sort.Direction.DESC, CREATED_AT));
        return notifyList.stream()
                .map(Notify -> modelMapper.map(Notify, NotifyDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void saveOrUpdateNotify(NotifyDTO notify, String username) {
        notify.setCreator(modelMapper
                .map(accountService.getAccountByUsername(username), Account.class));
        ScopeDTO scopeDTO;
        if (notify.getScope().getTitle().equals(SCOPE)) {
            scopeDTO = scopeService.getScopeByTitle(SCHOOL_WIDE);
        } else {
            scopeDTO = scopeService.getScopeByTitle(notify.getScope().getTitle());
        }
        notify.setScope(modelMapper.map(scopeDTO, Scope.class));

        if (notify.getId() == null) {
            notify.setCreatedAt(LocalDateTime.now());
        } else {
            notifyRepository.findById(notify.getId())
                    .ifPresent(notifySave -> notify.setCreatedAt(notifySave.getCreatedAt()));
        }
        notify.setUpdatedAt(LocalDateTime.now());
        notifyRepository.save(modelMapper.map(notify, Notify.class));
    }

    @Override
    public List<NotifyDTO> getNotifiesByClassName(String className) {
        List<Notify> notifyList = notifyRepository
                .findNotifiesByClassNameOrderByCreatedAtDesc(className.toLowerCase());
        return notifyList.stream()
                .map(Notify -> modelMapper.map(Notify, NotifyDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<NotifyDTO> getNotifiesByGradeName(String gradeName) {
        List<Notify> notifyList = notifyRepository
                .findNotifiesByGradeNameOrderByCreatedAtDesc(gradeName.toLowerCase());
        return notifyList.stream()
                .map(Notify -> modelMapper.map(Notify, NotifyDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<NotifyDTO> getNotifiesBySchoolWide() {
        List<Notify> notifyList = notifyRepository
                .findNotifiesBySchoolWideOrderByCreatedAtDesc();
        return notifyList.stream()
                .map(Notify -> modelMapper.map(Notify, NotifyDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<NotifyDTO> getNotifiesByPupilAccount(Long id) {
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

    @Override
    public Page<NotifyDTO> getListNotifyPaginated(Pageable pageable, Authentication auth) {
        List<NotifyDTO> notifyDTOList = getNotifiesFollowAuth(auth);
        return paginate(pageable, notifyDTOList);
    }

    @Override
    public Page<NotifyDTO> filterNotifiesPaginated(Pageable pageable, Authentication auth,
                                                   String createdAt, String scopeName, String creatorName) {
        List<NotifyDTO> notifyDTOList = getNotifiesFollowAuth(auth);
        notifyDTOList = filterNotifies(notifyDTOList,
                createdAt, scopeName, creatorName);
        return paginate(pageable, notifyDTOList);
    }

    @Override
    public Page<NotifyDTO> searchNotifiesPaginated(Pageable pageable, Authentication auth, String search) {
        List<NotifyDTO> notifyDTOList = getNotifiesFollowAuth(auth);
        if (search.equals(SEARCH_EMPTY)) {
            return paginate(pageable, notifyDTOList);
        }
        String searchString = search.toLowerCase().trim();
        notifyDTOList = notifyDTOList.stream()
                .filter(notifyDTO -> notifyDTO.getTitle().toLowerCase().contains(searchString)
                        || (notifyDTO.getCreator().getLastName()
                        + " "
                        + notifyDTO.getCreator().getFirstName())
                        .toLowerCase().contains(searchString))
                .collect(Collectors.toList());
        return paginate(pageable, notifyDTOList);
    }

    @Override
    public List<AccountDTO> getAllCreator() {
        List<Account> creatorList = notifyRepository.findAllNotifyCreator();
        return creatorList.stream()
                .map(creator -> modelMapper.map(creator, AccountDTO.class))
                .collect(Collectors.toList());
    }

    private List<NotifyDTO> filterNotifies(List<NotifyDTO> notifyDTOList, String createdAt,
                                        String scopeName, String creatorName) {
        return new CustomNotifyDTOFilterChain(notifyDTOList)
                .filterByCreatedAt(createdAt)
                .filterByScopeName(scopeName)
                .filterByCreatorName(creatorName)
                .getNotifyDTOList();
    }

    private List<NotifyDTO> getNotifiesFollowAuth(Authentication auth) {
        AccountDetailsImpl accountDetails = (AccountDetailsImpl) auth.getPrincipal();
        Long accountId = accountDetails.getId();
        String role = accountDetails.getAuthorities().iterator().next().getAuthority();

        List<NotifyDTO> notifyDTOList;

        if (role.equals(PUPIL)) {
            notifyDTOList = getNotifiesByPupilAccount(accountId);
        } else {
            notifyDTOList = getAllNotify();
        }
        return notifyDTOList;
    }

    private Page<NotifyDTO> paginate(Pageable pageable, List<NotifyDTO> notifyDTOList) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<NotifyDTO> notifyDTOPage;

        if (notifyDTOList.size() < startItem) {
            notifyDTOPage = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, notifyDTOList.size());
            notifyDTOPage = notifyDTOList.subList(startItem, toIndex);
        }
        return new PageImpl<>(notifyDTOPage, PageRequest.of(currentPage, pageSize), notifyDTOList.size());
    }
}
