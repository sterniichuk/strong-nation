package online.strongnation.security.model;

public record UpdateEmailDTO(String email,
                             String newEmail) implements EmailOwner {
}
