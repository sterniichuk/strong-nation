package online.strongnation.unit.repository;


import online.strongnation.security.model.Role;
import online.strongnation.security.model.User;
import online.strongnation.security.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void testFindEmailsByRole(){
        //given
        String dev1 = "em1";
        String dev2 = "em3";
        List<User> users = List.of(
                new User(dev1, "efwk1kk3j4", Role.DEVELOPER),
                new User("em2", "efwk1kk3j4", Role.MASTER),
                new User(dev2, "efwk1kk3j4", Role.DEVELOPER),
                new User("em4", "efwk1kk3j4", Role.ADMIN)
        );
        repository.saveAll(users);
        //when
        List<String> emailsByRole = repository.getEmailsByRole(Role.DEVELOPER);
        //then
        assertThat(emailsByRole.containsAll(List.of(dev1, dev2))).isTrue();
    }

}