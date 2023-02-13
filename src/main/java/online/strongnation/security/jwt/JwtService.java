package online.strongnation.security.jwt;

public interface JwtService {
    String extractUsername(String token);
}
