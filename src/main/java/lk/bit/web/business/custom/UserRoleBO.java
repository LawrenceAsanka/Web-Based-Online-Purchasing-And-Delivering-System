package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.UserRoleDTO;
import lk.bit.web.entity.UserRole;

import java.util.List;

public interface UserRoleBO extends SuperBO {

    List<UserRoleDTO> getAllUserRoles();

    void saveUserRole(String name);

    void updateUserRole(String name, int id);

    UserRole existUserRoleByName(String name);

    boolean existUserRoleById(int id);


}
