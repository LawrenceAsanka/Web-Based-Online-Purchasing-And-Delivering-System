package lk.bit.web.api.admin;

import lk.bit.web.business.custom.UserBO;
import lk.bit.web.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserBO userBO;

    @GetMapping
    public List<UserDTO> getUsers() {
        return userBO.getAllUsers();
    }
}
