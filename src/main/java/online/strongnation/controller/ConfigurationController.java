package online.strongnation.controller;

import lombok.AllArgsConstructor;
import online.strongnation.config.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("config/v1")
@AllArgsConstructor
public class ConfigurationController {
    Constants constants;

    @GetMapping("/get/constants")
    public ResponseEntity<String> get() {
        var response = "PATH_TO_POST_PHOTO_DIRECTORY: " + constants.PATH_TO_POST_PHOTO_DIRECTORY;
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
