package online.strongnation.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static online.strongnation.security.model.ApplicationUserPermission.*;

@Getter
@AllArgsConstructor
public enum Role {
    ADMIN(Set.of(POST_WRITE)),
    DEVELOPER(Set.of(POST_WRITE, POST_DELETE_ALL,
            COUNTRY_WRITE, REGION_WRITE,
            ADMIN_CREATE, ADMIN_READ, ADMIN_DELETE, ADMIN_UPDATE,
            LOGS_READ)),
    MASTER(Set.of(POST_WRITE, POST_DELETE_ALL,
            COUNTRY_WRITE, REGION_WRITE,
            ADMIN_CREATE, ADMIN_READ, ADMIN_DELETE, ADMIN_UPDATE,
            LOGS_READ,
            DEVELOPER_CREATE, DEVELOPER_READ,
            DEVELOPER_DELETE, DEVELOPER_UPDATE,
            PROPERTIES_READ
    ));

    private final Set<ApplicationUserPermission> permissions;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
