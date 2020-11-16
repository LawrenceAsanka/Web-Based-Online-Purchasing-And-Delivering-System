package lk.bit.web.dto;

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
    private String contact;
    private String address;
    private String username;
    private String password;
    private String role;

}
