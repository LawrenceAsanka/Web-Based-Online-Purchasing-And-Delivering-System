package lk.bit.web.api;

import lk.bit.web.business.custom.UserRoleBO;
import lk.bit.web.dto.UserRoleDTO;
import lk.bit.web.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/roles")
public class UserRoleController {

    @Autowired
    private UserRoleBO userRoleBO;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserRoleDTO> getUserRoles(){
        return userRoleBO.getAllUserRoles();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{roleName}")
    public UserRoleDTO getUserRole(@PathVariable @Valid @NotEmpty String roleName){
        UserRole userRole = userRoleBO.existUserRoleByName(roleName);
        if (userRole == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return new UserRoleDTO(userRole.getId(), userRole.getName());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addUserRole(@RequestBody UserRoleDTO userRole) {
        if (userRole.getRoleName() == null || userRoleBO.existUserRoleByName(userRole.getRoleName()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        userRoleBO.saveUserRole(userRole.getRoleName()
        );
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{id}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateUserRole(@PathVariable @Valid @NotEmpty int id,
                               @RequestBody UserRoleDTO userRole){
        if (!userRoleBO.existUserRoleById(id) || userRole.getRoleName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        userRoleBO.updateUserRole(userRole.getRoleName(),id);
    }

}
