package lk.bit.web.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/vi/advertisements")
public class AdvertisementController {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    private void saveAdvertisement(){}
}
