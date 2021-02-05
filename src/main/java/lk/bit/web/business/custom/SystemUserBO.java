package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.SystemUserDTO;
import lk.bit.web.util.UserTM;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface SystemUserBO extends UserDetailsService,SuperBO {

    List<UserTM> getAllUsers();

    SystemUserDTO getRequestedUser(String userId);

    void saveUser(SystemUserDTO user);

    void updateUser(SystemUserDTO user,String status, String id);

    boolean existUser(String userName);

    String getLastUserId();

    void updateStatus(String userId, String status);
}
