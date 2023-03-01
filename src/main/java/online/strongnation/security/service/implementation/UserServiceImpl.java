package online.strongnation.security.service.implementation;

import lombok.RequiredArgsConstructor;
import online.strongnation.security.exception.IllegalUserException;
import online.strongnation.security.exception.UserNotFoundException;
import online.strongnation.security.model.*;
import online.strongnation.security.repository.UserRepository;
import online.strongnation.security.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static online.strongnation.security.model.ApplicationUserPermission.UPDATE_SELF;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserValidator validator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<String> getEmails(Authentication authentication, Role role) {
        if (role == null) {
            throw new IllegalUserException("Role can't be null");
        }
        checkAuthentication(authentication);
        String email = authentication.getName();
        User reader = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("There is no user with email: " + email));
        checkPermission(reader, reader.getRole().getReadPermission());
        return repository.getEmailsByRole(role);
    }

    @Override
    @Transactional
    public String changePassword(Authentication authentication, UpdatePasswordDTO dto) {
        defaultUpdateCheck(authentication, dto);
        checkPasswordDTO(dto);
        String email = dto.email();
        User user = getUser(authentication, email);
        updatePassword(user, dto.newPassword());
        return "Password of " + email + " is updated";
    }

    private void checkPermission(User user, ApplicationUserPermission permission) {
        if (!user.getRole().hasPermission(permission)) {
            throw new IllegalUserException("User " + user.getEmail() + " has no " + permission.getPermission() + " permission");
        }
    }

    private void updatePassword(User user, String newPassword) {
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        repository.save(user);
    }

    @Override
    @Transactional
    public String changeEmail(Authentication authentication, UpdateEmailDTO dto) {
        defaultUpdateCheck(authentication, dto);
        checkEmailDTO(dto);
        String email = dto.email();
        User user = getUser(authentication, email);
        updateEmail(user, dto.newEmail());
        return "Email of " + email + " is updated to " + dto.newEmail();
    }

    private User getUser(Authentication authentication, String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("There is no user with email: " + email));
        String updaterEmail = authentication.getName();
        if (email.equals(updaterEmail)) {
            checkPermission(user, UPDATE_SELF);
        } else {
            User updater = repository.findByEmail(updaterEmail)
                    .orElseThrow(() -> new UserNotFoundException("There is no updater with email: " + updaterEmail));
            checkPermission(updater, user.getRole().getUpdatePermission());
        }
        return user;
    }

    private void updateEmail(User user, String newEmail) {
        repository.findByEmail(newEmail).ifPresent(x -> {
            throw new IllegalUserException(newEmail + " is already taken");
        });
        user.setEmail(newEmail);
        repository.save(user);
    }

    private void checkEmailDTO(UpdateEmailDTO dto) {
        if (dto.newEmail() == null) {
            throw new IllegalUserException("New email is null");
        }
        if (dto.newEmail().equals(dto.email())) {
            throw new IllegalUserException("Old and new emails are the same");
        }
        checkEmail(dto.newEmail());
    }

    private void checkPasswordDTO(UpdatePasswordDTO dto) {
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

    private void defaultUpdateCheck(Authentication authentication, EmailOwner updateDTO) {
        if (updateDTO == null) {
            throw new IllegalUserException("Request is empty");
        }
        checkAuthentication(authentication);
        if (updateDTO.email() == null) {
            throw new IllegalUserException("Email is null");
        }
    }

    private void checkAuthentication(Authentication authentication){
        if (authentication == null || authentication.getName() == null
                || authentication.getAuthorities() == null) {
            throw new IllegalUserException("Authentication error");
        }
    }

    private void checkEmail(String email) {
        try {
            validator.checkEmail(email);
        } catch (IllegalUserException ex) {
            throw new IllegalUserException("new email" + "\n" + ex.getMessage());
        }
    }

    private void checkPassword(String password) {
        try {
            validator.checkPassword(password);
        } catch (IllegalUserException ex) {
            throw new IllegalUserException("new password error" + "\n" + ex.getMessage());
        }
    }


    @Override
    public String delete(Authentication authentication, String email) {
        //todo implement deleting
        return null;
    }
}
