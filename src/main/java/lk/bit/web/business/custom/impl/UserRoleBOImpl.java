package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.UserRoleBO;
import lk.bit.web.dto.UserRoleDTO;
import lk.bit.web.entity.UserRole;
import lk.bit.web.repository.UserRoleRepository;
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
        List<UserRoleDTO> roles = new ArrayList<>();
        for (UserRole role : allUserRoles) {
            roles.add(new UserRoleDTO(role.getId(), role.getName()));
        }
        return roles;
    }

    @Override
    public void saveUserRole(String name) {
        userRoleRepository.save(new UserRole(name));
    }

    @Override
    public void updateUserRole(String name, int id) {
        UserRole userRole = userRoleRepository.findById(id).get();
        userRole.setName(name);
        userRoleRepository.save(userRole);
    }

    @Override
    public UserRole existUserRoleByName(String name) {
        return userRoleRepository.findByName(name);
    }

    @Override
    public boolean existUserRoleById(int id) {
        return userRoleRepository.existsById(id);
    }

}
