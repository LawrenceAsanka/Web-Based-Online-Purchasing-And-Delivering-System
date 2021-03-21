package lk.bit.web.dto;

import lk.bit.web.entity.Role;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CustomerDTO {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String contact;
    private String profilePicture;
    private Role role;
    private int emailVerified;
    private int accountStatus;
    private String creditLimit;

}
