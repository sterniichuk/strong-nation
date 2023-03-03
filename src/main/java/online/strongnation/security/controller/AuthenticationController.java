package online.strongnation.security.controller;

import lombok.RequiredArgsConstructor;
import online.strongnation.security.model.UserDTO;
import online.strongnation.security.model.AuthenticationResponse;
import online.strongnation.security.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody UserDTO request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
