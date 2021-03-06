package lk.bit.web.api;

import lk.bit.web.business.custom.SystemUserBO;
import lk.bit.web.dto.SystemUserDTO;
import lk.bit.web.util.tm.UserTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/v1/users")
public class SystemUserController {

    @Autowired
    private SystemUserBO systemUserBO;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserTM> getUsers() {
        return systemUserBO.getAllUsers();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/ids")
    public String getLastUserId() {
        return systemUserBO.getLastUserId();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public SystemUserDTO getUser(@PathVariable("id") @Valid @Pattern(regexp = "U\\d{3}") String userId) {
        if (!systemUserBO.existUser(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return systemUserBO.getRequestedUser(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/salesPerson")
    public List<SystemUserDTO> getUser() {
        return systemUserBO.getSystemUserByRoleSalePerson();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/activeUsers")
    public List<UserTM> readActivateUsersDetails() {
        return systemUserBO.readActiveUsersDetails();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/deactivateUsers")
    public List<UserTM> readDeactivateUsersDetails() {
        return systemUserBO.readDeactivateUsersDetails();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveUser(@RequestBody SystemUserDTO user) {
        if (systemUserBO.existUser(user.getId()) ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        systemUserBO.saveUser(user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{id}/{status}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateUser(@PathVariable("id") @Valid @Pattern(regexp = "U\\d{3}") String userId,
                           @PathVariable @Valid @NotEmpty  String status,
                           @RequestBody SystemUserDTO user) {
        if (userId == null && status == null && !systemUserBO.existUser(user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        systemUserBO.updateUser(user, status, userId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{id}")
    public void updateStatus(@PathVariable("id") @Valid @Pattern(regexp = "U\\d{3}") String userId,
                             @RequestParam("status")  @Valid @NotEmpty String status) {
        if (userId == null && status == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        systemUserBO.updateStatus(userId, status);
    }

}
