package online.strongnation.security.service;

import online.strongnation.security.model.*;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {

    AuthenticationResponse add(Authentication authentication, UserDTO user, Role role);


    List<String> getEmails(Authentication authentication, Role role);

    String changePassword(Authentication authentication, UpdatePasswordDTO dto);

    String changeEmail(Authentication authentication, UpdateEmailDTO dto);

    String delete(Authentication authentication, String email);
}
