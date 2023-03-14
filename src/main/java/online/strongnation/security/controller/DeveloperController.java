package online.strongnation.security.controller;

import lombok.AllArgsConstructor;
import online.strongnation.security.model.*;
import online.strongnation.security.service.ManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/developer")
@AllArgsConstructor
public class DeveloperController {

    private final ManagerService service;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('developer:create')")
    public ResponseEntity<AuthenticationResponse> create(@RequestBody UserDTO user) {
        AuthenticationResponse register = service.add(user, Role.DEVELOPER);
        return new ResponseEntity<>(register, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('developer:read')")
    public ResponseEntity<List<String>> getEmails() {
        List<String> response = service.getEmails(Role.DEVELOPER);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/password")
    @PreAuthorize("hasAuthority('developer:update')")
    public ResponseEntity<String> changePassword(@RequestBody UserDTO passwordDTO) {
        String response = service.changePassword(passwordDTO, Role.DEVELOPER);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/email")
    @PreAuthorize("hasAuthority('developer:update')")
    public ResponseEntity<String> changeEmail(@RequestBody UpdateEmailDTO updateEmailDTO) {
        String response = service.changeEmail(updateEmailDTO, Role.DEVELOPER);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{email}")
    @PreAuthorize("hasAuthority('developer:delete')")
    public ResponseEntity<String> delete(@PathVariable("email") String email) {
        String response = service.delete(email, Role.DEVELOPER);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
