package online.strongnation.security.service;

import online.strongnation.security.model.*;

import java.util.List;

public interface ManagerService {

    AuthenticationResponse add(UserDTO user, Role role);

    List<String> getEmails(Role role);

    String changePassword(UserDTO dto, Role role);

    String changeEmail(UpdateEmailDTO dto, Role role);

    String delete(String email, Role role);
}
