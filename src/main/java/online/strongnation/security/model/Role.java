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

    ADMIN(ADMIN_UPDATE, ADMIN_READ, Set.of(POST_WRITE, SLIDER_WRITE, UPDATE_SELF, DELETE_SELF)),
    DEVELOPER(DEVELOPER_UPDATE, DEVELOPER_READ, Set.of(POST_WRITE, POST_DELETE_ALL,
            COUNTRY_WRITE, REGION_WRITE,
            ADMIN_CREATE, ADMIN_READ, ADMIN_DELETE, ADMIN_UPDATE,
            DEVELOPER_DELETE,
            LOGS_READ, SLIDER_WRITE, UPDATE_SELF, DELETE_SELF)),
    MASTER(MASTER_UPDATE, MASTER_READ, Set.of(POST_WRITE, POST_DELETE_ALL,
            COUNTRY_WRITE, REGION_WRITE,
            ADMIN_CREATE, ADMIN_READ, ADMIN_DELETE, ADMIN_UPDATE,
            LOGS_READ,
            DEVELOPER_CREATE, DEVELOPER_READ,
            DEVELOPER_DELETE, DEVELOPER_UPDATE,
            PROPERTIES_READ, SLIDER_WRITE, UPDATE_SELF
    ));
    public static final String rolePrefix = "ROLE_";
    private final ApplicationUserPermission readPermission;
    private final ApplicationUserPermission updatePermission;

    private final Set<ApplicationUserPermission> permissions;


    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority(rolePrefix + this.name()));
        return permissions;
    }

    public boolean hasPermission(final ApplicationUserPermission permission) {
        return permissions.stream().anyMatch(x -> x.equals(permission));
    }

}
