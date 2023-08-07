package com.management.adqualtechschool.repository;

import com.management.adqualtechschool.entity.Scope;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScopeRepository extends JpaRepository<Scope, Long> {
    Scope findScopeByTitle(String title);
}
