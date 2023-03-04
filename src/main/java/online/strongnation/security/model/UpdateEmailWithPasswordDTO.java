package online.strongnation.security.model;

public record UpdateEmailWithPasswordDTO(String email,
                                         String newEmail,
                                         String password
                                         ) implements EmailOwner {
}
