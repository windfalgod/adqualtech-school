package com.management.adqualtechschool.service;

import com.management.adqualtechschool.dto.AttendanceDTO;
import java.util.List;
import org.springframework.security.core.Authentication;

public interface AttendanceService {
    List<AttendanceDTO> getAllAttendanceFollowMonthAndYear(int month, int year, Authentication auth);
}
