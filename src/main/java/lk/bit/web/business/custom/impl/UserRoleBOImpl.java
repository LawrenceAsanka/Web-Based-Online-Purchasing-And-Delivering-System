package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.UserRoleBO;
import lk.bit.web.dto.UserRoleDTO;
import lk.bit.web.entity.UserRole;
import lk.bit.web.repository.admin.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRoleBOImpl implements UserRoleBO {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public List<UserRoleDTO> getAllUserRoles() {
        List<UserRole> allUserRoles = userRoleRepository.findAll();

        ArrayList<UserRoleDTO> userRoles = new ArrayList<>();
        for (UserRole userRole : allUserRoles) {
            userRoles.add(new UserRoleDTO(userRole.getId(), userRole.getName()));
        }
        return userRoles;
    }

    @Override
    public void saveUserRole(String id, String name) {
        userRoleRepository.save(new UserRole(id, name));
    }

    @Override
    public void updateUserRole(String name, String id) {
        userRoleRepository.save(new UserRole(id, name));
    }

    @Override
    public void deleteUserRole(String id) {
        userRoleRepository.deleteById(id);
    }

    @Override
    public boolean existUserRole(String id) {
       return userRoleRepository.existsById(id);
    }
}
