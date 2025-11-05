function validatePasswordStrength(password) {
    const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    return regex.test(password);
}

function validateAndSubmit() {
    const newPassword = document.getElementById('inputNewPassword').value;
    const confirmNewPassword = document.getElementById('inputConfirmNewPassword').value;
    const passwordMatchError = document.getElementById('password-match-error');
    const passwordStrengthError = document.getElementById('password-strength-error');
    const confirmNewPasswordField = document.getElementById('inputConfirmNewPassword');
    const newPasswordField = document.getElementById('inputNewPassword');

    let isValid = true;

    if (newPassword !== confirmNewPassword) {
        confirmNewPasswordField.classList.add('error-border');
        passwordMatchError.classList.remove('d-none');
        isValid = false;
    } else {
        confirmNewPasswordField.classList.remove('error-border');
        passwordMatchError.classList.add('d-none');
    }

    if (!validatePasswordStrength(newPassword)) {
        newPasswordField.classList.add('error-border');
        passwordStrengthError.classList.remove('d-none');
        isValid = false;
    } else {
        newPasswordField.classList.remove('error-border');
        passwordStrengthError.classList.add('d-none');
    }

    if (isValid) {
        const confirmModal = new bootstrap.Modal(document.getElementById('confirmChangePasswordModal'));
        confirmModal.show();
    }
}

function submitNewPassword() {
    alert('Contraseña actualizada exitosamente (funcionalidad no implementada).');
    window.location.href = 'login.html';
    const confirmModal = bootstrap.Modal.getInstance(document.getElementById('confirmChangePasswordModal'));
    confirmModal.hide();
}
