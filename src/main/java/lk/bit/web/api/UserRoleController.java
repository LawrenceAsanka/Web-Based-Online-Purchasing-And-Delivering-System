package lk.bit.web.api;

import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1/roles")
public class UserRoleController {

   /* @Autowired
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
*/
}
