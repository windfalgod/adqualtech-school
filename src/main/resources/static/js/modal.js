$(document).ready(function () {
    let teacherModel = $('#teacherModal');
    teacherModel.modal('show')
    teacherModel.on('hidden.bs.modal', function () {
        window.location.href = '/teachers';
    });

    let studentModel = $('#studentModel');
    studentModel.modal('show')
    studentModel.on('hidden.bs.modal', function () {
        window.location.href = '/student';
    });

    $('#copyBtnUsername').click(function () {
        copyTextToClipboard($('#usernameAccountInfo').val());
        $(this).html('<i class="fa-solid fa-clipboard-check"></i> Copied');
        resetCopyButton($(this));
    });

    $('#copyBtnPassword').click(function () {
        copyTextToClipboard($('#passwordAccountInfo').val());
        $(this).html('<i class="fa-solid fa-clipboard-check"></i> Copied');
        resetCopyButton($(this));
    });

    // Function to copy text to clipboard
    function copyTextToClipboard(text) {
        navigator.clipboard.writeText(text)
    }

    // Function to reset the copy button text
    function resetCopyButton(button) {
        setTimeout(function () {
            button.html('<i class="fa-solid fa-clipboard"></i>');
        }, 1000);
    }
});