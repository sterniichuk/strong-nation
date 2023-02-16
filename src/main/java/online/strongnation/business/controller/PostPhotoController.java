package online.strongnation.business.controller;

import lombok.AllArgsConstructor;
import online.strongnation.business.config.Constants;
import online.strongnation.business.config.SecurityConstants;
import online.strongnation.business.service.PostPhotoService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v2/post-photo")
@CrossOrigin(origins = SecurityConstants.URL_WITH_ENABLED_CROSS_ORIGIN_REQUESTS)
@AllArgsConstructor
public class PostPhotoController {

    PostPhotoService service;
    Constants constants;

    @PostMapping("/upload/{id}")
    @PreAuthorize("hasAuthority('post:write')")
    public ResponseEntity<Long> upload(@RequestParam("file") MultipartFile file,
                                       @PathVariable("id") Long id) {
        final var response = service.uploadPhotoByPostId(id, file);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/download/{id}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable Long id) {
        Resource file = service.downloadPhotoByPostId(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
