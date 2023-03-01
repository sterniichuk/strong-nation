package online.strongnation.security.controller;

import lombok.AllArgsConstructor;
import online.strongnation.security.model.UpdateEmailDTO;
import online.strongnation.security.model.UpdatePasswordDTO;
import online.strongnation.security.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v2/master")
@AllArgsConstructor
public class MasterController {

    private final AuthenticationService service;

    @PutMapping("/update/password")
    @PreAuthorize("hasAuthority('master:update')")
    public ResponseEntity<?> changePassword(Authentication authentication,
                                            @RequestBody UpdatePasswordDTO passwordDTO) {
        String name = authentication.getName();
        return new ResponseEntity<>("name: " + name + "authorities: " + authentication.getAuthorities() + " userdto: " + passwordDTO, HttpStatus.OK);
    }

    @PutMapping("/update/email")
    @PreAuthorize("hasAuthority('master:update')")
    public ResponseEntity<?> changePassword(Authentication authentication,
                                            @RequestBody UpdateEmailDTO updateEmailDTO) {
        String name = authentication.getName();
        return new ResponseEntity<>("name: " + name + "authorities: " + authentication.getAuthorities() + " userdto: " + updateEmailDTO, HttpStatus.OK);
    }
}
