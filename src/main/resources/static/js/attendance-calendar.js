$(document).ready(function () {
    $("#monthInput").change(function () {
        $(".attendance-form").submit();
    })

    $("#yearInput").change(function () {
        $(".attendance-form").submit();
    });
});