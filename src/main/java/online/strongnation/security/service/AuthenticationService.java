package online.strongnation.security.service;

import online.strongnation.security.model.UserDTO;
import online.strongnation.security.model.AuthenticationResponse;
import online.strongnation.security.model.Role;

public interface AuthenticationService {
    AuthenticationResponse register(UserDTO request, Role role);

    AuthenticationResponse authenticate(UserDTO request);
}
