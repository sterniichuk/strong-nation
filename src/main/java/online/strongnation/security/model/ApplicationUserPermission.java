package online.strongnation.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationUserPermission {
    //country
    COUNTRY_WRITE("country:write"),
    //region
    REGION_WRITE("region:write"),
    //POST
    POST_WRITE("post:write"),
    POST_READ_ALL_INFO("post:read_all"),
    POST_DELETE_ALL("post:delete_all"),
    //admin
    ADMIN_CREATE("admin:create"),
    ADMIN_READ("admin:read"),
    ADMIN_DELETE("admin:delete"),
    ADMIN_UPDATE("admin:update"),
    //developer
    DEVELOPER_CREATE("developer:create"),
    DEVELOPER_READ("developer:read"),
    DEVELOPER_UPDATE("developer:update"),
    DEVELOPER_DELETE("developer:delete"),
    //master
    MASTER_UPDATE("master:update"),
    //app
    PROPERTIES_READ("properties:read"),
    LOGS_READ("logs:read");

    private final String permission;
}
