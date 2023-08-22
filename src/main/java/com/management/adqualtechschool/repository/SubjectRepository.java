package com.management.adqualtechschool.repository;

import com.management.adqualtechschool.entity.Subject;
import com.management.adqualtechschool.entity.TeachSubject;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Subject findByName(String name);
}
