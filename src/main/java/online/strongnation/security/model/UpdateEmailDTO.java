package online.strongnation.security.model;

import lombok.Builder;

@Builder
public record UpdateEmailDTO(String email,
                             String newEmail) implements EmailOwner {
}
