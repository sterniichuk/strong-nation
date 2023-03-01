package online.strongnation.security.model;

public record UpdatePasswordDTO(String email,
                                String password,
                                String newPassword,
                                String newPasswordChecking) implements EmailOwner{
}
