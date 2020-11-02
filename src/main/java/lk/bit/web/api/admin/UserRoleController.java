package lk.bit.web.api.admin;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1/roles")
public class UserRoleController {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public static void addUserRole(@RequestBody ){

    }
}
