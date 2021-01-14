package lk.bit.web.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuthenticationRequestDTO {

    private String userName;
    private String password;
}
