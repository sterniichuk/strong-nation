package online.strongnation.security.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Getter
@Setter
@Component
@PropertySources({
        @PropertySource("classpath:custom.properties")
})
public class OriginsConfig {
    @Value("${my_security.origins.allowed}")
    private String[] allowedOrigins;
}
