package online.strongnation.controller;

import lombok.AllArgsConstructor;
import online.strongnation.config.Constants;
import online.strongnation.config.SecurityConstants;
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
        var photo = "PATH_TO_POST_PHOTO_DIRECTORY: " + constants.PATH_TO_POST_PHOTO_DIRECTORY;
        var cors = "URL_WITH_ENABLED_CROSS_ORIGIN_REQUESTS: " + SecurityConstants.URL_WITH_ENABLED_CROSS_ORIGIN_REQUESTS;
        var response = photo + "\n" + cors;
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
