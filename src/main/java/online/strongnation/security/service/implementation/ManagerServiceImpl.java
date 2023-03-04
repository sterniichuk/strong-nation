package online.strongnation.security.service.implementation;

import online.strongnation.security.config.InitMasterConfig;
import online.strongnation.security.exception.IllegalUserException;
import online.strongnation.security.exception.UserNotFoundException;
import online.strongnation.security.model.*;
import online.strongnation.security.repository.UserRepository;
import online.strongnation.security.service.AuthenticationService;
import online.strongnation.security.service.ManagerService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;

@Service
public class ManagerServiceImpl implements ManagerService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final UserUtils utils;

    public ManagerServiceImpl(UserRepository repository,
                              PasswordEncoder passwordEncoder,
                              AuthenticationService authenticationService,
                              InitMasterConfig masterConfig, UserUtils utils) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
        this.utils = utils;
        initMaster(masterConfig);
    }

    private void initMaster(InitMasterConfig masterConfig) {
        List<String> emails = repository.getEmailsByRole(Role.MASTER);
        int size = emails.size();
        switch (size) {
            case 0 -> {
                UserDTO userDTO = masterConfig.getUserDTO();
                utils.checkNewUser(userDTO);
                authenticationService.register(userDTO, Role.MASTER);
                logger.info("Default master: " + userDTO.getEmail() + " created");
            }
            case 1 -> logger.info("Master is already present in the DB. Master: " + emails.get(0));
            default -> throw new IllegalStateException("Here is more than 1 MASTER in the DB");
        }
    }

    @Override
    public AuthenticationResponse add(UserDTO user, Role role) {
        utils.checkRole(role);
        utils.checkNewUser(user);
        return authenticationService.register(user, role);
    }

    @Override
    public List<String> getEmails(Role role) {
        utils.checkRole(role);
        return repository.getEmailsByRole(role);
    }

    @Override
    @Transactional
    public String changePassword(UserDTO dto, Role role) {
        utils.checkOldUser(dto);
        String email = dto.getEmail();
        User user = getUserForOperation(email, role);
        updatePassword(user, dto.getPassword());
        return "Password of " + email + " is updated";
    }

    private void updatePassword(User user, String newPassword) {
        String oldPasswordHash = user.getPasswordHash();
        if(passwordEncoder.matches(newPassword, oldPasswordHash)){
            throw new IllegalUserException("The new and old passwords are the same");
        }
        String newEncodedPassword = passwordEncoder.encode(newPassword);
        user.setPasswordHash(newEncodedPassword);
        repository.save(user);
    }

    @Override
    @Transactional
    public String changeEmail(UpdateEmailDTO dto, Role role) {
        utils.checkEmailDTO(dto);
        String email = dto.email();
        User user = getUserForOperation(email, role);
        updateEmail(user, dto.newEmail());
        return "Email of " + email + " is updated to " + dto.newEmail();
    }


    private User getUserForOperation(String email, Role role) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("There is no user with email: " + email));
        if(role != user.getRole()){
            throw new IllegalUserException("You are trying to get access to user with role: " + user.getRole() +
                    " using request for role: " + role +". Change request.");
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

    @Override
    public String delete(String email, Role role) {
        utils.checkRole(role);
        utils.checkOldEmail(email);
        User user = getUserForOperation(email, role);
        repository.deleteById(user.getId());
        return "Deleted user: " + email;
    }
}