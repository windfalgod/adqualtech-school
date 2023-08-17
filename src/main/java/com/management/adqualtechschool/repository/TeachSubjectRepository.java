package com.management.adqualtechschool.repository;

import com.management.adqualtechschool.entity.TeachSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeachSubjectRepository extends JpaRepository<TeachSubject, Long> {
}
