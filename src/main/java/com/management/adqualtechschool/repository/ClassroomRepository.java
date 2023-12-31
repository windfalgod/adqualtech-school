package com.management.adqualtechschool.repository;

import com.management.adqualtechschool.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    Classroom findClassroomByName(String className);
}
