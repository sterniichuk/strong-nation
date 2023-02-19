package online.strongnation.business.controller;

import lombok.AllArgsConstructor;
import online.strongnation.business.service.CountryService;
import online.strongnation.business.model.dto.CountryDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static online.strongnation.business.config.SecurityConstants.FRONTEND_WITH_ENABLED_CROSS_ORIGIN_REQUESTS;
import static online.strongnation.business.config.SecurityConstants.LOCAL_HOST_WITH_ENABLED_CROSS_ORIGIN_REQUESTS;

@RestController
@RequestMapping("api/v2/country")
@CrossOrigin(origins = {LOCAL_HOST_WITH_ENABLED_CROSS_ORIGIN_REQUESTS, FRONTEND_WITH_ENABLED_CROSS_ORIGIN_REQUESTS})
@AllArgsConstructor
public class CountryController {

    CountryService service;

    @PostMapping("/add/{name}")
    @PreAuthorize("hasAuthority('country:write')")
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
    @PreAuthorize("hasAuthority('country:write')")
    public ResponseEntity<CountryDTO> rename(@PathVariable("oldName") String oldName,
                                             @PathVariable("newName") String newName) {
        var response = service.rename(oldName, newName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{name}")
    @PreAuthorize("hasAuthority('country:write')")
    public ResponseEntity<CountryDTO> delete(@PathVariable("name") String countryName) {
        var response = service.delete(countryName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-all")
    @PreAuthorize("hasAuthority('country:write')")
    public ResponseEntity<List<CountryDTO>> deleteAll() {
        var response = service.deleteAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
