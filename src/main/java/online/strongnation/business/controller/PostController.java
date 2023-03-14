package online.strongnation.business.controller;

import lombok.AllArgsConstructor;
import online.strongnation.business.model.dto.GetPostResponse;
import online.strongnation.business.model.dto.GetPostResponseByCountry;
import online.strongnation.business.model.dto.PostDTO;
import online.strongnation.business.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v2/post")
@AllArgsConstructor
public class PostController {
    private final PostService service;

    @PostMapping("/add/{country}/{region}")
    @PreAuthorize("hasAuthority('post:write')")
    public ResponseEntity<PostDTO> create(@RequestBody PostDTO post,
                                          @PathVariable("country") String countryName,
                                          @PathVariable("region") String region) {
        final PostDTO response = service.create(post, countryName, region);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/add-by-region-id/{id}")
    @PreAuthorize("hasAuthority('post:write')")
    public ResponseEntity<PostDTO> create(@RequestBody PostDTO post,
                                          @PathVariable("id") Long id) {
        final PostDTO response = service.create(post, id);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/all/{country}")
    public ResponseEntity<List<GetPostResponseByCountry>> all(@PathVariable("country") String countryName) {
        final List<GetPostResponseByCountry> response = service.all(countryName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all/{country}/{region}")
    public ResponseEntity<List<GetPostResponse>> all(@PathVariable("country") String countryName,
                                                     @PathVariable("region") String regionName) {
        final List<GetPostResponse> response = service.all(countryName, regionName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/all-by-region-id/{id}")
    public ResponseEntity<List<GetPostResponse>> all(@PathVariable("id") Long id) {
        final List<GetPostResponse> response = service.all(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-by-post-id/{id}")
    public ResponseEntity<PostDTO> get(@PathVariable("id") Long id) {
        final PostDTO response = service.get(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('post:write')")
    public ResponseEntity<PostDTO> update(@RequestBody PostDTO post) {
        final PostDTO response = service.update(post);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/important/{id}/{important}")
    @PreAuthorize("hasAuthority('post:write')")
    public ResponseEntity<Boolean> setImportant(@PathVariable("id") Long id, @PathVariable("important") Boolean important) {
        final Boolean response = service.setImportant(id, important);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('post:write')")
    public ResponseEntity<PostDTO> delete(@PathVariable("id") Long id) {
        final PostDTO response = service.delete(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-all-by-region-id/{id}")
    @PreAuthorize("hasAuthority('post:write')")
    public ResponseEntity<List<PostDTO>> deleteAllByRegionId(@PathVariable("id") Long id) {
        final List<PostDTO> response = service.deleteAllByRegionId(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-all/{country}/{region}")
    @PreAuthorize("hasAuthority('post:delete_all')")
    public ResponseEntity<List<PostDTO>> deleteAll(@PathVariable("country") String countryName,
                                                   @PathVariable("region") String region) {
        final List<PostDTO> response = service.deleteAll(countryName, region);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
