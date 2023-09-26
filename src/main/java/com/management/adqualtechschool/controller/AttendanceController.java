package com.management.adqualtechschool.controller;

import com.management.adqualtechschool.dto.AttendanceDTO;
import com.management.adqualtechschool.service.AttendanceService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.ATTENDANCE_CALENDAR;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.MONTH;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.YEAR;

@Controller
@RequestMapping("/school-attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("")
    public String showAttendancePage(Model model, Authentication auth) {
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        List<AttendanceDTO> attendanceCalendar = attendanceService.getAllAttendanceFollowMonthAndYear(month, year, auth);
        model.addAttribute(ATTENDANCE_CALENDAR, attendanceCalendar);
        model.addAttribute(MONTH, month);
        model.addAttribute(YEAR, year);
        return "/pages/attendance/attendance-page";
    }

    @GetMapping("/changeTime")
    public String changeTimeAttendance(@RequestParam("month") Integer month,
                                       @RequestParam("year") Integer year,
                                       Model model, Authentication auth) {
        List<AttendanceDTO> attendanceCalendar = attendanceService.getAllAttendanceFollowMonthAndYear(month, year, auth);
        model.addAttribute(ATTENDANCE_CALENDAR, attendanceCalendar);
        model.addAttribute(MONTH, month);
        model.addAttribute(YEAR, year);
        return "/pages/attendance/attendance-page";
    }
}
