package online.strongnation.business.controller;

import lombok.AllArgsConstructor;
import online.strongnation.business.service.RegionService;
import online.strongnation.business.model.dto.RegionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/region")
@AllArgsConstructor
public class RegionController {

    private final RegionService service;

    @PostMapping("/add/{country}/{name}")
    @PreAuthorize("hasAuthority('region:write')")
    public ResponseEntity<RegionDTO> create(@PathVariable("country") String countryName,
                                            @PathVariable("name") String name) {
        final var response = service.create(countryName, name);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/add-all/{country}")
    @PreAuthorize("hasAuthority('region:write')")
    public ResponseEntity<List<RegionDTO>> createAll(@PathVariable("country") String countryName,
                                                     @RequestBody List<String> names) {
        final var response = service.createAll(countryName, names);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/get/{country}/{name}")
    public ResponseEntity<RegionDTO> get(@PathVariable("country") String countryName,
                                         @PathVariable("name") String name) {
        final var response = service.get(countryName, name);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<RegionDTO> get(@PathVariable("id") Long id) {
        final var response = service.get(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all/{country}")
    public ResponseEntity<List<RegionDTO>> all(@PathVariable("country") String countryName) {
        final var response = service.all(countryName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{country}/{oldName}/{newName}")
    @PreAuthorize("hasAuthority('region:write')")
    public ResponseEntity<RegionDTO> rename(@PathVariable("country") String countryName,
                                            @PathVariable("oldName") String oldName,
                                            @PathVariable("newName") String newName) {
        final var response = service.rename(countryName, oldName, newName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update-by-id/{id}/{newName}")
    @PreAuthorize("hasAuthority('region:write')")
    public ResponseEntity<RegionDTO> renameById(@PathVariable("id") Long id,
                                                @PathVariable("newName") String newName) {
        final var response = service.rename(id, newName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{country}/{name}")
    @PreAuthorize("hasAuthority('region:write')")
    public ResponseEntity<RegionDTO> delete(@PathVariable("country") String countryName,
                                            @PathVariable("name") String name) {
        final var response = service.delete(countryName, name);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-by-id/{id}")
    @PreAuthorize("hasAuthority('region:write')")
    public ResponseEntity<RegionDTO> deleteById(@PathVariable("id") Long id) {
        final var response = service.delete(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-all-by-country/{country}")
    @PreAuthorize("hasAuthority('region:write')")
    public ResponseEntity<List<RegionDTO>> deleteAllByCountry(@PathVariable("country") String countryName) {
        final var response = service.deleteAllByCountry(countryName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
