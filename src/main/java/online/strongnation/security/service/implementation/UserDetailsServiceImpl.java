package online.strongnation.security.service.implementation;

import lombok.RequiredArgsConstructor;
import online.strongnation.security.exception.UserNotFoundException;
import online.strongnation.security.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserValidator validator;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        validator.checkEmail(email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));
    }
}
