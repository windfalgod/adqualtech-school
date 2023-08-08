package com.management.adqualtechschool.service;

import com.management.adqualtechschool.dto.ScopeDTO;
import java.util.List;

public interface ScopeService {
    List<ScopeDTO> getAllScope();
    ScopeDTO getScopeByTitle(String title);
    List<ScopeDTO> getAllGradeScope();
}
