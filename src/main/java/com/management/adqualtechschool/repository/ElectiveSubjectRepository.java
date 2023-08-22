package com.management.adqualtechschool.repository;

import com.management.adqualtechschool.entity.ElectiveSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectiveSubjectRepository extends JpaRepository<ElectiveSubject, Long> {
}
