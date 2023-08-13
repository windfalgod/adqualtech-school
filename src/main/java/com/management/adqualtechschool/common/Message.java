package com.management.adqualtechschool.common;

public interface Message {
    String SUCCESS = "success";
    String FAILED = "failed";
    String EVENT_TITLE_NOT_EMPTY = "Tên tiêu đề sự kiện không được để trống!";
    String EVENT_CONTENT_NOT_EMPTY = "Nội dung sự kiện không được để trống!";
    String EVENT_IMAGE_NOT_EMPTY = "Ảnh cho sự kiện không được để trống!";
    String EVENT_START_AT_NOT_NULL = "Thời gian sự kiện diễn ra không được để trống!";
    String EVENT_END_AT_NOT_NULL = "Thời gian sự kiện kết thúc không được để trống!";
    String CREATE_EVENT_SUCCESS = "Thêm sự kiện thành công!";
    String CREATE_EVENT_FAILED = "Thêm sự kiện không thành công!";
    String UPDATE_EVENT_SUCCESS = "Cập nhật sự kiện thành công!";
    String UPDATE_EVENT_FAILED = "Cập nhật sự kiện không thành công!";
    String DELETE_EVENT_SUCCESS = "Xóa sự kiện thành công!";
    String DELETE_EVENT_FAILED = "Xóa sự kiện không thành công!";
    String START_BEFORE_END_EVENT = "Thời gian diễn ra luôn trước thời gian kết thúc sự kiện!";
    String CREATE_BEFORE_END_EVENT = "Thời gian tạo luôn trước thời gian kết thúc sự kiện!";
    String NOT_FOUND_ACCOUNT_ID = "Not found account by id!";
    String NOT_FOUND_ACCOUNT_USERNAME = "Not found account by username ";
    String NOT_FOUND_EVENT = "Not found event!";
    String NOT_FOUND_SCOPE = "Not found scope ";
    String NO_IMAGE = "Not found image name!";
    String CHANGE_SUCCESS = "Đổi mật khẩu thành công";
    String CHANGE_FAILED = "Đổi mật khẩu không thành công!";
    String NOT_NULL = "Mật khẩu không được trống";
    String REGEXP = "Mật khẩu phải chứa 1 chữ số, 1 chữ in hoa, 1 chữ thường, 1 ký tự đặc biệt";
    String NOT_MATCH = "Mật khẩu hiện tại không khớp!";
    String CREATE_NOTIFY_SUCCESS = "Thêm thông báo thành công!";
    String CREATE_NOTIFY_FAILED = "Thêm thông báo không thành công!";
    String UPDATE_NOTIFY_SUCCESS = "Cập nhật thông báo thành công!";
    String UPDATE_NOTIFY_FAILED = "Cập nhật thông báo không thành công!";
    String DELETE_NOTIFY_SUCCESS = "Xóa thông báo thành công!";
    String DELETE_NOTIFY_FAILED = "Xóa thông báo không thành công!";
    String NOT_FOUND_NOTIFY = "Not found notify!";
    String NOTIFY_TITLE_NOT_EMPTY = "Tên tiêu đề thông báo không được để trống!";
    String NOTIFY_CONTENT_NOT_EMPTY = "Nội dung thông báo không được để trống!";
    String RULE_TITLE_NOT_EMPTY = "Tên tiêu đề nội quy không được để trống!";
    String RULE_CONTENT_NOT_EMPTY = "Nội dung nội quy không được để trống!";
    String CREATE_RULE_SUCCESS = "Thêm nội quy thành công!";
    String CREATE_RULE_FAILED = "Thêm nội quy không thành công!";
    String UPDATE_RULE_SUCCESS = "Cập nhật nội quy thành công!";
    String UPDATE_RULE_FAILED = "Cập nhật nội quy không thành công!";
    String DELETE_RULE_SUCCESS= "Xóa nội quy thành công!";
    String DELETE_RULE_FAILED = "Xóa nội quy không thành công!";
    String START_BEFORE_END_RULE = "Thời gian bắt đầu luôn trước thời gian kết thúc nội quy!";
    String CREATE_BEFORE_START_RULE = "Thời gian tạo luôn trước thời gian bắt đầu nội quy!";
    String RULE_START_AT_NOT_NULL = "Thời gian nội quy có hiệu lực không được để trống!";
    String RULE_END_AT_NOT_NULL = "Thời gian nội quy hết hiệu lực không được để trống!";
    String NOT_FOUND_RULE= "Not found rule ";
    String CREATE_EXAM_SUCCESS = "Thêm đề thi thành công!";
    String CREATE_EXAM_FAILED = "Thêm đề thi không thành công!";
    String UPDATE_EXAM_SUCCESS = "Cập nhật đề thi thành công!";
    String UPDATE_EXAM_FAILED = "Cập nhật đề thi không thành công!";
    String DELETE_EXAM_SUCCESS = "Xóa đề thi thành công!";
    String DELETE_EXAM_FAILED = "Xóa đề thi không thành công!";
    String NOT_FOUND_EXAM = "Not found exam!";
    String NOT_EXAM_NAME = "Not found exam file name!";
    String EXAM_TITLE_NOT_EMPTY = "Tên tiêu đề đề thi không được để trống!";
    String EXAM_CONTENT_NOT_EMPTY = "Tệp đề thi không được để trống!";
    String CREATE_TEACHER_SUCCESS = "Thêm giáo viên thành công!";
    String CREATE_TEACHER_FAILED = "Thêm giáo viên không thành công!";
    String DELETE_TEACHER_SUCCESS = "Xóa giáo viên thành công!";
    String DELETE_TEACHER_FAILED = "Xóa giáo viên không thành công!";
    String UPGRADE_TEACHER_ROLE_SUCCESS = "Nâng quyền giáo viên thành công!";
    String UPGRADE_TEACHER_ROLE_FAILED = "Nâng quyền giáo viên không thành công!";
    String NOT_FOUND_ROLE_NAME= "Not found role name ";
    String SUBJECT_LIST = "subjectList";
    String SUBJECT_NAME = "subjectName";
    String NOT_CONTAIN_ROLE = "Set role not contains this role!";
    String TEACHER_PREFIX = "te";
    String TEACHER_NAME_NOT_NULL = "Tên giáo viên không được để trống!";
    String BIRTHDAY_NOT_NULL = "Ngày sinh không được để trống";
    String BIRTHDAY_NOT_VALID = "birthday not valid";
    String GENERAL_TEACHER_SUBJECT = "Môn dạy";
    String SEARCH_EMPTY = "";
}
