package online.strongnation.security.controller;

import lombok.RequiredArgsConstructor;
import online.strongnation.business.config.SecurityConstants;
import online.strongnation.security.model.AuthenticationRequest;
import online.strongnation.security.model.AuthenticationResponse;
import online.strongnation.security.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/auth")
@CrossOrigin(origins = SecurityConstants.URL_WITH_ENABLED_CROSS_ORIGIN_REQUESTS)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
//
//    @PostMapping("/register")
//    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request) {
//        return ResponseEntity.ok(service.register(request, Role.MASTER));
//    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }


}
