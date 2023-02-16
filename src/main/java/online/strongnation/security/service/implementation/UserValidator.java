package online.strongnation.security.service.implementation;

import lombok.RequiredArgsConstructor;
import online.strongnation.security.config.UserConfig;
import online.strongnation.security.exception.IllegalUserException;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserValidator {
    private final UserConfig config;

    public void checkEmail(String emailAddress){
        if(emailAddress == null){
            throw new IllegalUserException("Email can't be null");
        }
        if(!patternMatches(emailAddress, config.getEmailRegexPattern())){
            throw new IllegalUserException("Email doesn't match the " + config.getEmailValidationType());
        }
    }

    public void checkPassword(String password){
        if(password == null){
            throw new IllegalUserException("Password can't be null");
        }
        if(!patternMatches(password, config.getPasswordRegexPattern())){
            throw new IllegalUserException("Password doesn't follow the rules:\n" + config.getPasswordValidationType());
        }
    }

    private boolean patternMatches(String s, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(s)
                .matches();
    }
}
