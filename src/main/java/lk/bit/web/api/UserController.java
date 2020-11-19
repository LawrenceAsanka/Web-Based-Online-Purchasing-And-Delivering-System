package lk.bit.web.api;

import lk.bit.web.business.custom.UserBO;
import lk.bit.web.dto.UserDTO;
import lk.bit.web.util.UserTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
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
    public String getLastUserId() {
        return userBO.getLastUserId();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable("id") @Valid @Pattern(regexp = "U\\d{3}") String userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return userBO.getRequestedUser(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveUser(@RequestBody UserDTO user) {
        if (userBO.existUser(user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        userBO.saveUser(user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{id}/{status}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateUser(@PathVariable("id") @Valid @Pattern(regexp = "U\\d{3}") String userId,
                           @PathVariable @Valid @NotEmpty  String status,
                           @RequestBody UserDTO user) {
        if (userId == null && status == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        userBO.updateUser(user, status, userId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{id}")
    public void updateStatus(@PathVariable("id") @Valid @Pattern(regexp = "U\\d{3}") String userId,
                             @RequestParam("status")  @Valid @NotEmpty String status) {
        if (userId == null && status == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        userBO.updateStatus(userId, status);
    }

}
