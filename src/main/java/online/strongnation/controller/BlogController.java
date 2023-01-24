package online.strongnation.controller;

import lombok.RequiredArgsConstructor;
import online.strongnation.dto.BlogDTO;
import online.strongnation.dto.CategoryDTO;
import online.strongnation.dto.GetBlogResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("blog/v1")
@RequiredArgsConstructor
public class BlogController {
    private final CategoryDTO categoryWithUnits = CategoryDTO.builder()
            .name("food").units("kg").number(1000.1f).build();
    private final CategoryDTO categoryWithoutUnits = CategoryDTO.builder()
            .name("cars").number(101f).build();
    private final BlogDTO fullDto = BlogDTO.builder()
            .heading("Some amazing heading that you have never seen before")
            .money(new BigDecimal("10101.12"))
            .date(LocalDateTime.now())
            .link("https://github.com/")
            .categories(List.of(categoryWithoutUnits, categoryWithUnits))
            .id(1L).build();
    private final GetBlogResponse blogResponse = GetBlogResponse.builder()
            .heading(fullDto.getHeading())
            .date(fullDto.getDate())
            .id(fullDto.getId())
            .link(fullDto.getLink())
            .build();

    @PostMapping("/add/{country}/{region}")
    public ResponseEntity<BlogDTO> create(@RequestBody BlogDTO blog,
                                          @PathVariable("country") String countryName,
                                          @PathVariable("region") String region) {
        return new ResponseEntity<>(blog.toBuilder().id(1L).build(), HttpStatus.CREATED);
    }

    @PostMapping("/add-by-region-id/{id}")
    public ResponseEntity<BlogDTO> create(@RequestBody BlogDTO blog,
                                          @PathVariable("id") Long id) {
        return new ResponseEntity<>(blog.toBuilder().id(1L).build(), HttpStatus.CREATED);
    }

    @GetMapping("/all/{country}/{region}")
    public ResponseEntity<List<GetBlogResponse>> all(@PathVariable("country") String countryName,
                                               @PathVariable("region") String regionName) {
        return new ResponseEntity<>(List.of(blogResponse), HttpStatus.OK);
    }

    @GetMapping("/all-by-region-id/{id}")
    public ResponseEntity<List<GetBlogResponse>> all(@PathVariable("id") Long id) {
        return new ResponseEntity<>(List.of(blogResponse), HttpStatus.OK);
    }

    @GetMapping("/get-by-blog-id/{id}")
    public ResponseEntity<BlogDTO> get(@PathVariable("id") Long id) {
        return new ResponseEntity<>(fullDto.toBuilder().id(id).build(), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<BlogDTO> update(@RequestBody BlogDTO blog) {
        return new ResponseEntity<>(blog, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BlogDTO> delete(@PathVariable("id") Long id) {
        return new ResponseEntity<>(fullDto.toBuilder().id(id).build(), HttpStatus.OK);
    }

    @DeleteMapping("/delete-all-by-region-id/{id}")
    public ResponseEntity<List<BlogDTO>> deleteAllByRegionId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(List.of(fullDto), HttpStatus.OK);
    }

    @DeleteMapping("/delete-all/{country}/{region}")
    public ResponseEntity<List<BlogDTO>> deleteAll(@PathVariable("country") String countryName,
                                          @PathVariable("region") String region) {
        return new ResponseEntity<>(List.of(fullDto), HttpStatus.OK);
    }
}
