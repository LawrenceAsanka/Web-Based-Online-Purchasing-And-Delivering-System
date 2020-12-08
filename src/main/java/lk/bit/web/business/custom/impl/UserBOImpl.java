package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.UserBO;
import lk.bit.web.dto.UserDTO;
import lk.bit.web.entity.CustomEntity;
import lk.bit.web.entity.User;
import lk.bit.web.entity.UserRole;
import lk.bit.web.repository.UserRepository;
import lk.bit.web.repository.UserRoleRepository;
import lk.bit.web.util.UserTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class UserBOImpl implements UserBO {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public List<UserTM> getAllUsers() {
        List<CustomEntity> allUsers = userRepository.getAllUsers();
        List<UserTM> users = new ArrayList<>();
        for (CustomEntity user : allUsers) {
            users.add(new UserTM(user.getUserId(), user.getFirstName(), user.getLastName(), user.getAddress(),
                    user.getNIC(), user.getContact(), user.getUsername(), user.getUserRole(), user.getUserStatus()));
        }
        return users;
    }

    @Override
    public UserDTO getRequestedUser(String userId) {
        String roleName = "";
        User user = userRepository.findById(userId).get();
        Set<UserRole> role = user.getUserRole();
        for (UserRole userRole : role) {
            roleName = userRole.getName();
        }

        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getNic(),
                user.getContact(), user.getAddress(), user.getUsername(), user.getPassword(), roleName);
    }

    @Override
    public void saveUser(UserDTO user) {
        UserRole userRole = userRoleRepository.findByName(user.getRole());

        userRepository.save(new User(user.getId(), user.getFirstName(), user.getLastName(), user.getNic(),
                user.getAddress(), user.getContact(), user.getUsername(), user.getPassword(),
                "ACTIVE", userRole));
    }

    @Override
    public void updateUser(UserDTO user, String status, String id) {
        UserRole userRole = userRoleRepository.findByName(user.getRole());

        userRepository.save(new User(id, user.getFirstName(), user.getLastName(), user.getNic(),
                user.getAddress(), user.getContact(), user.getUsername(), user.getPassword(), status, userRole));
    }

    @Override
    public boolean existUser(String id) {
        return userRepository.existsById(id);
    }

    @Override
    public String getLastUserId() {
        User lastUser = userRepository.findFirstLastCustomerIdByOrderByIdDesc();
        String lastUserId = lastUser.getId();

        if (lastUserId == null) {
            return "U001";
        } else {
            String id = lastUserId.replace("U", "");
            int userId = Integer.parseInt(id) + 1;
            if (userId < 10) {
                return "U00" + userId;
            } else if (userId < 100) {
                return "U0" + userId;
            } else {
                return "U" + userId;
            }
        }
    }

    @Override
    public void updateStatus(String userId, String status) {
        User user = userRepository.findById(userId).get();
        //UserRole userRole = userRoleRepository.findByName(user.getRole());
        user.setStatus(status);
        userRepository.save(user);
    }

}
