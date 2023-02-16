package online.strongnation.security.controller;

import lombok.AllArgsConstructor;
import online.strongnation.business.model.dto.RegionDTO;
import online.strongnation.security.model.UserDTO;
import online.strongnation.security.model.AuthenticationResponse;
import online.strongnation.security.model.Role;
import online.strongnation.security.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/admin")
@AllArgsConstructor
public class AdminController {

    private final AuthenticationService service;

    @PostMapping("/add")
    public ResponseEntity<AuthenticationResponse> create(@RequestBody UserDTO user) {
        AuthenticationResponse register = service.register(user, Role.DEVELOPER);
        return new ResponseEntity<>(register, HttpStatus.CREATED);
    }

    @GetMapping("/get/{email}")
    public ResponseEntity<UserDTO> get(@PathVariable("email") String email) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> get() {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/update/{email}")
    public ResponseEntity<String> update(@RequestBody UserDTO user) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<RegionDTO> delete(@PathVariable("email") String email) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
