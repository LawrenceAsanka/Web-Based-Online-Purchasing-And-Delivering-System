package lk.bit.web.entity;

import lk.bit.web.entity.SuperEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "customer")
public class Customer implements SuperEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id",length = 20)
    private int customerId;

    @Column(name = "customer_fname",length = 50,nullable = false)
    private String customerFirstName;

    @Column(name = "customer_lname",length = 50,nullable = false)
    private String customerLastName;

    @Column(name = "customer_email",length = 50,unique = true,nullable = false)
    private String customerEmail;

    @Column(length = 10,nullable = false)
    private String password;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(length = 20,nullable = false)
    private String status;

    public Customer(String customerFirstName, String customerLastName, String customerEmail, String password, String profilePicture, String status) {
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.customerEmail = customerEmail;
        this.password = password;
        this.profilePicture = profilePicture;
        this.status = status;
    }

    public Customer(int ownerId){
       this.customerId = ownerId;
    }
}
