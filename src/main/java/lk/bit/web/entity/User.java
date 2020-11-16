package lk.bit.web.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "auth_user")
public class User implements SuperEntity {

    @Id
    @Column(name = "auth_user_id", length = 50, nullable = false)
    private String id;

    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @Column(length = 20, nullable = false)
    private String nic;

    @Column(length = 100, nullable = false)
    private String address;

    @Column(length = 20, nullable = false)
    private String contact;

    @Column(unique = true, length = 20, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(length = 20, nullable = false)
    private String status;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "auth_user_role",
            joinColumns=@JoinColumn(name = "auth_user_id",referencedColumnName = "auth_user_id"),
    inverseJoinColumns = @JoinColumn(name = "auth_role_id",referencedColumnName = "auth_role_id"))
    private Set<UserRole> userRole;


    public User(String id, String firstName, String lastName, String nic, String address,
                String contact, String username, String password,String status,UserRole user) {
        userRole = new HashSet<UserRole>();

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nic = nic;
        this.address = address;
        this.contact = contact;
        this.username = username;
        this.password = password;
        userRole.add(new UserRole(user.getId(),user.getName()));
        this.status = status;
    }


}
