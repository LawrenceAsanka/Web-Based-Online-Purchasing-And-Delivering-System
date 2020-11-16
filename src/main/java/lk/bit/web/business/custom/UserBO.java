package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.UserDTO;
import lk.bit.web.util.UserTM;

import java.util.List;

public interface UserBO extends SuperBO {

    List<UserTM> getAllUsers();

    void saveUser(UserDTO user);

    void updateUser(UserDTO user, String id);

    void deleteUser(String id);

    boolean existUser(String id);

    String getLastUserId();
}
