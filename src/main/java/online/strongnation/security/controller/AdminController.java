package online.strongnation.security.controller;

import lombok.AllArgsConstructor;
import online.strongnation.business.model.dto.RegionDTO;
import online.strongnation.security.model.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin/")
@AllArgsConstructor
public class AdminController {

    @PostMapping("/add")
    public ResponseEntity<String> create(@RequestBody UserDTO user) {
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PostMapping("/add-all")
    public ResponseEntity<String> createAll(@RequestBody List<UserDTO> users) {
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<UserDTO> get(@PathVariable("id") Long id) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/get-by-email/{email}")
    public ResponseEntity<UserDTO> get(@PathVariable("email") String email) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> get() {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/update/{email}")
    public ResponseEntity<String> update(@RequestBody UserDTO user) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<RegionDTO> delete(@PathVariable("email") String email) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
