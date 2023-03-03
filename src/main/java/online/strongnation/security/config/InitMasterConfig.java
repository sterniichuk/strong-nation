package online.strongnation.security.config;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import online.strongnation.security.model.UserDTO;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Setter
@Getter
@Component
@PropertySources({
        @PropertySource("classpath:custom.properties")
})
@ConfigurationProperties(prefix = "master")
public class InitMasterConfig {
    private String email;
    private String password;

    public UserDTO getUserDTO(){
        return UserDTO.builder()
                .email(email)
                .password(password)
                .build();
    }
}
