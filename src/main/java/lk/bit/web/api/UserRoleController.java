package lk.bit.web.api;

import lk.bit.web.business.custom.UserRoleBO;
import lk.bit.web.dto.UserRoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

 /*   @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addUserRole(@RequestBody UserRoleDTO userRole) {
        if (userRoleBO.existUserRole(userRole.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        userRoleBO.saveUserRole(userRole.getId(), userRole.getRoleName()
        );
    }*/

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{id}")
    public void updateUserRole(@PathVariable String id,
                               @RequestBody UserRoleDTO userRole
    ){
        if (!userRoleBO.existUserRole(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        userRoleBO.updateUserRole(userRole.getRoleName(), id);
        System.out.println(id);
    }

    @DeleteMapping("/{id}")
    public String deleteUserRole(@PathVariable String id){
        return id;
    }
}
