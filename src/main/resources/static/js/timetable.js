$(function() {

	$('select[multiple].active.select-criteria').multiselect({
	  columns: 3,
	  placeholder: 'Yêu cầu',
	  search: true,
	  searchOptions: {
	      'default': 'Yêu cầu'
	  },
	  selectAll: true
	});

	$('select[multiple].active.select-teacher').multiselect({
		columns: 3,
		placeholder: 'Chọn giáo viên',
		search: true,
		searchOptions: {
			'default': 'Giáo viên'
		},
		selectAll: true
	});

	$('select[multiple].active.select-study-room').multiselect({
		columns: 3,
		placeholder: 'Chọn phòng học',
		search: true,
		searchOptions: {
			'default': 'Phòng học'
		},
		selectAll: true
	});

});