package online.strongnation.unit.service;

import online.strongnation.security.exception.IllegalUserException;
import online.strongnation.security.service.implementation.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class UserValidatorTest {

    @Autowired
    private UserValidator service;

    @Test
    void checkEmail() {
        String email = "dev4@gmail.com";
        service.checkEmail(email);
    }

    @Test
    void checkEmailThrows() {
        String email = " dev4@gmail.com";
        assertThatThrownBy(() -> service.checkEmail(email)).isInstanceOf(IllegalUserException.class);
    }

    @Test
    void checkPassValid() {
        String email = "dev4@gmAil.com";
        service.checkPassword(email);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "12e2e",
            "11111111",
            "aaaaaaaa",
            "AAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    })
    void checkPassInvalid(String pass) {
        assertThatThrownBy(() -> service.checkPassword(pass)).isInstanceOf(IllegalUserException.class);
    }
}