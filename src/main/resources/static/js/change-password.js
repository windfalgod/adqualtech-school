// validate for input
$().ready(function() {
    $("#changePasswordForm").validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        rules: {
            "currentPassword": {
                required: true,
                minlength: 8,
                validatePassword: true
            },
            "newPassword": {
                required: true,
                minlength: 8,
                validatePassword: true
            },
            "confirmPassword": {
                required: true,
                minlength: 8,
                validatePassword: true,
                equalTo: '#new_password'
            }
        },
        messages: {
            "currentPassword": {
                required: "Mật khẩu hiện tại không được để trống!",
                minlength: "Mật khẩu hiện tại phải nhiều hơn 8 ký tự!"
            },
            "newPassword": {
                required: "Mật khẩu mới không được để trống!",
                minlength: "Mật khẩu mới phải nhiều hơn 8 ký tự!"
            },
            "confirmPassword": {
                required: "Mật khẩu xác nhận không được để trống!",
                minlength: "Mật khẩu xác nhận phải nhiều hơn 8 ký tự!",
                equalTo: "Mật khẩu xác nhận phải giống với mật khẩu mới!"
            }
        },
        highlight: function(element) {
            $(element).css("border-color", "red");
        },

        unhighlight: function(element) {
            $(element).css("border-color", "");
        },
    });
});
// validate password
jQuery.validator.addMethod("validatePassword", function (value, element) {
    return this.optional(element) || /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,32}$/.test(value);
}, "Mật khẩu phải chứa 1 chữ hoa, 1 chữ thường, 1 chữ số và 1 ký tự đặc biệt!");

// password toggle
const togglePassword1 = document.querySelector('#toggle-password1');
const togglePassword2 = document.querySelector('#toggle-password2');
const togglePassword3 = document.querySelector('#toggle-password3');
const password = document.querySelector('#current_password');
const newPassword = document.querySelector('#new_password');
const confirmPassword = document.querySelector('#confirm_password');

togglePassword1.addEventListener('click', function (e) {
    const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
    if (this.classList.contains("fa-eye")) {
        this.classList.replace("fa-eye", "fa-eye-slash")
    } else {
        this.classList.replace("fa-eye-slash" ,"fa-eye")
    }
    password.setAttribute('type', type);
});

togglePassword2.addEventListener('click', function (e) {
    const type = newPassword.getAttribute('type') === 'password' ? 'text' : 'password';
    if (this.classList.contains("fa-eye")) {
        this.classList.replace("fa-eye", "fa-eye-slash")
    } else {
        this.classList.replace("fa-eye-slash" ,"fa-eye")
    }
    newPassword.setAttribute('type', type);
});

togglePassword3.addEventListener('click', function (e) {
    const type = confirmPassword.getAttribute('type') === 'password' ? 'text' : 'password';
    if (this.classList.contains("fa-eye")) {
        this.classList.replace("fa-eye", "fa-eye-slash")
    } else {
        this.classList.replace("fa-eye-slash" ,"fa-eye")
    }
    confirmPassword.setAttribute('type', type);
});