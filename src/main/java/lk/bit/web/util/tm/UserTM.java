package lk.bit.web.util.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserTM {

    private String userId;
    private String firstName;
    private String lastName;
    private String address;
    private String nic;
    private String contact;
    private String username;
    private String userRole;
    private String status;
}
