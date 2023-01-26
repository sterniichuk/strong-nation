package online.strongnation.controller;

import lombok.AllArgsConstructor;
import online.strongnation.model.dto.CategoryDTO;
import online.strongnation.model.dto.RegionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("region/v1")
@AllArgsConstructor
public class RegionController {
    private final RegionDTO dtoWithNameAndId = RegionDTO.builder()
            .name("Rivne").id(1L).build();
    private final CategoryDTO categoryWithUnits = CategoryDTO.builder()
            .name("food").units("kg").number(1000.1f).build();
    private final CategoryDTO categoryWithoutUnits = CategoryDTO.builder()
            .name("cars").number(101f).build();
    private final RegionDTO fullDto = RegionDTO.builder()
            .name("Rivne").id(1L).money(new BigDecimal("10010101.1"))
            .categories(List.of(categoryWithoutUnits, categoryWithUnits)).build();

    @PostMapping("/add/{country}/{name}")
    public ResponseEntity<RegionDTO> create(@PathVariable("country") String countryName,
                                            @PathVariable("name") String name) {
        return new ResponseEntity<>(dtoWithNameAndId, HttpStatus.CREATED);
    }

    @PostMapping("/add-all/{country}")
    public ResponseEntity<List<RegionDTO>> createAll(@PathVariable("country") String countryName,
                                                     @RequestBody List<String> names) {
        final long[] i = {1};
        List<RegionDTO> regions = names.stream()
                .map(s -> dtoWithNameAndId.toBuilder().name(s).id(i[0]++).build()).toList();
        return new ResponseEntity<>(regions, HttpStatus.CREATED);
    }

    @GetMapping("/get/{country}/{name}")
    public ResponseEntity<RegionDTO> get(@PathVariable("country") String countryName,
                                         @PathVariable("name") String name) {
        return new ResponseEntity<>(fullDto.toBuilder().name(name).build(), HttpStatus.OK);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<RegionDTO> get(@PathVariable("id") Long id) {
        return new ResponseEntity<>(fullDto.toBuilder().id(id).build(), HttpStatus.OK);
    }

    @GetMapping("/all/{country}")
    public ResponseEntity<List<RegionDTO>> all(@PathVariable("country") String countryName) {
        return new ResponseEntity<>(List.of(fullDto), HttpStatus.OK);
    }

    @PutMapping("/update/{country}/{oldName}/{newName}")
    public ResponseEntity<RegionDTO> rename(@PathVariable("country") String countryName,
                                            @PathVariable("oldName") String oldName,
                                             @PathVariable("newName") String newName) {
        return new ResponseEntity<>(fullDto.toBuilder().name(newName).build(), HttpStatus.OK);
    }

    @PutMapping("/update-by-id/{id}/{newName}")
    public ResponseEntity<RegionDTO> renameById(@PathVariable("id") Long id,
                                            @PathVariable("newName") String newName) {
        return new ResponseEntity<>(fullDto.toBuilder().name(newName).build(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{country}/{name}")
    public ResponseEntity<RegionDTO> delete(@PathVariable("country") String countryName,
                                            @PathVariable("name") String name) {
        return new ResponseEntity<>(fullDto.toBuilder().name(name).build(), HttpStatus.OK);
    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<RegionDTO> deleteById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(fullDto.toBuilder().id(id).build(), HttpStatus.OK);
    }

    @DeleteMapping("/delete-all-by-country/{country}")
    public ResponseEntity<List<RegionDTO>> deleteAllByCountry(@PathVariable("country") String countryName) {
        return new ResponseEntity<>(List.of(fullDto), HttpStatus.OK);
    }
}
