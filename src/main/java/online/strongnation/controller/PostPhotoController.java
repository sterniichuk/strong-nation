package online.strongnation.controller;

import lombok.AllArgsConstructor;
import online.strongnation.config.SecurityConstants;
import online.strongnation.service.PostPhotoService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("post-photo/v1")
@CrossOrigin(origins = SecurityConstants.URL_WITH_ENABLED_CROSS_ORIGIN_REQUESTS)
@AllArgsConstructor
public class PostPhotoController {

    PostPhotoService service;

    @PostMapping("/upload/{id}")
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
