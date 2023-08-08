package com.management.adqualtechschool.repository;

import com.management.adqualtechschool.entity.Scope;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScopeRepository extends JpaRepository<Scope, Long> {
    Scope findScopeByTitle(String title);

    @Query(value = "select s from Scope s where s.title like 'Khối %'")
    List<Scope> findAllGrade();
}
