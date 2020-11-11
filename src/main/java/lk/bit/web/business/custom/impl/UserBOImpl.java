package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.UserBO;
import lk.bit.web.dto.admin_dto.UserDTO;
import lk.bit.web.entity.User;
import lk.bit.web.repository.admin.UserRepository;
import lk.bit.web.repository.admin.UserRoleRepository;
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
    public List<UserDTO> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        List<UserDTO> users = new ArrayList<>();
        for (User user : allUsers) {
            users.add(new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getNic(),
                    user.getContactOne(), user.getContactTwo(), user.getUsername(), user.getPassword(),
                    user.getUserRole().getId(), user.getStatus()));
        }
        return users;
    }

    @Override
    public void saveUser(UserDTO user) {
        userRepository.save(new User(user.getId(), user.getFirstName(), user.getLastName(), user.getNic(),
                user.getContactOne(), user.getContactTwo(), user.getUsername(), user.getPassword(),
                user.getUserRoleId(), user.getStatus()));
    }

    @Override
    public void updateUser(UserDTO user, String id) {
        userRepository.save(new User(id, user.getFirstName(), user.getLastName(), user.getNic(),
                user.getContactOne(), user.getContactTwo(), user.getUsername(), user.getPassword(),
                user.getUserRoleId(), user.getStatus()));
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);

    }

    @Override
    public boolean existUser(String id) {
        return userRepository.existsById(id);
    }
}
