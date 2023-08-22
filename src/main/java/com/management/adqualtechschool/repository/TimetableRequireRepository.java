package com.management.adqualtechschool.repository;

import com.management.adqualtechschool.entity.Require;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimetableRequireRepository extends JpaRepository<Require, Long> {
}
