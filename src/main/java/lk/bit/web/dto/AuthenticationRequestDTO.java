package lk.bit.web.dto;

import lk.bit.web.entity.Role;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuthenticationRequestDTO {

    private String userName;
    private String password;
    private Role role;
}
