package lk.bit.web.api.admin_api;

import lk.bit.web.business.custom.UserBO;
import lk.bit.web.dto.admin_dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserBO userBO;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserDTO> getUsers() {
        return userBO.getAllUsers();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveUser(@RequestBody UserDTO user) {
        if (userBO.existUser(user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        userBO.saveUser(new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getNic(),
                user.getContactOne(), user.getContactTwo(), user.getUsername(), user.getPassword(),
                user.getUserRoleId(), user.getStatus()));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateUser(@PathVariable("id") String userId, @RequestBody UserDTO user) {
        if (!userBO.existUser(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        userBO.updateUser(new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getNic(),
                user.getContactOne(), user.getContactTwo(), user.getUsername(), user.getPassword(),
                user.getUserRoleId(), user.getStatus()), userId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
@DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable("id") String userId) {
        if (!userBO.existUser(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        userBO.deleteUser(userId);
    }
}
