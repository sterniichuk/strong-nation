package online.strongnation.security.service.implementation;

import lombok.RequiredArgsConstructor;
import online.strongnation.security.exception.IllegalUserException;
import online.strongnation.security.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUtils {
    private final UserValidator validator;

    protected void checkNewUser(UserDTO user) {
        if (user == null) {
            throw new IllegalUserException("Body is empty");
        }
        final String email = user.getEmail();
        final String password = user.getPassword();
        validator.checkEmail(email);
        validator.checkPassword(password);
    }

    protected void checkOldUser(UserDTO user) {
        if (user == null) {
            throw new IllegalUserException("Body is empty");
        }
        final String email = user.getEmail();
        if (email == null) {
            throw new IllegalUserException("Email is null");
        }
        final String password = user.getPassword();
        validator.checkPassword(password);
    }


    protected void checkNewEmail(String email) {
        try {
            validator.checkEmail(email);
        } catch (IllegalUserException ex) {
            throw new IllegalUserException("new email\n" + ex.getMessage());
        }
    }

    protected void checkOldEmail(String email) {
        if (email == null) {
            throw new IllegalUserException("Old email is null");
        }
    }


    protected void checkPassword(String password) {
        try {
            validator.checkPassword(password);
        } catch (IllegalUserException ex) {
            throw new IllegalUserException("new password error\n" + ex.getMessage());
        }
    }

    protected void checkRole(Role role) {
        if (role == null) {
            throw new IllegalUserException("Role can't be null");
        }
    }

    protected void checkEmailDTO(UpdateEmailDTO dto) {
        if (dto.email() == null) {
            throw new IllegalUserException("Email is null");
        }
        if (dto.newEmail() == null) {
            throw new IllegalUserException("New email is null");
        }
        if (dto.newEmail().equals(dto.email())) {
            throw new IllegalUserException("Old and new emails are the same");
        }
        checkNewEmail(dto.newEmail());
    }

    protected void checkPasswordDTO(UpdatePasswordDTO dto) {
        if (dto.password() == null) {
            throw new IllegalUserException("old password is null. Write value to 'password' field");
        }
        if (dto.newPassword() == null) {
            throw new IllegalUserException("new password is null");
        }
        if (dto.newPasswordChecking() == null) {
            throw new IllegalUserException("new password checking is null");
        }
        if (!dto.newPasswordChecking().equals(dto.newPassword())) {
            throw new IllegalUserException("newPassword and newPasswordChecking are not the same");
        }
        checkPassword(dto.newPassword());
    }

    protected void selfUpdateCheck(Authentication authentication, EmailOwner updateDTO) {
        if (updateDTO == null) {
            throw new IllegalUserException("Request is empty");
        }
        checkAuthentication(authentication);
        if (updateDTO.email() == null) {
            throw new IllegalUserException("Email is null");
        }
        if (!authentication.getName().equals(updateDTO.email())) {
            throw new IllegalUserException("This is not your email address. " +
                    "This request is only for updating the user's own data. " +
                    "For manipulations with other accounts, look for another request.");
        }
    }

    protected void checkAuthentication(Authentication authentication) {
        if (authentication == null || authentication.getName() == null
                || authentication.getAuthorities() == null) {
            throw new IllegalUserException("Authentication object is invalid. Custom exception");
        }
    }

    public void checkUpdateEmailDTO(UpdateEmailWithPasswordDTO dto) {
        notNull(dto.newEmail(), "New email");
        if (dto.newEmail().equals(dto.email())) {
            throw new IllegalUserException("Old and new emails are the same");
        }
        notNull(dto.password(), "Password");
        checkNewEmail(dto.newEmail());
    }

    protected <T> void notNull(T object, String objectName) {
        if (object == null){
            throw new IllegalUserException(objectName + " is null");
        }
    }
}
