package online.strongnation.security.service.implementation;

import online.strongnation.security.config.InitMasterConfig;
import online.strongnation.security.exception.IllegalUserException;
import online.strongnation.security.exception.UserNotFoundException;
import online.strongnation.security.model.*;
import online.strongnation.security.repository.UserRepository;
import online.strongnation.security.service.AuthenticationService;
import online.strongnation.security.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

import static online.strongnation.security.model.ApplicationUserPermission.DELETE_SELF;
import static online.strongnation.security.model.ApplicationUserPermission.UPDATE_SELF;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserValidator validator;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;
    private final Logger logger = Logger.getLogger(this.getClass().getName());


    public UserServiceImpl(UserRepository repository,
                           UserValidator validator,
                           PasswordEncoder passwordEncoder,
                           AuthenticationService authenticationService,
                           InitMasterConfig masterConfig) {
        this.repository = repository;
        this.validator = validator;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
        initMaster(masterConfig);
    }

    private void initMaster(InitMasterConfig masterConfig) {
        List<String> emails = repository.getEmailsByRole(Role.MASTER);
        int size = emails.size();
        switch (size) {
            case 0 -> {
                UserDTO userDTO = masterConfig.getUserDTO();
                checkUser(userDTO);
                authenticationService.register(userDTO, Role.MASTER);
                logger.info("Default master: " + userDTO.getEmail() + " created");
            }
            case 1 -> logger.info("Master is already present in the DB. Master: " + emails.get(0));
            default -> throw new IllegalStateException("Here is more than 1 MASTER in the DB");
        }
    }

    @Override
    public AuthenticationResponse add(Authentication authentication, UserDTO user, Role role) {
        checkRole(role);
        checkUser(user);
        checkAuthentication(authentication);
        String email = authentication.getName();
        User creator = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("There is no user with email: " + email));
        checkPermission(creator, role.getCreatePermission());
        return authenticationService.register(user, role);
    }

    private void checkUser(UserDTO user) {
        if (user == null) {
            throw new IllegalUserException("Body is empty");
        }
        final String email = user.getEmail();
        final String password = user.getPassword();
        validator.checkEmail(email);
        validator.checkPassword(password);
    }

    @Override
    public List<String> getEmails(Authentication authentication, Role role) {
        checkRole(role);
        checkAuthentication(authentication);
        String email = authentication.getName();
        User reader = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("There is no user with email: " + email));
        checkPermission(reader, role.getReadPermission());
        return repository.getEmailsByRole(role);
    }

    private void checkRole(Role role) {
        if (role == null) {
            throw new IllegalUserException("Role can't be null");
        }
    }

    @Override
    @Transactional
    public String changePassword(Authentication authentication, UpdatePasswordDTO dto) {
        defaultUpdateCheck(authentication, dto);
        checkPasswordDTO(dto);
        String email = dto.email();
        User user = getUserForUpdating(authentication, email);
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
        User user = getUserForUpdating(authentication, email);
        updateEmail(user, dto.newEmail());
        return "Email of " + email + " is updated to " + dto.newEmail();
    }

    private User getUserForUpdating(Authentication authentication, String email) {
        return getUserForOperation(authentication, email, UPDATE_SELF, Role::getUpdatePermission);
    }

    private User getUserForOperation(Authentication authentication, String email,
                                     ApplicationUserPermission selfPermission,
                                     Function<Role, ApplicationUserPermission> getterOfOperationPermissionOfUpdater) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("There is no user with email: " + email));
        String updaterEmail = authentication.getName();
        if (email.equals(updaterEmail)) {
            checkPermission(user, selfPermission);
        } else {
            User updater = repository.findByEmail(updaterEmail)
                    .orElseThrow(() -> new UserNotFoundException("There is no updater with email: " + updaterEmail));
            checkPermission(updater, getterOfOperationPermissionOfUpdater.apply(user.getRole()));
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

    private void checkAuthentication(Authentication authentication) {
        if (authentication == null || authentication.getName() == null
                || authentication.getAuthorities() == null) {
            throw new IllegalUserException("Authentication object is invalid. Custom exception");
        }
    }

    private void checkEmail(String email) {
        try {
            validator.checkEmail(email);
        } catch (IllegalUserException ex) {
            throw new IllegalUserException("new email\n" + ex.getMessage());
        }
    }

    private void checkPassword(String password) {
        try {
            validator.checkPassword(password);
        } catch (IllegalUserException ex) {
            throw new IllegalUserException("new password error\n" + ex.getMessage());
        }
    }

    private User getUserForDeleting(Authentication authentication, String email) {
        return getUserForOperation(authentication, email, DELETE_SELF, Role::getDeletePermission);
    }

    @Override
    public String delete(Authentication authentication, String email) {
        checkAuthentication(authentication);
        checkEmail(email);
        User user = getUserForDeleting(authentication, email);
        repository.deleteById(user.getId());
        return "Deleted user: " + email;
    }
}