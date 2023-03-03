package online.strongnation.unit.service;

import online.strongnation.security.config.InitMasterConfig;
import online.strongnation.security.exception.IllegalUserException;
import online.strongnation.security.model.*;
import online.strongnation.security.repository.UserRepository;
import online.strongnation.security.service.AuthenticationService;
import online.strongnation.security.service.UserService;
import online.strongnation.security.service.implementation.UserServiceImpl;
import online.strongnation.security.service.implementation.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator validator;

    @Mock
    private AuthenticationService authenticationService;

    private UserService service;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private InitMasterConfig masterConfig;
    private final String masterEmail = "masterMail";

    @BeforeEach
    void setUp() {
        //given
        given(userRepository.getEmailsByRole(Role.MASTER)).willReturn(List.of(masterEmail));
        service = new UserServiceImpl(userRepository, validator, encoder, authenticationService, masterConfig);
    }

    @Test
    void adminAddAdmin() {
        addRole("some random mail of updater", Role.ADMIN, Role.ADMIN, "new dev mail");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ADMIN",
            "DEVELOPER",
    })
    void addDeveloperByNotAllowedUser(Role creatorRole) {
        addRole("some random mail of updater", creatorRole, Role.DEVELOPER, "new dev mail");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ADMIN",
            "DEVELOPER",
            "MASTER"
    })
    void someoneAddMaster(Role creatorRole) {
        addRole("some random mail of updater", creatorRole, Role.MASTER, "new master mail");
    }

    void addRole(String updaterEmail, Role updater, Role targetRole, String targetEmail) {
        //given
        Authentication authMock = getAuthMock(updaterEmail, updater);
        UserDTO newUser = UserDTO.builder().email(targetEmail).password("hash").build();
        //when
        //then
        throwableOperationTest(updaterEmail, updater, targetRole,
                () -> service.add(authMock, newUser, targetRole), Role::getCreatePermission);
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "ADMIN",
            "DEVELOPER",
            "MASTER"
    })
    void someoneDeleteMaster(Role updater) {
        String updaterEmail = "updaterEmail";
        deleteRole(updaterEmail, updater, Role.MASTER, masterEmail);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ADMIN",
            "DEVELOPER",
    })
    void deleteDeveloper(Role updater) {
        String updaterEmail = "updaterEmail";
        deleteRole(updaterEmail, updater, Role.DEVELOPER, "some developer");
    }

    void deleteRole(String updaterEmail, Role updater, Role targetRole, String targetEmail) {
        //given
        //when
        Authentication authMock = getAuthMock(updaterEmail, updater);
        //then
        throwableOperationTestWithTargetFind(updaterEmail, updater,targetEmail, targetRole,
                () -> service.delete(authMock, targetEmail), Role::getDeletePermission);
    }

    void throwableOperationTest(String updaterEmail, Role updater, Role targetRole,
                                Runnable underTest, Function<Role, ApplicationUserPermission> permissionGetter) {
        //given
        User updaterUser = User.builder().email(updaterEmail).role(updater).build();
        //when
        //then
        when(userRepository.findByEmail(updaterEmail))
                .thenAnswer((x) -> Optional.of(updaterUser));
        assertThatThrownBy(underTest::run)
                .isInstanceOf(IllegalUserException.class)
                .hasMessage("User " + updaterEmail + " has no " + permissionGetter.apply(targetRole).getPermission() + " permission");
    }

    private Authentication getAuthMock(String email, Role role) {
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getAuthorities()).thenAnswer((x) -> role.getGrantedAuthorities());
        given(authentication.getName()).willReturn(email);
        return authentication;
    }


    @Test
    void getEmailsOfAdminsByAdmin() {
        getEmails("reader", Role.ADMIN, Role.ADMIN);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ADMIN",
            "DEVELOPER",
    })
    void getEmailsOfDeveloperByNotAllowedUsers(Role reader) {
        String updaterEmail = "updaterEmail";
        getEmails(updaterEmail, reader, Role.DEVELOPER);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ADMIN",
            "DEVELOPER",
            "MASTER"
    })
    void getEmailOfMaster(Role reader) {
        String updaterEmail = "updaterEmail";
        getEmails(updaterEmail, reader, Role.MASTER);
    }

    void getEmails(String readerEmail, Role reader, Role targetRole) {
        //given
        //when
        Authentication authMock = getAuthMock(readerEmail, reader);
        //then
        throwableOperationTest(readerEmail, reader, targetRole,
                () -> service.getEmails(authMock, targetRole), Role::getReadPermission);
    }

    void changePassword(String updaterEmail, Role updaterRole, String targetEmail, Role targetRole) {
        //given
        //when
        Authentication authMock = getAuthMock(updaterEmail, updaterRole);
        UpdatePasswordDTO dto = UpdatePasswordDTO.builder().newPassword("pass").password("old password")
                .newPasswordChecking("pass").email(targetEmail).build();
        //then
        throwableOperationTestWithTargetFind(updaterEmail, updaterRole,targetEmail, targetRole,
                () -> service.changePassword(authMock, dto), Role::getUpdatePermission);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ADMIN",
            "DEVELOPER",
    })
    void changePasswordForMaster(Role updaterRole) {
        changePassword("not master", updaterRole, masterEmail, Role.MASTER);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ADMIN",
            "DEVELOPER",
    })
    void changePasswordForDeveloper(Role updaterRole) {
        changePassword("user editor", updaterRole, "developer target", Role.DEVELOPER);
    }

    @Test
    void changePasswordForAdmin() {
        changePassword("admin editor", Role.ADMIN, "admin target", Role.ADMIN);
    }

    void changeEmail(String updaterEmail, Role updaterRole, String targetEmail, Role targetRole) {
        //given
        //when
        Authentication authMock = getAuthMock(updaterEmail, updaterRole);
        UpdateEmailDTO dto = UpdateEmailDTO.builder().email(targetEmail).newEmail("new amazing email").build();
        //then
        throwableOperationTestWithTargetFind(updaterEmail, updaterRole,targetEmail, targetRole,
                () -> service.changeEmail(authMock, dto), Role::getUpdatePermission);
    }

    void throwableOperationTestWithTargetFind(String updaterEmail, Role updater,String targetEmail, Role targetRole,
                                              Runnable underTest, Function<Role, ApplicationUserPermission> permissionGetter){
        User targetUser = User.builder().email(targetEmail).role(targetRole).build();
        when(userRepository.findByEmail(targetEmail))
                .thenAnswer((x) -> Optional.of(targetUser));
        throwableOperationTest(updaterEmail, updater, targetRole, underTest, permissionGetter);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ADMIN",
            "DEVELOPER",
    })
    void changeEmailForMaster(Role updaterRole) {
        changeEmail("not master", updaterRole, masterEmail, Role.MASTER);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ADMIN",
            "DEVELOPER",
    })
    void changeEmailForDeveloper(Role updaterRole) {
        changeEmail("user editor", updaterRole, "developer target", Role.DEVELOPER);
    }

    @Test
    void changeEmailForAdmin() {
        changePassword("admin editor", Role.ADMIN, "admin target", Role.ADMIN);
    }


    @Test
    void delete() {
    }
}