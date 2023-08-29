package com.management.adqualtechschool.repository;

import com.management.adqualtechschool.entity.SubjectLessonPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectLessonPlanRepository extends JpaRepository<SubjectLessonPlan, Long> {
}
