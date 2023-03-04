package online.strongnation.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements EmailOwner{
    private String email;
    private String password;

    @Override
    public String email() {
        return email;
    }
}