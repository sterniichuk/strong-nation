package online.strongnation.business.controller;

import lombok.AllArgsConstructor;
import online.strongnation.business.config.Constants;
import online.strongnation.business.config.SecurityConstants;
import online.strongnation.business.service.PostPhotoService;
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
    Constants constants;

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
//
//
//    @GetMapping("/direct-download/{fileName}")
//    public ResponseEntity<InputStreamResource> downloadPhoto(@PathVariable String fileName) {
//        String photosDirectory = constants.getAllPathToPhoto();
//        try {
//            File file = new File(photosDirectory + fileName);
//            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
//            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//            headers.add("Pragma", "no-cache");
//            headers.add("Expires", "0");
//
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .contentLength(file.length())
//                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                    .body(resource);
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
