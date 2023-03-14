package online.strongnation.business.controller;

import lombok.AllArgsConstructor;
import online.strongnation.business.service.CountryService;
import online.strongnation.business.model.dto.CountryDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/country")
@AllArgsConstructor
public class CountryController {

    CountryService service;

    @PostMapping("/add/{name}")
    @PreAuthorize("hasAuthority('country:write')")
    public ResponseEntity<CountryDTO> create(@PathVariable("name") String countryName) {
        CountryDTO response = service.create(countryName);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/get/{name}")
    public ResponseEntity<CountryDTO> get(@PathVariable("name") String countryName) {
        CountryDTO response = service.get(countryName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CountryDTO>> getAll() {
        List<CountryDTO> response = service.getAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{oldName}/{newName}")
    @PreAuthorize("hasAuthority('country:write')")
    public ResponseEntity<CountryDTO> rename(@PathVariable("oldName") String oldName,
                                             @PathVariable("newName") String newName) {
        CountryDTO response = service.rename(oldName, newName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{name}")
    @PreAuthorize("hasAuthority('country:write')")
    public ResponseEntity<CountryDTO> delete(@PathVariable("name") String countryName) {
        CountryDTO response = service.delete(countryName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-all")
    @PreAuthorize("hasAuthority('country:write')")
    public ResponseEntity<List<CountryDTO>> deleteAll() {
        List<CountryDTO> response = service.deleteAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
