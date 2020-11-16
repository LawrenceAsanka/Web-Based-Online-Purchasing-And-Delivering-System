package lk.bit.web.api;

import lk.bit.web.business.custom.UserBO;
import lk.bit.web.dto.UserDTO;
import lk.bit.web.util.UserTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserBO userBO;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserTM> getUsers() {
        return userBO.getAllUsers();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/id")
    public String getLastUserId(){
        return userBO.getLastUserId();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveUser(@RequestBody UserDTO user) {
        if (userBO.existUser(user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        userBO.saveUser(user);
    }

/*    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateUser(@PathVariable("id") String userId, @RequestBody UserDTO user) {
        if (!userBO.existUser(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        userBO.updateUser(new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getNic(),
                user.getAddress(), user.getContact(), user.getUsername(), user.getPassword(),
                user.getUserRoleId(), user.getStatus()), userId);
    }*/

/*    @ResponseStatus(HttpStatus.NO_CONTENT)
@DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable("id") String userId) {
        if (!userBO.existUser(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        userBO.deleteUser(userId);
    }*/
}
