package lk.bit.web.dto;

import lk.bit.web.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDTO {

    private String id;
    private String firstName;
    private String lastName;
    private String nic;
    private String contactOne;
    private String contactTwo;
    private String username;
    private String password;
    private String userRoleId;
    private String status;

}
