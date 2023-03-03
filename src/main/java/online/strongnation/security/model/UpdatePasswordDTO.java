package online.strongnation.security.model;

import lombok.Builder;

@Builder
public record UpdatePasswordDTO(String email,
                                String password,
                                String newPassword,
                                String newPasswordChecking) implements EmailOwner{
}
