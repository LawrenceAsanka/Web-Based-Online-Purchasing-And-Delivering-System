package lk.bit.web.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SignUpDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
