package online.strongnation.security.service;

import online.strongnation.security.model.Role;
import online.strongnation.security.model.UpdateEmailDTO;
import online.strongnation.security.model.UpdatePasswordDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {

    List<String> getEmails(Authentication authentication, Role role);

    String changePassword(Authentication authentication, UpdatePasswordDTO dto);

    String changeEmail(Authentication authentication, UpdateEmailDTO dto);

    String delete(Authentication authentication, String email);
}
