package lk.bit.web.entity.admin_entity;

import lk.bit.web.entity.SuperEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "user")
public class User implements SuperEntity {

    @Id
    private String id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String nic;
    @Column(name = "contact_1")
    private String contactOne;
    @Column(name = "contact_2")
    private String contactTwo;
    @Column(unique = true)
    private String username;
    private String password;
    @ManyToOne
    @JoinColumn(name = "user_role_id",
    referencedColumnName = "id",nullable = false)
    private UserRole userRole;
    private String status;


    public User(String id, String firstName, String lastName, String nic, String contactOne,
                String contactTwo, String username, String password, String userRole, String status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nic = nic;
        this.contactOne = contactOne;
        this.contactTwo = contactTwo;
        this.username = username;
        this.password = password;
        this.userRole = new UserRole(userRole);
        this.status = status;
    }


}
