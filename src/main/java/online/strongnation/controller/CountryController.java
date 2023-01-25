package online.strongnation.controller;

import lombok.AllArgsConstructor;
import online.strongnation.dto.CountryDTO;
import online.strongnation.service.CountryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("country/v1")
@AllArgsConstructor
public class CountryController {

    CountryService service;

    @PostMapping("/add/{name}")
    public ResponseEntity<CountryDTO> create(@PathVariable("name") String countryName) {
        var response = service.create(countryName);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/get/{name}")
    public ResponseEntity<CountryDTO> get(@PathVariable("name") String countryName) {
        var response = service.get(countryName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CountryDTO>> getAll() {
        var response = service.getAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{oldName}/{newName}")
    public ResponseEntity<CountryDTO> rename(@PathVariable("oldName") String oldName,
                                             @PathVariable("newName") String newName) {
        var response = service.rename(oldName, newName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<CountryDTO> delete(@PathVariable("name") String countryName) {
        var response = service.delete(countryName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<List<CountryDTO>> deleteAll() {
        var response = service.deleteAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
