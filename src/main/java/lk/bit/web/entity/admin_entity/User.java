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
    @Column(name = "id",length = 50,nullable = false)
    private String id;
    @Column(name = "first_name",length = 100,nullable = false)
    private String firstName;
    @Column(name = "last_name",length = 100,nullable = false)
    private String lastName;
    @Column(length = 20)
    private String nic;
    @Column(name = "contact_1",length = 20,nullable = false)
    private String contactOne;
    @Column(name = "contact_2",length = 20,nullable = false)
    private String contactTwo;
    @Column(unique = true,length = 20,nullable = false)
    private String username;
    @Column(length = 20,nullable = false)
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
