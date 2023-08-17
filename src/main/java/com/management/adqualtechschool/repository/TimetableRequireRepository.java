package com.management.adqualtechschool.repository;

import com.management.adqualtechschool.entity.TimetableRequire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimetableRequireRepository extends JpaRepository<TimetableRequire, Long> {
}
