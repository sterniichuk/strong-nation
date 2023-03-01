package online.strongnation.security.service.implementation;

import online.strongnation.security.exception.IllegalUserException;
import online.strongnation.security.exception.UserNotFoundException;
import online.strongnation.security.model.UserDTO;
import online.strongnation.security.model.AuthenticationResponse;
import online.strongnation.security.repository.UserRepository;
import online.strongnation.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import online.strongnation.security.model.User;
import online.strongnation.security.model.Role;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserValidator validator;

    @Override
    public AuthenticationResponse register(UserDTO request, Role role) {
        final String email = request.getEmail();
        final String password = request.getPassword();
        validator.checkEmail(email);
        validator.checkPassword(password);
        if(repository.existsByEmail(email)){
            throw new IllegalUserException("Email: " + email +" is already in use");
        }
        var user = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .role(role)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(UserDTO request) {
        String email = request.getEmail();
        String password = request.getPassword();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );
        var user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
