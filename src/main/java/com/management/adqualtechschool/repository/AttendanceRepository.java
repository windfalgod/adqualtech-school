package com.management.adqualtechschool.repository;

import com.management.adqualtechschool.entity.Attendance;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findAllByMonthAndYearOrderByDateAsc(int month, int year);
}
