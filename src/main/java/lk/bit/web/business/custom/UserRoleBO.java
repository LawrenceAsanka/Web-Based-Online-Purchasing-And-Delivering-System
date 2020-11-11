package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.admin_dto.UserRoleDTO;

import java.util.List;

public interface UserRoleBO extends SuperBO {

    List<UserRoleDTO> getAllUserRoles();

    void saveUserRole(String id, String name);

    void updateUserRole(String name, String id);

    void deleteUserRole(String id);

    boolean existUserRole(String id);
}
