package online.strongnation.controller;

import lombok.RequiredArgsConstructor;
import online.strongnation.model.dto.PostDTO;
import online.strongnation.model.dto.CategoryDTO;
import online.strongnation.model.dto.GetPostResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("post/v1")
@RequiredArgsConstructor
public class PostController {
    private final CategoryDTO categoryWithUnits = CategoryDTO.builder()
            .name("food").units("kg").number(1000.1f).build();
    private final CategoryDTO categoryWithoutUnits = CategoryDTO.builder()
            .name("cars").number(101f).build();
    private final PostDTO fullDto = PostDTO.builder()
            .heading("Some amazing heading that you have never seen before")
            .money(new BigDecimal("10101.12"))
            .date(LocalDateTime.now())
            .link("https://github.com/")
            .categories(List.of(categoryWithoutUnits, categoryWithUnits))
            .id(1L).build();
    private final GetPostResponse postResponse = GetPostResponse.builder()
            .heading(fullDto.getHeading())
            .date(fullDto.getDate())
            .id(fullDto.getId())
            .link(fullDto.getLink())
            .build();

    @PostMapping("/add/{country}/{region}")
    public ResponseEntity<PostDTO> create(@RequestBody PostDTO post,
                                          @PathVariable("country") String countryName,
                                          @PathVariable("region") String region) {
        return new ResponseEntity<>(post.toBuilder().id(1L).build(), HttpStatus.CREATED);
    }

    @PostMapping("/add-by-region-id/{id}")
    public ResponseEntity<PostDTO> create(@RequestBody PostDTO post,
                                          @PathVariable("id") Long id) {
        return new ResponseEntity<>(post.toBuilder().id(1L).build(), HttpStatus.CREATED);
    }

    @GetMapping("/all/{country}/{region}")
    public ResponseEntity<List<GetPostResponse>> all(@PathVariable("country") String countryName,
                                                     @PathVariable("region") String regionName) {
        return new ResponseEntity<>(List.of(postResponse), HttpStatus.OK);
    }

    @GetMapping("/all-by-region-id/{id}")
    public ResponseEntity<List<GetPostResponse>> all(@PathVariable("id") Long id) {
        return new ResponseEntity<>(List.of(postResponse), HttpStatus.OK);
    }

    @GetMapping("/get-by-post-id/{id}")
    public ResponseEntity<PostDTO> get(@PathVariable("id") Long id) {
        return new ResponseEntity<>(fullDto.toBuilder().id(id).build(), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<PostDTO> update(@RequestBody PostDTO post) {
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<PostDTO> delete(@PathVariable("id") Long id) {
        return new ResponseEntity<>(fullDto.toBuilder().id(id).build(), HttpStatus.OK);
    }

    @DeleteMapping("/delete-all-by-region-id/{id}")
    public ResponseEntity<List<PostDTO>> deleteAllByRegionId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(List.of(fullDto), HttpStatus.OK);
    }

    @DeleteMapping("/delete-all/{country}/{region}")
    public ResponseEntity<List<PostDTO>> deleteAll(@PathVariable("country") String countryName,
                                                   @PathVariable("region") String region) {
        return new ResponseEntity<>(List.of(fullDto), HttpStatus.OK);
    }
}
