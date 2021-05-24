package lk.bit.web.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "system_user")
public class SystemUser implements SuperEntity {

    @Id
    @Column(length = 50, nullable = false)
    private String id;

    @NotEmpty(message = "first name cannot be null")
    @Size(min = 3)
    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    @NotEmpty(message = "last name cannot be null")
    @Size(min = 3)
    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @NotEmpty
    @Column(length = 20, nullable = false)
    private String nic;

    @NotEmpty
    @Column(length = 100, nullable = false)
    private String address;

    @NotEmpty
    @Column(length = 20, nullable = false)
    private String contact;

    @NotEmpty
    @Column(unique = true, length = 20, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @NotEmpty
    @Column(nullable = false)
    private String role;

    @NotEmpty
    @Column(length = 20, nullable = false)
    private String status;

    /*@NotEmpty
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "auth_user_role",
            joinColumns=@JoinColumn(name = "auth_user_id",referencedColumnName = "auth_user_id"),
    inverseJoinColumns = @JoinColumn(name = "auth_role_id",referencedColumnName = "auth_role_id"))
    private Set<UserRole> userRole;*/

    public SystemUser(String id, String firstName, String lastName, String nic, String address,
                      String contact, String username, String password, String role) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nic = nic;
        this.address = address;
        this.contact = contact;
        this.username = username;
        this.password = password;
        this.role = role;
    }


    public SystemUser(String userId) {
        this.id = userId;
    }
}
