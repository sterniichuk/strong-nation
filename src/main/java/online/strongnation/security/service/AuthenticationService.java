package online.strongnation.security.service;

import online.strongnation.security.model.AuthenticationRequest;
import online.strongnation.security.model.AuthenticationResponse;
import online.strongnation.security.model.Role;

public interface AuthenticationService {
    AuthenticationResponse register(AuthenticationRequest request, Role role);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
