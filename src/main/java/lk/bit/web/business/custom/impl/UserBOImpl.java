package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.UserBO;
import lk.bit.web.dto.UserDTO;
import lk.bit.web.entity.User;
import lk.bit.web.repository.admin.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserBOImpl implements UserBO {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        List<UserDTO> users = new ArrayList<>();
        for (User user : allUsers) {
            users.add(new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getNic(),
                    user.getContactOne(), user.getContactTwo(), user.getUsername(), user.getPassword(),
                    user.getUserRole(), user.getStatus()));
        }
        return users;
    }

    @Override
    public void saveUser(UserDTO user) {

    }

    @Override
    public void updateUser(UserDTO user, String id) {

    }

    @Override
    public void deleteUser(String id) {

    }
}
