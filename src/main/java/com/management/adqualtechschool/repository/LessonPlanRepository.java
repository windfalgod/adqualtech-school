package com.management.adqualtechschool.repository;

import com.management.adqualtechschool.entity.LessonPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonPlanRepository extends JpaRepository<LessonPlan, Long> {
}
