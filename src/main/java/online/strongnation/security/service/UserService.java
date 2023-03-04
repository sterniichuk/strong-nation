package online.strongnation.security.service;

import online.strongnation.security.model.UpdateEmailWithPasswordDTO;
import online.strongnation.security.model.UpdatePasswordDTO;
import online.strongnation.security.model.UserDTO;
import org.springframework.security.core.Authentication;

public interface UserService {
    String changePassword(Authentication authentication, UpdatePasswordDTO passwordDTO);

    String changeEmail(Authentication authentication, UpdateEmailWithPasswordDTO updateEmailDTO);

    String delete(Authentication authentication, UserDTO user);
}
