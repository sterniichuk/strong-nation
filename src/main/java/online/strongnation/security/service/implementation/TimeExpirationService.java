package online.strongnation.security.service.implementation;

import lombok.RequiredArgsConstructor;
import online.strongnation.security.config.JwtConfig;
import online.strongnation.security.model.TokenExpirationConfig;
import online.strongnation.security.exception.SecurityException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TimeExpirationService {

    private final JwtConfig config;

    private static final List<String> allowedUnits = List.of(
            "NANOSECONDS",
            "MICROSECONDS",
            "MILLISECONDS",
            "SECONDS",
            "MINUTES",
            "HOURS",
            "DAYS");

    public TokenExpirationConfig changeTimeExpirationTime(TokenExpirationConfig time) {
        if (time == null) {
            throw new SecurityException("Body is empty");
        }
        if (time.tokenExpirationTime() == null) {
            throw new SecurityException("Expiration time is not specified");
        }
        if (time.units() == null) {
            throw new SecurityException("Units of time are not specified");
        }
        if (time.tokenExpirationTime() < 1) {
            throw new SecurityException("Token expiration time less than 1");
        }
        try {
            TimeUnit.valueOf(time.units());
        } catch (IllegalArgumentException e) {
            throw new SecurityException("Illegal format of units. Allowed are " + allowedUnits);
        }
        JwtConfig.staticTokenExpirationTime = time.tokenExpirationTime();
        JwtConfig.staticUnits = time.units();
        return time;
    }

    public TokenExpirationConfig get() {
        return new TokenExpirationConfig(config.getTokenExpirationTime(), config.getUnits());
    }
}
