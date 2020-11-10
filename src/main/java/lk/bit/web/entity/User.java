package lk.bit.web.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
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
}
