package online.strongnation.security.controller;

import lombok.AllArgsConstructor;
import online.strongnation.business.model.dto.RegionDTO;
import online.strongnation.security.model.AuthenticationResponse;
import online.strongnation.security.model.Role;
import online.strongnation.security.model.UserDTO;
import online.strongnation.security.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/developer")
@AllArgsConstructor
public class DeveloperController {

    private final AuthenticationService service;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('developer:create')")
    public ResponseEntity<AuthenticationResponse> create(@RequestBody UserDTO user) {
        AuthenticationResponse register = service.register(user, Role.DEVELOPER);
        return new ResponseEntity<>(register, HttpStatus.CREATED);
    }

    @GetMapping("/get/{email}")
    @PreAuthorize("hasAuthority('developer:read')")
    public ResponseEntity<UserDTO> get(@PathVariable("email") String email) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('developer:read')")
    public ResponseEntity<List<UserDTO>> get() {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/update/{email}")
    @PreAuthorize("hasAuthority('developer:update')")
    public ResponseEntity<String> update(@RequestBody UserDTO user) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{email}")
    @PreAuthorize("hasAuthority('developer:delete')")
    public ResponseEntity<RegionDTO> delete(@PathVariable("email") String email) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
