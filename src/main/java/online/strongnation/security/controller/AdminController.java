package online.strongnation.security.controller;

import lombok.AllArgsConstructor;
import online.strongnation.security.model.AuthenticationResponse;
import online.strongnation.security.model.Role;
import online.strongnation.security.model.UpdateEmailDTO;
import online.strongnation.security.model.UserDTO;
import online.strongnation.security.service.ManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/admin")
@AllArgsConstructor
public class AdminController {

    private final ManagerService service;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<AuthenticationResponse> create(@RequestBody UserDTO user) {
        AuthenticationResponse register = service.add(user, Role.ADMIN);
        return new ResponseEntity<>(register, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<String>> getEmails() {
        List<String> response = service.getEmails(Role.ADMIN);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/password")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<String> changePassword(@RequestBody UserDTO passwordDTO) {
        String response = service.changePassword(passwordDTO, Role.ADMIN);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/email")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<String> changeEmail(@RequestBody UpdateEmailDTO updateEmailDTO) {
        String response = service.changeEmail(updateEmailDTO, Role.ADMIN);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{email}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<String> delete(@PathVariable("email") String email) {
        String response = service.delete(email, Role.ADMIN);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
