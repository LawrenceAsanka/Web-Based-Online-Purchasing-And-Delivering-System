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

@Component
public class UserBOImpl implements UserBO {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public List<UserTM> getAllUsers() {
        List<CustomEntity> allUsers = userRepository.getAllUsers();
        List<UserTM> users  = new ArrayList<>();
        for (CustomEntity user : allUsers) {
            users.add(new UserTM(user.getUserId(),user.getFirstName(),user.getLastName(),user.getAddress(),
                    user.getNIC(),user.getContact(),user.getUsername(),user.getUserRole()));
        }
        return users;
    }

    @Override
    public void saveUser(UserDTO user) {

        UserRole userRole = userRoleRepository.findByName(user.getRole());
        //System.out.println(userRole);
        userRepository.save(new User(user.getId(), user.getFirstName(), user.getLastName(), user.getNic(),
                user.getAddress(), user.getContact(), user.getUsername(), user.getPassword(),
                "ACTIVE", userRole));
    }

    @Override
    public void updateUser(UserDTO user, String id) {

    }

    @Override
    public void deleteUser(String id) {

    }

    @Override
    public boolean existUser(String id) {
        return false;
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

   /* @Override
    public List<UserDTO> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        List<UserDTO> users = new ArrayList<>();
        for (User user : allUsers) {
            users.add(new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getNic(),
                    user.getAddress(), user.getContact(), user.getUsername(), user.getPassword(),
                    user.getUserRole().getId(), user.getStatus()));
        }
        return users;
    }

    @Override
    public void saveUser(UserDTO user) {
        userRepository.save(new User(user.getId(), user.getFirstName(), user.getLastName(), user.getNic(),
                user.getAddress(), user.getContact(), user.getUsername(), user.getPassword(),
                user.getUserRoleId(), user.getStatus()));
    }

    @Override
    public void updateUser(UserDTO user, String id) {
        userRepository.save(new User(id, user.getFirstName(), user.getLastName(), user.getNic(),
                user.getAddress(), user.getContact(), user.getUsername(), user.getPassword(),
                user.getUserRoleId(), user.getStatus()));
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);

    }

    @Override
    public boolean existUser(String id) {
        return userRepository.existsById(id);
    }*/
}
