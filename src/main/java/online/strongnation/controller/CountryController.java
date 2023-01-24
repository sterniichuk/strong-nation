package online.strongnation.controller;

import lombok.AllArgsConstructor;
import online.strongnation.dto.CategoryDTO;
import online.strongnation.dto.CountryDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("country/v1")
@AllArgsConstructor
public class CountryController {

    private final CountryDTO dtoWithNameAndId = CountryDTO.builder()
            .name("Ukraine").id(1L).build();
    private final CategoryDTO categoryWithUnits = CategoryDTO.builder()
            .name("food").units("kg").number(1000.1f).build();
    private final CategoryDTO categoryWithoutUnits = CategoryDTO.builder()
            .name("cars").number(10f).build();
    private final CountryDTO fullDto = CountryDTO.builder()
            .name("Ukraine").id(1L).money(new BigDecimal("10010101.1"))
            .categories(List.of(categoryWithoutUnits, categoryWithUnits)).build();

    @PostMapping("/add/{name}")
    public ResponseEntity<CountryDTO> create(@PathVariable("name") String countryName) {
        return new ResponseEntity<>(dtoWithNameAndId.toBuilder().name(countryName).build(), HttpStatus.CREATED);
    }

    @GetMapping("/get/{name}")
    public ResponseEntity<CountryDTO> get(@PathVariable("name") String countryName) {
        return new ResponseEntity<>(fullDto.toBuilder().name(countryName).build(), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CountryDTO>> getAll() {
        return new ResponseEntity<>(List.of(fullDto), HttpStatus.OK);
    }

    @PutMapping("/update/{oldName}/{newName}")
    public ResponseEntity<CountryDTO> rename(@PathVariable("oldName") String oldName,
                                             @PathVariable("newName") String newName) {
        return new ResponseEntity<>(fullDto.toBuilder().name(newName).build(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<CountryDTO> delete(@PathVariable("name") String countryName) {
        return new ResponseEntity<>(fullDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<List<CountryDTO>> deleteAll() {
        return new ResponseEntity<>(List.of(fullDto), HttpStatus.OK);
    }
}
