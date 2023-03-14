package online.strongnation.security.controller;

import lombok.AllArgsConstructor;
import online.strongnation.security.model.TokenExpirationConfig;
import online.strongnation.security.service.implementation.TimeExpirationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v2/security-configuration")
@AllArgsConstructor
public class TimeExpirationController {
    private TimeExpirationService timeExpirationService;

    @PutMapping("token/time-expiration/update")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER, ROLE_MASTER')")
    public ResponseEntity<TokenExpirationConfig> changeTimeExpirationTime(@RequestBody TokenExpirationConfig time) {
        TokenExpirationConfig response = timeExpirationService.changeTimeExpirationTime(time);
        return ResponseEntity.ok(response);
    }

    @GetMapping("token/time-expiration/get")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER, ROLE_MASTER')")
    public ResponseEntity<TokenExpirationConfig> get() {
        TokenExpirationConfig response = timeExpirationService.get();
        return ResponseEntity.ok(response);
    }
}
