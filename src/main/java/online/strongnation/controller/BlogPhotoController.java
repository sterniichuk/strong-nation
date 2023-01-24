package online.strongnation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@RestController
@RequestMapping("blog-photo/v1")
@RequiredArgsConstructor
public class BlogPhotoController {
    private String string;

    @PostMapping("/upload/{id}")
    public ResponseEntity<Long> upload(@RequestParam("file") MultipartFile file,
                                                  @PathVariable("id") Long blogId) {
        try {
            byte[] bytes = file.getBytes();
            string = new String(bytes, StandardCharsets.UTF_8);
            System.out.println(string);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(blogId, HttpStatus.CREATED);
    }

    @GetMapping("/download/{id}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable Long id) {
        Resource file = loadAsResource(id.toString());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    private Resource loadAsResource(String id) {
        try {
            try (FileOutputStream fos = new FileOutputStream(id)) {
                fos.write(string.getBytes());
            }
            Path file = Path.of(id);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            return null;
        }
        catch (IOException e) {
            throw new IllegalStateException("Could not read file: " + id, e);
        }
    }
}
