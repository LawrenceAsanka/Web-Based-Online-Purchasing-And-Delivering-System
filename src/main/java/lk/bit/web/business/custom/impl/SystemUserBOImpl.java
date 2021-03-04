package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.SystemUserBO;
import lk.bit.web.dto.SystemUserDTO;
import lk.bit.web.entity.CustomEntity;
import lk.bit.web.entity.SystemUser;
import lk.bit.web.repository.SystemUserRepository;
import lk.bit.web.util.tm.UserTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class SystemUserBOImpl implements SystemUserBO {

    @Autowired
    private SystemUserRepository systemUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        List<SimpleGrantedAuthority> roles = null;
        SystemUser systemUser = systemUserRepository.findSystemUser(userName);
        if (systemUser != null) {
            roles = Collections.singletonList(new SimpleGrantedAuthority(systemUser.getRole()));
            return new User(systemUser.getUsername(), systemUser.getPassword(), roles);
        }
        throw new UsernameNotFoundException("User name not found with name" + userName);
    }

    @Override
    public List<UserTM> getAllUsers() {
        List<CustomEntity> allUsers = systemUserRepository.getAllUsers();
        List<UserTM> users = new ArrayList<>();
        for (CustomEntity user : allUsers) {
            users.add(new UserTM(user.getUserId(), user.getFirstName(), user.getLastName(), user.getAddress(),
                    user.getNIC(), user.getContact(), user.getUsername(), user.getUserRole(), user.getUserStatus()));
        }
        return users;
    }

    @Override
    public SystemUserDTO getRequestedUser(String userId) {
        SystemUser systemUser = systemUserRepository.findById(userId).get();

        return new SystemUserDTO(systemUser.getId(), systemUser.getFirstName(), systemUser.getLastName(), systemUser.getNic(),
                systemUser.getContact(), systemUser.getAddress(), systemUser.getUsername(), systemUser.getPassword(), systemUser.getRole());
    }

    @Override
    public List<SystemUserDTO> getSystemUserByRoleSalePerson() {
        List<SystemUser> systemUsers = systemUserRepository.getSystemUsersByRoleSalesPerson();
        List<SystemUserDTO> systemUserDTOList = new ArrayList<>();

        for (SystemUser systemUser : systemUsers) {
            systemUserDTOList.add(new SystemUserDTO(systemUser.getId(), systemUser.getFirstName(), systemUser.getLastName(),
                    systemUser.getNic(), systemUser.getContact(), systemUser.getAddress(), systemUser.getUsername(),
                    systemUser.getPassword(), systemUser.getRole()));
        }

        return systemUserDTOList;
    }

    @Override
    public void saveUser(SystemUserDTO user) {
        systemUserRepository.save(new SystemUser(user.getId(), user.getFirstName(), user.getLastName(), user.getNic(),
                user.getAddress(), user.getContact(), user.getUsername(), passwordEncoder.encode(user.getPassword()),
                user.getRole(), "ACTIVE"));
    }

    @Override
    public void updateUser(SystemUserDTO user, String status, String id) {
        SystemUser systemUser = systemUserRepository.findById(id).get();
        if (systemUser != null) {
            SystemUser systemUserEntity = new SystemUser();
            systemUserEntity.setId(id);
            systemUserEntity.setFirstName(user.getFirstName());
            systemUserEntity.setLastName(user.getLastName());
            systemUserEntity.setNic(user.getNic());
            systemUserEntity.setAddress(user.getAddress());
            systemUserEntity.setContact(user.getContact());
            systemUserEntity.setUsername(user.getUsername());
            systemUserEntity.setStatus(status);
            if (user.getPassword().isEmpty()) {
                systemUserEntity.setPassword(systemUser.getPassword());
            } else {
                systemUserEntity.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            systemUserEntity.setRole(user.getRole());

            systemUserRepository.save(systemUserEntity);
        }

    }

    @Override
    public boolean existUser(String userName) {
        SystemUser systemUser = systemUserRepository.findSystemUser(userName);
        return systemUser != null;
    }

    @Override
    public String getLastUserId() {
        String lastSystemUserId = systemUserRepository.getLastUserId();
        String newId;

        if (lastSystemUserId == null) {
            newId = "U001";
        } else {
            String id = lastSystemUserId.replace("U", "");
            int userId = Integer.parseInt(id) + 1;
            if (userId < 10) {
                newId = "U00" + userId;
            } else if (userId < 100) {
                newId = "U0" + userId;
            } else {
                newId = "U" + userId;
            }
        }

        return newId;
    }

    @Override
    public void updateStatus(String userId, String status) {
        SystemUser systemUser = systemUserRepository.findById(userId).get();
        //UserRole userRole = userRoleRepository.findByName(user.getRole());
        systemUser.setStatus(status);
        systemUserRepository.save(systemUser);
    }


}
