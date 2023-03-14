package online.strongnation.business.controller;

import lombok.AllArgsConstructor;
import online.strongnation.business.service.SliderService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("api/v2/slider-photo")
@AllArgsConstructor
public class SliderPhotoController {

    SliderService service;

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('slider:write')")
    public ResponseEntity<Long> upload(@RequestParam("file") MultipartFile file) {
        final Long response = service.uploadSliderPhoto(file);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/download/{id}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable Long id) {
        Resource file = service.downloadSliderPhotoById(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/all-id")
    public ResponseEntity<List<Long>> all() {
        final List<Long> response = service.getListOfSliderPhotoIds();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('slider:write')")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        final Long response = service.deleteSliderPhotoById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
