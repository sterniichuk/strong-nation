package online.strongnation.security.controller;

import lombok.AllArgsConstructor;
import online.strongnation.security.model.UpdateEmailWithPasswordDTO;
import online.strongnation.security.model.UpdatePasswordDTO;
import online.strongnation.security.model.UserDTO;
import online.strongnation.security.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/user")
@AllArgsConstructor
public class UserController {
    private final UserService service;

    @PutMapping("/update/password")
    @PreAuthorize("hasAuthority('user:update-self')")
    public ResponseEntity<String> changePassword(Authentication authentication,
                                                 @RequestBody UpdatePasswordDTO passwordDTO) {
        var response = service.changePassword(authentication, passwordDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/email")
    @PreAuthorize("hasAuthority('user:update-self')")
    public ResponseEntity<String> changeEmail(Authentication authentication,
                                              @RequestBody UpdateEmailWithPasswordDTO updateEmailDTO) {
        var response = service.changeEmail(authentication, updateEmailDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('user:delete-self')")
    public ResponseEntity<String> delete(Authentication authentication,
                                         @RequestBody UserDTO user) {
        var response = service.delete(authentication, user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
