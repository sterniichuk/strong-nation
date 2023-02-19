package online.strongnation.security.controller;

import lombok.RequiredArgsConstructor;
import online.strongnation.security.model.UserDTO;
import online.strongnation.security.model.AuthenticationResponse;
import online.strongnation.security.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static online.strongnation.business.config.SecurityConstants.FRONTEND_WITH_ENABLED_CROSS_ORIGIN_REQUESTS;
import static online.strongnation.business.config.SecurityConstants.LOCAL_HOST_WITH_ENABLED_CROSS_ORIGIN_REQUESTS;

@RestController
@RequestMapping("/api/v2/auth")
@CrossOrigin(origins = {LOCAL_HOST_WITH_ENABLED_CROSS_ORIGIN_REQUESTS, FRONTEND_WITH_ENABLED_CROSS_ORIGIN_REQUESTS})
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody UserDTO request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
