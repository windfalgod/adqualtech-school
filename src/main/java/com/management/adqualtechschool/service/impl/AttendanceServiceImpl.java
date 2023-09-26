package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.dto.AttendanceDTO;
import com.management.adqualtechschool.entity.Account;
import com.management.adqualtechschool.entity.Attendance;
import com.management.adqualtechschool.repository.AccountRepository;
import com.management.adqualtechschool.repository.AttendanceRepository;
import com.management.adqualtechschool.service.AttendanceService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<AttendanceDTO> getAllAttendanceFollowMonthAndYear(int month, int year, Authentication auth) {
        List<Attendance> attendanceList = attendanceRepository.findAllByMonthAndYearOrderByDateAsc(month, year);
        // check if current month and year is same with month and year get from input
        if (LocalDate.now().getYear() == year && LocalDate.now().getMonth().getValue() == month) {
            if (attendanceList.size() == 0) {
                initializeCurrentMonthCalendar(auth);
                attendanceList = attendanceRepository.findAllByMonthAndYearOrderByDateAsc(month, year);
            }
            List<AttendanceDTO> attendanceDTOCalendar = attendanceList.stream()
                    .map(attendance -> modelMapper.map(attendance, AttendanceDTO.class)).
                    collect(Collectors.toList());

            int startDayOfWeek = 1;
            int dayOfWeekOfStartDayOfMonth = attendanceDTOCalendar.get(0).getDayOfWeek();
            while (startDayOfWeek < dayOfWeekOfStartDayOfMonth) {
                attendanceDTOCalendar.add(0, new AttendanceDTO());
                startDayOfWeek++;
            }
            while (attendanceDTOCalendar.size() < 35) {
                attendanceDTOCalendar.add(new AttendanceDTO());
            }
            return attendanceDTOCalendar;

        }

        if (attendanceList.size() == 0) {
            return new ArrayList<>();
        }

        return attendanceList.stream()
                .map(attendance -> modelMapper.map(attendance, AttendanceDTO.class)).
                collect(Collectors.toList());

    }


    private void initializeCurrentMonthCalendar(Authentication auth) {
       int numDaysOfCurrentMonth = LocalDate.now().lengthOfMonth();
       Account account = accountRepository.findByUsername(auth.getName());
       List<Attendance> attendanceList = new ArrayList<>();

       for (int date = 1; date <= numDaysOfCurrentMonth; date++) {
           Attendance dateAttendance = new Attendance();
           dateAttendance.setAccount(account);
           dateAttendance.setYear(LocalDate.now().getYear());
           dateAttendance.setMonth(LocalDate.now().getMonthValue());
           dateAttendance.setDate(date);
           dateAttendance.setCreatedAt(LocalDateTime.now());
           dateAttendance.setUpdatedAt(LocalDateTime.now());
           LocalDate localDate = LocalDate.of(dateAttendance.getYear(), dateAttendance.getMonth(),date);
           dateAttendance.setDayOfWeek(localDate.getDayOfWeek().getValue());
           attendanceRepository.save(dateAttendance);
           attendanceList.add(dateAttendance);
       }
       account.setAttendanceList(attendanceList);
       accountRepository.save(account);
    }
 }
