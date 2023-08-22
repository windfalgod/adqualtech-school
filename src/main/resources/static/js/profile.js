$(document).ready(function() {
    let profileFieldSet = $("#profileFieldSet")
    let confirmEdit = $("#confirmEdit")
    let confirmCancel = $("#confirmCancel")
    let editProfileBtn = $("#editProfileBtn")
    let changeImageBtn = $("#changeImageBtn")

    let genderSelect = $("#genderSelect")
    let genderSelectLabel = $("#genderSelectLabel")

    let profileGender = $("#profileGender")
    let profileGenderLabel = $("#profileGenderLabel")

    let address = $("#address")
    let citySelect = $("#citySelect")
    let districtSelect = $("#districtSelect")
    let wardSelect = $("#wardSelect")

    let classroomSelect = $("#classroomSelect")
    let classInChargedSelect = $("#classInChargedSelect")

    let addressLabel = $("#addressLabel")
    let citySelectLabel = $("#citySelectLabel")
    let districtSelectLabel = $("#districtSelectLabel")
    let wardSelectLabel = $("#wardSelectLabel")

    let classroomSelectLabel = $("#classroomSelectLabel")
    let classInChargedSelectLabel = $("#classInChargedSelectLabel")
    let profileAddress = $("#profileAddress")
    let profileAddressLabel = $("#profileAddressLabel")

    let profileClassroom = $("#profileClassroom")
    let profileClassroomLabel = $("#profileClassroomLabel")
    let profileClassInCharged = $("#profileClassInCharged")
    let profileClassInChargedLabel = $("#profileClassInChargedLabel")

    let subjectContainer = $("#subjectContainer")
    let hiddenSubjectContainer = $("#hiddenSubjectContainer")

    let profilePhone = $("#profilePhone")
    let profileEmail = $("#profileEmail")
    let profileRank = $("#profileRank")
    let profileLevel = $("#profileLevel")
    let uploadImgBtn = $("#uploadImgBtn")
    let avatarImage = $("#avatarImage")
    let anonymousImage = $("#anonymousImage")

    // click edit button
    editProfileBtn.click(function (event) {
        showAndHiddenContentProfile()
        event.preventDefault()
    })

    // if in url /profile/edit
    if (window.location.pathname.startsWith('/profile/edit')) {
        showAndHiddenContentProfile()
    }

    function showAndHiddenContentProfile() {
        profileFieldSet.prop("disabled", false);
        confirmEdit.prop("hidden", false)
        confirmCancel.prop("hidden", false)
        changeImageBtn.prop("hidden", false)
        genderSelect.prop("hidden", false)
        genderSelectLabel.prop("hidden", false)
        profileGender.hide()
        profileGenderLabel.hide()

        address.prop("hidden", false)
        addressLabel.prop("hidden", false)
        profileAddress.hide()
        profileAddressLabel.hide()
        citySelect.prop("hidden", false)
        citySelectLabel.prop("hidden", false)
        districtSelect.prop("hidden", false)
        districtSelectLabel.prop("hidden", false)
        wardSelect.prop("hidden", false)
        wardSelectLabel.prop("hidden", false)

        profileClassInCharged.hide()
        profileClassInChargedLabel.hide()
        classInChargedSelect.prop("hidden", false)
        classInChargedSelectLabel.prop("hidden", false)

        hiddenSubjectContainer.attr("id", "#")
        subjectContainer.attr("id", "hiddenSubjectContainer")

        profileClassroom.hide()
        profileClassroomLabel.hide()
        classroomSelect.prop("hidden", false)
        classroomSelectLabel.prop("hidden", false)
        editProfileBtn.hide()

        profilePhone.attr("placeholder", "Nhập số điện thoại")
        profileEmail.attr("placeholder", "Nhập email")
        profileRank.attr("placeholder", "Nhập cấp bậc")
        profileLevel.attr("placeholder", "Nhập trình độ")
        uploadImgBtn.prop("hidden", false)
    }

    // preview image before submit
    $('#formImage').on('change', function () {
        const input = this;
        const preview = $('#imagePreview')[0];

        if (input.files && input.files[0]) {
            const reader = new FileReader();

            reader.onload = function (e) {
                $(preview).attr('src', e.target.result).show();
            };
            avatarImage.hide()
            anonymousImage.hide()
            reader.readAsDataURL(input.files[0]);
        } else {
            $(preview).attr('src', '').hide();
            avatarImage.show()
            anonymousImage.show()
        }
    });

    let profileLastName = $("#profileLastName")
    let profileFirstName = $("#profileFirstName")

    function setWidthToValue(inputField) {
        inputField.css('width', (inputField.val().length + 1) + 'ch');
    }

    setWidthToValue(profileLastName);
    setWidthToValue(profileFirstName);
    profileLastName.on('input', function () {
        setWidthToValue(profileLastName)
    });
    profileFirstName.on('input', function () {
        setWidthToValue(profileFirstName)
    });

    $('select[multiple].active.subject-select').multiselect({
        columns: 3,
        placeholder: 'Chọn môn dạy',
        search: true,
        searchOptions: {
            'default': 'Môn dạy'
        },
        selectAll: true
    });

    let checkboxes = $(".option-checkbox");

    // Loop through checkboxes
    checkboxes.each(function() {
        let checkbox = $(this);
        let option = checkbox.closest('option');

        if (!checkbox.prop('checked')) {
            option.removeAttr("selected");
        }
    });

    // submit edit
    confirmEdit.click(function () {
        $("#confirmEdit").submit();
    })
});