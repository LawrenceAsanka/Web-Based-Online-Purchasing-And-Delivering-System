package lk.bit.web.api;

import lk.bit.web.dto.ReturnDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController("api/v1/returns")
public class ReturnController {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    private void saveReturn(@RequestBody ReturnDTO returnDTO) {

    }
}
