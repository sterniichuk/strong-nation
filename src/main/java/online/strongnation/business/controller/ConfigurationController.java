package online.strongnation.business.controller;

import lombok.AllArgsConstructor;
import online.strongnation.business.config.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v2/config")
@AllArgsConstructor
public class ConfigurationController {
    Constants constants;

    @GetMapping("/get/constants")
    @PreAuthorize("hasAuthority('properties:read')")
    public ResponseEntity<String> get() {
        var photo = "PATH_TO_POST_PHOTO_DIRECTORY: " + constants.PATH_TO_POST_PHOTO_DIRECTORY;
        return new ResponseEntity<>(photo, HttpStatus.OK);
    }
}
