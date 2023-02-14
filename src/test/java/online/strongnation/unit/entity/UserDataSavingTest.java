package online.strongnation.unit.entity;

import online.strongnation.security.model.Role;
import online.strongnation.security.model.User;
import online.strongnation.security.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserDataSavingTest {

    @Autowired
    private UserRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void simpleSave() {
        //given
        User user = new User("Email", "21ke2l1lek21le", Role.DEVELOPER);
        //when
        repository.save(user);
        //then
        User savedUser = repository.findAll().get(0);
        assertThat(savedUser).isEqualTo(user);
    }
}