package online.strongnation.security.service.implementation;

import lombok.RequiredArgsConstructor;
import online.strongnation.security.exception.IllegalUserException;
import online.strongnation.security.exception.UserNotFoundException;
import online.strongnation.security.model.UpdateEmailWithPasswordDTO;
import online.strongnation.security.model.UpdatePasswordDTO;
import online.strongnation.security.model.User;
import online.strongnation.security.model.UserDTO;
import online.strongnation.security.repository.UserRepository;
import online.strongnation.security.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserUtils utils;

    @Override
    @Transactional
    public String changePassword(Authentication authentication, UpdatePasswordDTO dto) {
        utils.selfUpdateCheck(authentication, dto);
        utils.checkPasswordDTO(dto);
        String email = dto.email();
        User user = getUser(email);
        String oldPasswordHash = user.getPasswordHash();
        checkPassword(oldPasswordHash, dto.password());
        String newPassword = dto.newPassword();
        if (passwordEncoder.matches(newPassword, oldPasswordHash)) {
            throw new IllegalUserException("The new and old passwords are the same");
        }
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        repository.save(user);
        return "Password of " + email + " user is updated";
    }

    private User getUser(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));
    }

    private void checkPassword(String hash, String rawOldPassword) {
        if (!passwordEncoder.matches(rawOldPassword, hash)) {
            throw new IllegalUserException("Password is not correct");
        }
    }

    @Override
    public String changeEmail(Authentication authentication, UpdateEmailWithPasswordDTO updateEmailDTO) {
        utils.selfUpdateCheck(authentication, updateEmailDTO);
        utils.checkUpdateEmailDTO(updateEmailDTO);
        String email = updateEmailDTO.email();
        User user = getUser(email);
        checkPassword(user.getPasswordHash(), updateEmailDTO.password());
        String newEmail = updateEmailDTO.newEmail();
        repository.findByEmail(newEmail).ifPresent(x -> {
            throw new IllegalUserException(newEmail + " is already taken");
        });
        user.setEmail(newEmail);
        repository.save(user);
        return "Email: " + email + " is changed to: " + newEmail;
    }

    @Override
    public String delete(Authentication authentication, UserDTO dto) {
        utils.selfUpdateCheck(authentication, dto);
        utils.notNull(dto.getPassword(), "Password");
        User user = getUser(dto.email());
        checkPassword(user.getPasswordHash(), dto.getPassword());
        repository.deleteById(user.getId());
        return "User: " + dto.getEmail() + " is deleted";
    }
}
