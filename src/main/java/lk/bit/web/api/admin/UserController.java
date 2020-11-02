package lk.bit.web.api.admin;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @GetMapping
    public static String test(){
        return "Happy coding my boy";
    }
}
