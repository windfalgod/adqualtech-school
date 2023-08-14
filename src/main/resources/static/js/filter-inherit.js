$(document).ready(function () {
    let gradeSelect = $('.gradeSelect')
    let classSelect = $(".classSelect")
    gradeSelect.change(function () {
        let selectedOption = $(this).find("option:selected");
        let selectedValue = selectedOption.val().replace("Khối", "Lớp");

        classSelect.find("option").each(function () {
            if ($(this).val().startsWith(selectedValue)) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    });

    classSelect.change(function () {
        let selectedOption = $(this).find("option:selected");
        let selectedValue = selectedOption.val().replace("Lớp", "Khối");
        let gradeName = selectedValue.substring(0, selectedValue.length - 1)

        gradeSelect.find("option").each(function () {
            if ($(this).val().startsWith(gradeName)) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    });
})