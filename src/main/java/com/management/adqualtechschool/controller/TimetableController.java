package com.management.adqualtechschool.controller;

import com.management.adqualtechschool.dto.AccountDTO;
import com.management.adqualtechschool.dto.StudyRoomDTO;
import com.management.adqualtechschool.dto.TimetableRequireDTO;
import com.management.adqualtechschool.dto.RequireDTO;
import com.management.adqualtechschool.service.AccountService;
import com.management.adqualtechschool.service.StudyRoomService;
import com.management.adqualtechschool.service.TimetableRequireService;
import com.management.adqualtechschool.service.TimetableService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.STUDY_ROOM_LIST;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.TEACHER_LIST;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.TIMETABLE;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.TIMETABLE_REQUIRE_LIST;
import static com.management.adqualtechschool.common.Message.FAILED;
import static com.management.adqualtechschool.common.Message.TIMETABLE_ARRANGE_FAILED;

@Controller
@RequestMapping(value = "/timetable")
public class TimetableController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private StudyRoomService studyRoomService;

    @Autowired
    private TimetableRequireService timetableRequireService;

    @Autowired
    private TimetableService timetableService;

    @GetMapping("")
    public String showTimetablePage() {
        return "pages/timetable/view";
    }

    @GetMapping("/arrange")
    public String arrangeTimetable(Model model) {
        addDataAndRequireArrangeToModel(model);
        model.addAttribute(TIMETABLE, new TimetableRequireDTO());
        return "pages/timetable/arrange";
    }

    @PostMapping("/arranging")
    public String doArrangeTimetable(Model model,
                                     @Valid @ModelAttribute("timetable") TimetableRequireDTO timetable,
                                     BindingResult result, RedirectAttributes attr)  {
        if (result.hasErrors()) {
            addDataAndRequireArrangeToModel(model);
            attr.addFlashAttribute(FAILED, TIMETABLE_ARRANGE_FAILED);
            return "pages/timetable/arrange";
        }
        timetableService.scheduleTimetable();
        return "redirect:arrange";
    }

    private void addDataAndRequireArrangeToModel(Model model) {
        List<AccountDTO> teacherList = accountService.getAllTeacherAccount();
        model.addAttribute(TEACHER_LIST, teacherList);
        List<StudyRoomDTO> studyRoomList = studyRoomService.getAllStudyRoom();
        model.addAttribute(STUDY_ROOM_LIST, studyRoomList);
        List<RequireDTO> requireDTOList = timetableRequireService.getAllRequire();
        model.addAttribute(TIMETABLE_REQUIRE_LIST, requireDTOList);
    }
}
